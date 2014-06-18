// Equites, a Scala chess playground
// Copyright © 2013-2014 Frank S. Thomas <frank@timepit.eu>
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

package eu.timepit.equites
package proto

import scala.util.parsing.combinator._
import scalaz.Apply
import scalaz.std.option._

import proto.Uci._
import util.CoordinateAction
import util.PieceUtil._
import util.SquareUtil._

/**
 * Parsers for the Universal Chess Interface (UCI).
 *
 * @see [[http://www.shredderchess.com/chess-info/features/uci-universal-chess-interface.html UCI Protocol]]
 */
object UciParsers extends RegexParsers {
  def symbol: Parser[String] = """\p{Alnum}+""".r

  def string: Parser[String] = """.*""".r

  def boolean: Parser[Boolean] = "true" ^^^ true | "false" ^^^ false

  def int: Parser[Int] = """-?\d+""".r ^^ (_.toInt)

  def algebraicFile: Parser[Char] = oneOf(algebraicFileRange) ^^ (_.charAt(0))

  def algebraicRank: Parser[Int] = oneOf(algebraicRankRange) ^^ (_.toInt)

  def square: Parser[Square] = algebraicFile ~ algebraicRank ^^ {
    case file ~ rank => unsafeFromAlgebraic(file, rank)
  }

  def draw: Parser[Draw] =
    square ~ square ^^ { case src ~ dest => src to dest }

  def promotedPieceType: Parser[PromotedPieceType] =
    PieceType.allPromoted.map {
      pt => showLowerCaseLetter(pt) ^^^ pt
    }.reduce(_ | _)

  def coordinateAction: Parser[CoordinateAction] =
    draw ~ promotedPieceType.? ^^ {
      case parsedDraw ~ pieceTypeOpt =>
        val colorOpt = Color.guessFrom(parsedDraw.direction)
        val pieceOpt = Apply[Option].apply2(colorOpt, pieceTypeOpt)(Piece.apply)
        CoordinateAction(parsedDraw, pieceOpt)
    }

  def id: Parser[Id] = "id" ~> symbol ~ string ^^ {
    case key ~ value => Id(key, value)
  }

  def uciok: Parser[UciOk.type] = "uciok" ^^^ UciOk

  def readyok: Parser[ReadyOk.type] = "readyok" ^^^ ReadyOk

  def bestmove: Parser[Bestmove] =
    "bestmove" ~> coordinateAction ~ ("ponder" ~> coordinateAction).? ^^ {
      case move ~ ponder => Bestmove(move, ponder)
    }

  def option: Parser[UciOption] =
    "option" ~> "name" ~> optionName ~ optionType ^^ {
      case name ~ optType => UciOption(name, optType)
    }

  def optionName: Parser[String] = """.*(?=\s+type)""".r

  def optionType: Parser[UciOption.Type] = "type" ~>
    (optionTypeCheck
      | optionTypeSpin
      | optionTypeCombo
      | optionTypeButton
      | optionTypeString)

  def optionTypeCheck: Parser[UciOption.Check] =
    "check" ~> "default" ~> boolean ^^ UciOption.Check

  def optionTypeSpin: Parser[UciOption.Spin] =
    "spin" ~> "default" ~> int ~ ("min" ~> int) ~ ("max" ~> int) ^^ {
      case default ~ min ~ max => UciOption.Spin(default, min, max)
    }

  def optionTypeCombo: Parser[UciOption.Combo] =
    "combo" ~> "default" ~> symbol ~ ("var" ~> symbol).* ^^ {
      case default ~ values => UciOption.Combo(default, values)
    }

  def optionTypeButton: Parser[UciOption.Button.type] =
    "button" ^^^ UciOption.Button

  def optionTypeString: Parser[UciOption.StringType] =
    "string" ~> "default" ~> string ^^ UciOption.StringType

  def response: Parser[Response] = id | uciok | readyok | bestmove | option

  private def oneOf[A](as: Seq[A]): Parser[String] = as.mkString("|").r
}
