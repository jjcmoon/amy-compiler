package amyc
package parsing

import utils._
import scala.io.Source
import java.io.File

import scala.util.Try

// The lexer for Amy.
// Transforms an iterator coming from scala.io.Source to a stream of (Char, Position),
// then uses a functional approach to consume the stream.
object Lexer extends Pipeline[List[File], Stream[Token]] {
	import Tokens._

	/** Maps a string s to the corresponding keyword,
		* or None if it corresponds to no keyword
		*/
	private def keywords(s: String): Option[Token] = s match {
		case "abstract" => Some(ABSTRACT())
		case "Boolean"  => Some(BOOLEAN())
		case "case"     => Some(CASE())
		case "class"    => Some(CLASS())
		case "def"      => Some(DEF())
		case "else"     => Some(ELSE())
		case "error"    => Some(ERROR())
		case "extends"  => Some(EXTENDS())
		case "false"    => Some(FALSE())
		case "if"       => Some(IF())
		case "Int"      => Some(INT())
		case "match"    => Some(MATCH())
		case "object"   => Some(OBJECT())
		case "String"   => Some(STRING())
		case "true"     => Some(TRUE())
		case "Unit"     => Some(UNIT())
		case "val"      => Some(VAL())
		case _          => None
	}

	private def lexFile(ctx: Context)(f: File): Stream[Token] = {
		import ctx.reporter._

		// Special character which represents the end of an input file
		val EndOfFile: Char = scala.Char.MaxValue
		val EndOfLine: Char = '\n'
		val source = Source.fromFile(f)

		// Useful type alias:
		// The input to the lexer will be a stream of characters,
		// along with their positions in the files
		type Input = (Char, Position)

		def mkPos(i: Int) = Position.fromFile(f, i)

		// The input to the lexer
		val inputStream: Stream[Input] =
			source.toStream.map(c => (c, mkPos(source.pos))) #::: Stream((EndOfFile, mkPos(source.pos)))

		/** Gets rid of whitespaces and comments and calls readToken to get the next token.
			* Returns the first token and the remaining input that did not get consumed
			*/
		@scala.annotation.tailrec
		def nextToken(stream: Stream[Input]): (Token, Stream[Input]) = {
			require(stream.nonEmpty)
			var (currentChar, currentPos) #:: rest = stream

			// Use with care!
			def nextChar = rest.head._1

			if (Character.isWhitespace(currentChar)) {
				nextToken(stream.dropWhile{ case (c, _) => Character.isWhitespace(c) } )
			} else if (currentChar == '/' && nextChar == '/') {
				// Single-line comment
				nextToken(stream.dropWhile{ case (c, _) => c!=EndOfLine && c!=EndOfFile})
			} else if (currentChar == '/' && nextChar == '*') {
				// Multi-line comment
				def chipComment(stream: Stream[Input]): Stream[Input] = {
					val rstream = stream.dropWhile{ case(c, _) => c!='*'}
					Try(rstream.tail.head._1).toOption match {
						case None => {
							ctx.reporter.fatal("Unclosed comment.", currentPos)
						}
						case Some('/') => rstream.tail.tail
						case Some(_) => chipComment(rstream.tail)
					}
				}
				nextToken(chipComment(rest.tail))

			} else {
				readToken(stream);
			}
		}

		/** Reads the next token from the stream. Assumes no whitespace or comments at the beginning.
			* Returns the first token and the remaining input that did not get consumed.
			*/
		def readToken(stream: Stream[Input]): (Token, Stream[Input]) = {
			require(stream.nonEmpty)

			val (currentChar, currentPos) #:: rest = stream

			// Use with care!
			def nextChar = rest.head._1

			// Returns input token with correct position and uses up one character of the stream
			def useOne(t: Token) = (t.setPos(currentPos), rest)
			// Returns input token with correct position and uses up two characters of the stream
			def useTwo(t: Token) = (t.setPos(currentPos), rest.tail)

			def useN(t: Token, n: Int) = (t.setPos(currentPos), rest.drop(n-1))

			currentChar match {
				case `EndOfFile` => useOne(EOF())

				// Reserved word or Identifier
				case _ if Character.isLetter(currentChar) =>
					val (wordLetters, afterWord) = stream.span { case (ch, _) =>
						Character.isLetterOrDigit(ch) || ch == '_'
					}
					val word = wordLetters.map(_._1).mkString
					keywords(word) match {
						case Some(x) => useN(x, word.length)
						case None => useN(ID(word), word.length)
					}

				// Int literal
				case _ if Character.isDigit(currentChar) => {
					// Make sure you fail for integers that do not fit 32 bits.
					val (wordLetters, afterWord) = stream.span { case (ch, _) =>
					Character.isDigit(ch)
					}
					val word = wordLetters.map(_._1).mkString
					Try(word.toInt).toOption match {
						case Some(x:Int) => useN(INTLIT(x), word.length)
						case None => {
							ctx.reporter.error(s"""Cannot convert to 32-bit integer literal.""", currentPos)
							useN(BAD(), word.length)}
					}
				}
				
				// String literal
				case '"' => {
					val (wordLetters, afterWord) = stream.tail.span { case (ch, _) =>
							ch != '"' && ch != EndOfLine
					}
					val word = wordLetters.map(_._1).mkString
					Try(afterWord.head._1).toOption match {
						case None => {
							ctx.reporter.error("String not closed at end of file.", currentPos)
							useN(BAD(), word.length+1)
						}
						case Some('"') => {
							useN(STRINGLIT(word), word.length+2)
						}
						case Some(_) => {
							ctx.reporter.error("String not closed at end of line.", currentPos)
							useN(BAD(), word.length+1)
						}

					} 
				}

					

				case '{' =>
					useOne(LBRACE())
				case '}' =>
					useOne(RBRACE())
				case '(' =>
					useOne(LPAREN())
				case ')' =>
					useOne(RPAREN())
				case ',' =>
					useOne(COMMA())
				case ':' =>
					useOne(COLON())
				case '.' =>
					useOne(DOT())
				case ';' =>
					useOne(SEMICOLON())
				case '_' =>
					useOne(UNDERSCORE())
				case '-' =>
					useOne(MINUS())
				case '*' =>
					useOne(TIMES())
				case '/' =>
					useOne(DIV())
				case '%' =>
					useOne(MOD())
				case '!' =>
					useOne(BANG())
				case '&' => rest.head._1 match {
					case '&' => useTwo(AND())
					case _ => {
						ctx.reporter.error("single '&'", currentPos)
						useOne(BAD())
					}
				}
				case '|' => rest.head._1 match {
					case '|' => useTwo(OR())
					case _ => {
						ctx.reporter.error("single '|'", currentPos)
						useOne(BAD())
					}
				}				case '+' => rest.head._1 match {
					case '+' => useTwo(CONCAT())
					case _ => useOne(PLUS())
				}
				case '=' => rest.head._1 match {
					case '=' => useTwo(EQUALS())
					case '>' => useTwo(RARROW())
					case _ => useOne(EQSIGN())
				}
				case '<' => rest.head._1 match {
						case '=' => useTwo(LESSEQUALS())
						case _ => useOne(LESSTHAN())
				}


				case x => {
					ctx.reporter.error(s"""Unknown character \"$x\".""", currentPos)
					useOne(BAD())
				}
			}
		}

		// To lex a file, call nextToken() until it returns the empty Stream as "rest"
		def tokenStream(s: Stream[Input]): Stream[Token] = {
			if (s.isEmpty) Stream()
			else {
				val (token, rest) = nextToken(s)
				token #:: tokenStream(rest)
			}
		}

		tokenStream(inputStream)
	}

	// Lexing all input files means putting the tokens from each file one after the other
	def run(ctx: Context)(files: List[File]): Stream[Token] = {
		files.toStream flatMap lexFile(ctx)
	}
}

/** Extracts all tokens from input and displays them */
object DisplayTokens extends Pipeline[Stream[Token], Unit] {
	def run(ctx: Context)(tokens: Stream[Token]): Unit = {
		tokens.toList foreach { t => println(s"$t(${t.position.withoutFile})") }
	}
}
