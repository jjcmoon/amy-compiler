package amyc
package analyzer

import utils._
import ast.{Identifier, NominalTreeModule => N, SymbolicTreeModule => S}

// Name analyzer for Amy
// Takes a nominal program (names are plain strings, qualified names are string pairs)
// and returns a symbolic program, where all names have been resolved to unique Identifiers.
// Rejects programs that violate the Amy naming rules.
// Also populates and returns the symbol table.
object NameAnalyzer extends Pipeline[N.Program, (S.Program, SymbolTable)] {
  def run(ctx: Context)(p: N.Program): (S.Program, SymbolTable) = {
    import ctx.reporter._

    // Step 0: Initialize symbol table
    val table = new SymbolTable

    // Step 1: Add modules to table 
    val modNames = p.modules.groupBy(_.name)
    modNames.foreach { case (name, modules) =>
      if (modules.size > 1) {
        fatal(s"Two modules named $name in program", modules.head.position)
      }
    }

    modNames.keys.toList foreach table.addModule


    // Helper method: will transform a nominal type 'tt' to a symbolic type,
    // given that we are within module 'inModule'.
    def transformType(tt: N.TypeTree, inModule: String): S.Type = {
      tt.tpe match {
        case N.IntType => S.IntType
        case N.BooleanType => S.BooleanType
        case N.StringType => S.StringType
        case N.UnitType => S.UnitType
        case N.ClassType(qn@N.QualifiedName(module, name)) =>
          table.getType(module getOrElse inModule, name) match {
            case Some(symbol) =>
              S.ClassType(symbol)
            case None =>
              fatal(s"Could not find type $qn", tt)
          }
      }
    }

    // Step 2: Check name uniqueness of definitions in each module
    for (mod <- p.modules) {
      val defNames = mod.defs.groupBy(_.name)
      defNames.foreach { case (name,defs) =>
        if (defs.size > 1) {
          fatal(s"Two definitions named $name in same module", defs.head.position)
        } } 
    }

    // Step 3: Discover types and add them to symbol table
    for (mod <- p.modules) {
      mod.defs.foreach { 
        case N.AbstractClassDef(name) =>
          table.addType(mod.name, name)
        case _ => ()
      }
    }

    // Step 4: Discover type constructors, add them to table
    for (mod <- p.modules) {
      mod.defs.foreach { 
        case N.CaseClassDef(name, fields, parent) => {
          val symparent = table.getType(mod.name, parent) match {
            case None => fatal(s"Parent $parent of $name not found.")
            case Some(x) => x
          }
          val symargs = fields map (tt => transformType(tt, mod.name))
          table.addConstructor(mod.name, name, symargs, symparent)
        }
        case _ => ()
      }
    }

    // Step 5: Discover functions signatures, add them to table
    for (mod <- p.modules) {
      mod.defs.foreach { 
        case N.FunDef(name, params, retType, body) =>
          val symArgTypes = params map { case N.ParamDef(_, tt) => transformType(tt, mod.name)}
          val symRetType = transformType(retType, mod.name)
          table.addFunction(mod.name, name, symArgTypes, symRetType)
        case _ => ()
      }
    }

    // Step 6: We now know all definitions in the program.
    //         Reconstruct modules and analyse function bodies/ expressions
    
    // This part is split into three transfrom functions,
    // for definitions, FunDefs, and expressions.
    // Keep in mind that we transform constructs of the NominalTreeModule 'N' to respective constructs of the SymbolicTreeModule 'S'.
    // transformFunDef is given as an example, as well as some code for the other ones

    def transformDef(df: N.ClassOrFunDef, module: String): S.ClassOrFunDef = { df match {
      case N.AbstractClassDef(name) => table.getType(module, name) match {
        case Some(tt) => S.AbstractClassDef(tt).setPos(df)
        case None => fatal("Abstract class not found")
      }
      case N.CaseClassDef(name, fields, _) => table.getConstructor(module, name) match {
        case Some((id, ConstrSig(argT, parent, _))) => {
          S.CaseClassDef(
            id,
            fields zip argT map {case (x,t) =>S.TypeTree(t).setPos(x)}, 
            parent
          ).setPos(df)
        }
        case None => fatal("Case class not found")
      }
      case fd: N.FunDef =>
        transformFunDef(fd, module)
    }}.setPos(df)

    def transformFunDef(fd: N.FunDef, module: String): S.FunDef = {
      val N.FunDef(name, params, retType, body) = fd
      val Some((sym, sig)) = table.getFunction(module, name)

      params.groupBy(_.name).foreach { case (name, ps) =>
        if (ps.size > 1) {
          fatal(s"Two parameters named $name in function ${fd.name}", fd)
        }
      }

      val paramNames = params.map(_.name)

      val newParams = params zip sig.argTypes map { case (pd@N.ParamDef(name, tt), tpe) =>
        val s = Identifier.fresh(name)
        S.ParamDef(s, S.TypeTree(tpe).setPos(tt)).setPos(pd)
      }

      val paramsMap = paramNames.zip(newParams.map(_.name)).toMap

      S.FunDef(
        sym,
        newParams,
        S.TypeTree(sig.retType).setPos(retType),
        transformExpr(body)(module, (paramsMap, Map()))
      ).setPos(fd)
    }

    def transformLit(lit: N.Literal[Any]): S.Literal[Any] = { 
      val newLit = lit match {
        case N.IntLiteral(v) => S.IntLiteral(v)
        case N.BooleanLiteral(v) => S.BooleanLiteral(v)
        case N.StringLiteral(v) => S.StringLiteral(v)
        case N.UnitLiteral() => S.UnitLiteral()
      }
      newLit.setPos(lit)
    }

    // This function takes as implicit a pair of two maps:
    // The first is a map from names of parameters to their unique identifiers,
    // the second is similar for local variables.
    // Make sure to update them correctly if needed given the scoping rules of Amy
    def transformExpr(expr: N.Expr)
                     (implicit module: String, names: (Map[String, Identifier], Map[String, Identifier])): S.Expr = {
      val (params, locals) = names
      val res = expr match {
        case N.Match(scrut, cases) =>
          // Returns a transformed pattern along with all bindings
          // from strings to unique identifiers for names bound in the pattern.
          // Also, calls 'fatal' if a new name violates the Amy naming rules.
          def transformPattern(pattern: N.Pattern): (S.Pattern, List[(String, Identifier)]) = {
            val newPattern = pattern match {
              case N.WildcardPattern() => (S.WildcardPattern(), Nil)
              case N.LiteralPattern(lit) => (S.LiteralPattern(transformLit(lit)), Nil)
              case N.IdPattern(name) => {
                if (locals contains name)
                  fatal("Pattern $name collides with existing name")
                else {
                  val newId = Identifier.fresh(name)
                  (S.IdPattern(newId), List((name, newId)))
                }
              }
              case N.CaseClassPattern(constr, args) => {
                val rec:List[(S.Pattern, List[(String, Identifier)])] = args map transformPattern
                val (id, ConstrSig(argTypes,_,_)) = table.getConstructor(module, constr.name) match {
                  case Some(x) => x
                  case None => fatal(s"Case class ${constr.name} not found")
                }
                if (args.length != argTypes.length)
                  fatal(s"${constr.name} requires ${argTypes.length} parameters, but ${args.length} were found", expr)

                (S.CaseClassPattern(id, rec map (_._1)), rec flatMap (_._2))
              }
            }
            (newPattern._1.setPos(pattern), newPattern._2)
          }
          

          def transformCase(cse: N.MatchCase) = {
            val N.MatchCase(pat, rhs) = cse
            val (newPat, moreLocals) = transformPattern(pat)
            moreLocals.groupBy(_._1) foreach {
              case (name,ps) => if (ps.size>1) fatal("Two arguments named $name in same pattern", cse)
            }
            S.MatchCase(newPat, transformExpr(rhs)(module, (params, locals++moreLocals.toMap)))
          }

          S.Match(transformExpr(scrut), cases.map(transformCase))

        case N.Plus(lhs,rhs)       => S.Plus(transformExpr(lhs), transformExpr(rhs))
        case N.Minus(lhs,rhs)      => S.Minus(transformExpr(lhs), transformExpr(rhs))
        case N.Times(lhs,rhs)      => S.Times(transformExpr(lhs), transformExpr(rhs))
        case N.Div(lhs,rhs)        => S.Div(transformExpr(lhs), transformExpr(rhs))
        case N.Mod(lhs,rhs)        => S.Mod(transformExpr(lhs), transformExpr(rhs))
        case N.LessThan(lhs,rhs)   => S.LessThan(transformExpr(lhs), transformExpr(rhs))
        case N.LessEquals(lhs,rhs) => S.LessEquals(transformExpr(lhs), transformExpr(rhs))
        case N.And(lhs,rhs)        => S.And(transformExpr(lhs), transformExpr(rhs))
        case N.Or(lhs,rhs)         => S.Or(transformExpr(lhs), transformExpr(rhs))
        case N.Equals(lhs,rhs)     => S.Equals(transformExpr(lhs), transformExpr(rhs))
        case N.Concat(lhs,rhs)     => S.Concat(transformExpr(lhs), transformExpr(rhs))

        case N.Not(e) => S.Not(transformExpr(e))
        case N.Neg(e) => S.Neg(transformExpr(e))

        case N.Variable(name) => {
          if (locals contains name)
            S.Variable(locals(name))
          else if (params contains name)
            S.Variable(params(name))
          else
            fatal(s"Variable $name not found", expr)
        }

        case N.Error(msg) => S.Error(transformExpr(msg))
        case N.Ite(cond, thenn, elze) => 
          S.Ite(transformExpr(cond), transformExpr(thenn), transformExpr(elze))
        case N.Let(df, value, body) => {
          val N.ParamDef(name, tt) = df
          if (locals contains name)
            fatal(s"Variable $name already defined", df)
          if (params contains name)
            warning(s"Shadowing parameter $name", df)
          val newLocals = locals + (name -> Identifier.fresh(name))
          val newParamdef = S.ParamDef(newLocals(name), S.TypeTree(transformType(tt, module)))
          S.Let(
            newParamdef,
            transformExpr(value),
            transformExpr(body)(module, (params, newLocals))  
          )
        }

        case N.Sequence(e1, e2) =>
          S.Sequence(transformExpr(e1), transformExpr(e2))

        case N.Call(qname, args:List[N.Expr]) => {
          val owner = qname match {
            case N.QualifiedName(Some(mod), _) => mod
            case _ => module
          }
          val (id, sig) = table.getFunction(owner, qname.name) match {
            case Some(x) => x
            case None => table.getConstructor(owner, qname.name) match {
              case Some(x) => x
              case None => fatal(s"No function or constructor ${qname.name} found in module $owner",expr)
            }
          }
          val argTypes = sig match {
            case FunSig(argT, _, _) => argT
            case ConstrSig(argT, _, _) => argT
          }
          if (args.length != argTypes.length)
            fatal(s"${qname.name} requires ${argTypes.length} parameters, but ${args.length} were found", expr)
          S.Call(id, args map transformExpr)
        }

        case (lit:N.Literal[Any]) => transformLit(lit)

        case x => {
          println(x)
          fatal("unimplemented", expr)
        }
      }
      res.setPos(expr)
    }

    // Putting it all together to construct the final program for step 6.
    val newProgram = S.Program(
      p.modules map { case mod@N.ModuleDef(name, defs, optExpr) =>
        S.ModuleDef(
          table.getModule(name).get,
          defs map (transformDef(_, name)),
          optExpr map (transformExpr(_)(name, (Map(), Map())))
        ).setPos(mod)
      }
    ).setPos(p)
    (newProgram, table)

  }
}
