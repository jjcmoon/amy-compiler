package amyc.wasm

import scala.language.implicitConversions
import amyc.utils._
import Instructions._

// Printer for Wasm modules
object ModulePrinter {
  private implicit def s2d(s: String) = Raw(s)

  private def mkMod(mod: Module): Document = Stacked(
    "(module ",
    Indented(Stacked(mod.imports map mkImport)),
    Indented("(global (mut i32) i32.const 0) " * mod.globals),
    Indented(mkTable(mod.functions.filter(_.index>=0))),
    Indented("(type $return_iti (func (param i32) (result i32)))"),
    Indented(Stacked(mod.functions map mkFun)),
    ")"
  )

  private def mkImport(s: String): Document =
    Lined(List("(import ", s, ")"))

  private def mkTable(fhs: List[Function]): Document = {
    val funcs = sortFuncs(fhs.filter(_.index>=0))
    Stacked(
      Lined(List("(table (export \"lookup\") ", fhs.length.toString, " anyfunc)")),
      "(elem (i32.const 0) ", 
      Indented(Stacked(funcs)), 
      ")"
    )
  }

  private def sortFuncs(fhs: List[Function]): List[Document] =
    fhs.sortWith(_.index < _.index).map(x=>Lined(List("$", x.name, " ")))

  private def mkFun(fh: Function): Document = {
    val name = fh.name
    val isMain = fh.isMain
    val exportDoc: Document = if (isMain) s"""(export "$name" (func $$$name))""" else ""
    val paramsDoc: Document = if (fh.args == 0) "" else {
      Lined(List(
        "(param ",
        Lined(List.fill(fh.args)(Raw("i32")), " "),
        ") "
      ))
    }
    val resultDoc: Document = if (isMain) "" else "(result i32) "
    val localsDoc: Document =
      if (fh.locals > 0)
        "(local " <:> Lined(List.fill(fh.locals)(Raw("i32")), " ") <:> ")"
      else
        ""

    Stacked(
      exportDoc,
      Lined(List(s"(func $$${fh.name} ", paramsDoc, resultDoc, localsDoc)),
      Indented(Stacked(mkCode(fh.code))),
      ")"
    )
  }

  private def mkCode(code: Code): List[Document] = code.instructions match {
    case Nil => Nil
    case h :: t => h match {
      case Else =>
        Unindented(mkInstr(h)) ::
        mkCode(t)
      case End =>
        Unindented(mkInstr(h)) ::
        (mkCode(t) map Unindented)
      case If_void | If_i32 | Block(_) | Loop(_) | Block32(_) | Loop32(_) =>
        mkInstr(h) ::
        (mkCode(t) map Indented)
      case _ =>
        mkInstr(h) ::
        mkCode(t)
    }
  }

  private def mkInstr(instr: Instruction): Document = {
    instr match {
      case Const(value) => s"i32.const $value"
      case Add => "i32.add"
      case Sub => "i32.sub"
      case Mul => "i32.mul"
      case Div => "i32.div_s"
      case Rem => "i32.rem_s"
      case And => "i32.and"
      case Or  => "i32.or"
      case Eqz => "i32.eqz"
      case Lt_s => "i32.lt_s"
      case Le_s => "i32.le_s"
      case Eq => "i32.eq"
      case Drop => "drop"
      case Nop => "nop"
      case If_void => "if"
      case If_i32 => "if (result i32)"
      case Else => "else"
      case Block(label) => s"block $$$label"
      case Loop(label) => s"loop $$$label"
      case Block32(label) => s"block $$$label (result i32)"
      case Loop32(label) => s"loop $$$label (result i32)"
      case Call_indirect => "call_indirect $return_iti"
      case Br(label)=> s"br $$$label"
      case Return => "ret"
      case End => "end"
      case Call(name) => s"call $$$name"
      case Unreachable => "unreachable"
      case GetLocal(index) => s"get_local $index"
      case SetLocal(index) => s"set_local $index"
      case GetGlobal(index) => s"get_global $index"
      case SetGlobal(index) => s"set_global $index"
      case Store => "i32.store"
      case Load => "i32.load"
      case Store8 => "i32.store8"
      case Load8_u => "i32.load8_u"
    }
  }

  def apply(mod: Module) = mkMod(mod).print
  def apply(fh: Function) = mkFun(fh).print
  def apply(instr: Instruction) = mkInstr(instr).print

}
