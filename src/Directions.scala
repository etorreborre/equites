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

import scala.collection.Traversable
import scala.collection.immutable.LinearSeq

object Directions {
  def apply(vectors: Vector*): Directions = {
    new Directions(vectors.toList)
  }

  def apply[A <: Traversable[Vector]](vectors: A): Directions = {
    new Directions(vectors.toList)
  }

  val front = Directions(Vector( 0,  1))
  val right = Directions(Vector( 1,  0))
  val back  = Directions(Vector( 0, -1))
  val left  = Directions(Vector(-1,  0))

  val frontRight = Directions(front(0) + right(0))
  val backRight  = Directions(back(0)  + right(0))
  val backLeft   = Directions(back(0)  + left(0))
  val frontLeft  = Directions(front(0) + left(0))

  val diagonalFront = Directions(frontRight ++ frontLeft)
  val diagonalBack  = Directions(backRight ++ backLeft)

  val diagonal   = Directions(diagonalFront ++ diagonalBack)
  val orthogonal = Directions(front ++ right ++ back ++ left)
  val anywhere   = Directions(orthogonal ++ diagonal)

  val knightLike = Directions({
    val steps = List(-2, -1, 1, 2)
    for {
      file <- steps
      rank <- steps
      if file.abs != rank.abs
    } yield Vector(file, rank)
  })
}

class Directions(vectors: List[Vector]) extends LinearSeq[Vector] {
  def inverse: Directions = Directions(vectors.map(_ * -1))

  def inverseIfBlack(color: Color): Directions = {
    if (color == Black) inverse else this
  }

  override def equals(that: Any): Boolean = that match {
    case other: Directions => vectors.filterNot(other.contains).isEmpty
    case _ => false
  }

  // required for LinearSeq[Vector]:
  def apply(idx: Int): Vector = vectors(idx)
  def length: Int = vectors.length

  override def isEmpty: Boolean = vectors.isEmpty
  override def iterator: Iterator[Vector] = vectors.iterator
}