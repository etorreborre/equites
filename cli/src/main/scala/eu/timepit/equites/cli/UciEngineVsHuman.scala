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
package cli

import scala.concurrent.duration._
import scalaz.concurrent.Task
import scalaz.stream._

import proto.Uci._
import proto.UciParsers._
import proto.UciProcess._
import util.ScalazProcess._

/*
object UciEngineVsHuman { //extends App {
  val game = Subprocess.popen("gnuchess", "-u").flatMap { engine =>
    val readResponses =
      engine.output.pipe(collectResponses)
    val readFirstBestmove =
      readResponses |> collectFirst { case bm: Bestmove => bm }
    def writePositionCommand(history: Seq[GameState]) =
      toRawCommands(Position(history)).through(engine.input)
    val writeGoCommand =
      toRawCommands(Go(Go.Movetime(50.millis))).through(engine.input)
    val prepareGame =
      newGameCommands.through(engine.input) ++ readResponses.find(_ == ReadyOk)
    val quitEngine =
      toRawCommands(Quit).through(engine.input)

    def xxx =
      stdInLines.once.map(s => parseAll(coordinateMove, s))
      .collect { case Success(result, _) => result }

    
    
    def gameLoop(history: Seq[GameState]): Process[Task, Seq[GameState]] = {
      var newHistory = history
      Process(history).toSource
        .observe(stdOutLastBoard)
        .append(xxx.map(cm => (history, cm)).pipe(appendCoordinateMove).map(x => {newHistory = x; x} ))
        .flatMap(h => writePositionCommand(h))
        .append(writeGoCommand.drain)
        .drain
        .append(Process(newHistory).zip(readFirstBestmove) |> appendBestmove)
        .flatMap(gameLoop)
    }

    val initialPosition = Vector(GameState.init)
    (prepareGame ++ gameLoop(initialPosition)).onComplete(quitEngine)
  }
  game.run.run
}
*/ 