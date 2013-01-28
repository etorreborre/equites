// Equites, a simple chess interface
// Copyright © 2011-2013 Frank S. Thomas <f.thomas@gmx.de>
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

import scala.util.Random
import scalaz._

import util.Math._
import util.Notation._

trait SquareInstances {
  implicit object squareInstance extends Equal[Square] {
    // Equal
    def equal(s1: Square, s2: Square): Boolean = s1 == s2
  }
}

object Square {
  def apply(algebraicFile: Char, algebraicRank: Int): Square =
    Square(algebraicFileRange.indexOf(algebraicFile),
           algebraicRankRange.indexOf(algebraicRank))

  def validCoordinates(file: Int, rank: Int): Boolean = {
    Rules.fileRange.contains(file) &&
    Rules.rankRange.contains(rank)
  }

  def validSum(that: Square, vec: Vec): Boolean =
    validCoordinates(that.file + vec.file, that.rank + vec.rank)

  def l1Dist(p: Square, q: Square): Int = p.l1Dist(q)
  def lInfDist(p: Square, q: Square): Int = p.lInfDist(q)

  // impure
  def random(): Square =
    Square(Rules.fileRange.start + Random.nextInt(Rules.fileRange.length),
           Rules.rankRange.start + Random.nextInt(Rules.rankRange.length))
}

case class Square(file: Int, rank: Int) {
  def +(vec: Vec): Square = Square(file + vec.file, rank + vec.rank)
  def -(vec: Vec): Square = Square(file - vec.file, rank - vec.rank)

  def +(that: Square): Vec = Vec(file + that.file, rank + that.rank)
  def -(that: Square): Vec = Vec(file - that.file, rank - that.rank)

  def isValid: Boolean = Square.validCoordinates(file, rank)

  def isLight: Boolean = isOdd(sum)
  def isDark: Boolean = isEven(sum)

  def l1Dist(that: Square): Int = (this - that).l1Length
  def lInfDist(that: Square): Int = (this - that).lInfLength

  def up: Square   = this + Vec(0, 1)
  def down: Square = this - Vec(0, 1)

  def right: Square = this + Vec(1, 0)
  def left: Square  = this - Vec(1, 0)

  def rightmost: Square = Square(Rules.fileRange.end, rank)
  def leftmost: Square = Square(Rules.fileRange.start, rank)

  private def sum = file + rank
}