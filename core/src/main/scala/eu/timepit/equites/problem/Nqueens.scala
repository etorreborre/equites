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
package problem

import scalaz.std.stream._

object Nqueens {
  def allBoards: Stream[Board] = {
    case class Candidate(board: Board, available: Set[Square])

    def nextBoards(c: Candidate): Stream[Candidate] = {
      c.available.toStream.map { sq =>
        val placed = Placed(Piece(White, Queen), sq)
        val reachable = Rules.undirectedReachableSquares(placed)
        Candidate(c.board + placed, c.available -- reachable - sq)
      }
    }

    val first = Candidate(Board.empty, Square.allAsSet)
    val n = math.sqrt(Square.allAsSeq.length).toInt
    util.backtrack(first)(nextBoards, _.board.pieceCount >= n).map(_.board)
  }
}
