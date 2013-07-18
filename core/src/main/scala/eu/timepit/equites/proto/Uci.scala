// Equites, a Scala chess playground
// Copyright © 2013 Frank S. Thomas <frank@timepit.eu>
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

import implicits.ActionImplicits._
import implicits.GameStateImplicits._

object Uci {
  sealed trait Command extends util.TextCommand
  sealed trait Request extends Command
  sealed trait Response extends Command

  // Requests to the engine:

  case object Uci extends Request

  case class Debug(on: Boolean) extends Request {
    override def cmdArgs: Seq[String] = Seq(util.toStringOnOff(on))
  }

  case object IsReady extends Request

  case class SetOption(name: String, value: Option[String] = None)
    extends Request {

    override def cmdArgs: Seq[String] =
      Seq("name", name) ++ value.map("value " + _)
  }

  // TODO: register

  case object UciNewGame extends Request

  case class Position(history: Seq[GameState]) extends Request {
    override def cmdArgs: Seq[String] =
      if (history.isEmpty) {
        Seq("startpos", "moves")
      } else {
        val moves = history.tail.flatMap(_.lastAction).map(_.toCoordinate)
        Seq(history.head.toFen, "moves") ++ moves
      }
  }

  // TODO: go

  case object Stop extends Request

  case object PonderHit extends Request

  case object Quit extends Request

  // Responses from the engine:

  case class Id(key: String, value: String) extends Response

  case object UciOk extends Response

  case object ReadyOk extends Response

  case class Bestmove(move: util.CoordinateMove,
    ponder: Option[util.CoordinateMove] = None) extends Response {

    override def cmdArgs: Seq[String] =
      Seq(move.toAlgebraic) ++ ponder.map("ponder " + _.toAlgebraic)
  }

  // TODO: copyprotection

  // TODO: registration

  // TODO: info

  // TODO: option
}
