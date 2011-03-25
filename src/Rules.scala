// Equites, a simple chess interface
// Copyright © 2011 Frank S. Thomas <f.thomas@gmx.de>
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

package equites

object Rules {
  val fileRange = 0 to 7
  val rankRange = 0 to 7
  val maxLength = math.max(fileRange.length, rankRange.length) - 1

  val kingFile  = 4
  val queenFile = 3
  val rookFiles   = List(0, 7)
  val knightFiles = List(1, 6)
  val bishopFiles = List(2, 5)

  val movementType: Map[PieceType, Pair[Directions, Int]] = {
    import Directions._
    Map(King   -> (anywhere,   1),
        Queen  -> (anywhere,   maxLength),
        Rook   -> (orthogonal, maxLength),
        Bishop -> (diagonal,   maxLength),
        Knight -> (knightLike, 1))
  }

  def backRankBy(color: Color): Int = {
    if (color == White) rankRange.start else rankRange.end
  }

  def pawnRankBy(color: Color): Int = {
    if (color == White) rankRange.start + 1 else rankRange.end - 1
  }

  def castlingFields(piece: Piece, side: Side): Pair[Field, Field] = {
    val rank = backRankBy(piece.color)
    val rookFile = if (side == Kingside) rookFiles(1) else rookFiles(0)
    val shift: (Int, Int) => Int = {
      (file, offset) => file + (if (side == Kingside) offset else -offset)
    }

    piece.pieceType match {
      case King => (Field(kingFile, rank), Field(shift(kingFile, 2), rank))
      case Rook => (Field(rookFile, rank), Field(shift(kingFile, 1), rank))
      case _ => throw new IllegalArgumentException
    }
  }

  def startingPositions(color: Color): Map[Field, Piece] = {
    val backRank = backRankBy(color)
    val pawnRank = pawnRankBy(color)

    val royals  = List(Field(kingFile,  backRank) -> new King(color),
                       Field(queenFile, backRank) -> new Queen(color))
    val rooks   =   rookFiles.map(Field(_, backRank) -> new Rook(color))
    val knights = knightFiles.map(Field(_, backRank) -> new Knight(color))
    val bishops = bishopFiles.map(Field(_, backRank) -> new Bishop(color))
    val pawns   =   fileRange.map(Field(_, pawnRank) -> new Pawn(color))

    Map[Field, Piece]() ++ royals ++ rooks ++ knights ++ bishops ++ pawns
  }

  def startingPositions: Map[Field, Piece] = {
    startingPositions(White) ++ startingPositions(Black)
  }

  def fieldsInDirection(from: Field, direction: Vector,
    maxDist: Int = maxLength): Stream[Field] = {

    if (maxDist < 1 || !Field.validSum(from, direction))
      Stream.empty
    else {
      val next: Field = from + direction
      Stream.cons(next, fieldsInDirection(next, direction, maxDist - 1))
    }
  }
}

class Rules(board: Board) {
  def takeUntilOccupied(fields: Stream[Field], color: Color): List[Field] = {
    val occupied = fields.findIndexOf(board.occupied)
    if (occupied == -1)
      fields.toList
    else {
      val offset = if (board.opponentAt(fields(occupied), color)) 1 else 0
      fields.take(occupied + offset).toList
    }
  }
}
