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
package problem

object NQueens {
  def allBoards: Stream[Board] = {
    def nextBoards(cand: (Board, Set[Square])): Stream[(Board, Set[Square])] = {
      val (board, available) = cand
      available.toStream.map { sq =>
        val placed = Placed(Queen(White), sq)
        (board + placed, available -- Rules.possibleSquares(placed) - sq)
      }
    }

    val first = (Board.empty, Rules.allSquaresSet)
    val n = math.sqrt(Rules.allSquaresSeq.length).toInt

    util.backtracking(first)(nextBoards, cand => cand._1.size >= n).map(_._1)
  }
}