// Equites, a Scala chess playground
// Copyright © 2011, 2013 Frank S. Thomas <frank@timepit.eu>
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

sealed trait Action

trait DrawLike {
  def from: Square
  def to: Square

  def direction: Vec = to - from
  def l1Length: Int = direction.l1Length
  def lInfLength: Int = direction.lInfLength
}

sealed trait MoveLike extends Action with DrawLike {
  def piece: AnyPiece
  def placedPiece: Placed[AnyPiece] = Placed(piece, from)
}

sealed trait PromotionLike extends MoveLike {
  require(piece isFriendOf promotedTo)

  def piece: AnyPawn
  def promotedTo: PromotedPiece
}

sealed trait CaptureLike extends MoveLike {
  require(piece isOpponentOf captured)

  def captured: AnyPiece
  def capturedOn: Square = to
  def placedCaptured: Placed[AnyPiece] = Placed(captured, capturedOn)
}

object Move {
  def apply(piece: AnyPiece, fromTo: (Square, Square)): Move =
    Move(piece, fromTo._1, fromTo._2)

  def apply(placed: Placed[AnyPiece], to: Square): Move =
    Move(placed.elem, placed.square, to)
}

case class Move(piece: AnyPiece, from: Square, to: Square)
  extends MoveLike

object Promotion {
  def apply(placed: Placed[AnyPawn], to: Square, promotedTo: PromotedPiece)
      : Promotion =
    Promotion(placed.elem, placed.square, to, promotedTo)
}

case class Promotion(piece: AnyPawn, from: Square, to: Square,
  promotedTo: PromotedPiece)
  extends PromotionLike

object Capture {
  def apply(move: MoveLike, captured: AnyPiece): Capture =
    Capture(move.piece, move.from, move.to, captured)

  def apply(placed: Placed[AnyPiece], to: Square, captured: AnyPiece): Capture =
    Capture(placed.elem, placed.square, to, captured)
}

case class Capture(piece: AnyPiece, from: Square, to: Square, captured: AnyPiece)
  extends CaptureLike

object CaptureAndPromotion {
  def apply(promo: PromotionLike, captured: AnyPiece): CaptureAndPromotion =
    CaptureAndPromotion(promo.piece, promo.from, promo.to, captured,
                        promo.promotedTo)

  def apply(placed: Placed[AnyPawn], to: Square, captured: AnyPiece,
            promotedTo: PromotedPiece): CaptureAndPromotion =
    CaptureAndPromotion(placed.elem, placed.square, to, captured, promotedTo)
}

case class CaptureAndPromotion(piece: AnyPawn, from: Square, to: Square,
  captured: AnyPiece, promotedTo: PromotedPiece)
  extends CaptureLike with PromotionLike

object EnPassant {
  def apply(placed: Placed[AnyPawn], to: Square, captured: AnyPawn,
            capturedOn: Square): EnPassant =
    EnPassant(placed.elem, placed.square, to, captured, capturedOn)
}

case class EnPassant(piece: AnyPawn, from: Square, to: Square, captured: AnyPawn,
  override val capturedOn: Square)
  extends CaptureLike

object Side {
  def all: List[Side] = List(Kingside, Queenside)
}

sealed trait Side
case object Kingside extends Side
case object Queenside extends Side

sealed trait Castling extends Action {
  def color: Color
  def side: Side

  def king: AnyKing = Piece(color, King)
  def rook: AnyRook = Piece(color, Rook)

  def kingMove: Move = moveOf(king)
  def rookMove: Move = moveOf(rook)

  private def moveOf(piece: CastlingPiece): Move =
    Move(piece, Rules.castlingSquares(side -> piece))
}

case class CastlingShort(color: Color) extends Castling {
  def side: Side = Kingside
}

case class CastlingLong(color: Color) extends Castling {
  def side: Side = Queenside
}
