// Equites, a simple chess interface
// Copyright © 2011, 2013 Frank S. Thomas <f.thomas@gmx.de>
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
package utils

import scala.collection.immutable.NumericRange

object Notation {
  val algebraicFileRange: NumericRange[Char] =
    (Rules.fileRange.start + 'a').toChar to
    (Rules.fileRange.end   + 'a').toChar

  val algebraicRankRange: Range =
    (Rules.rankRange.start + 1) to
    (Rules.rankRange.end   + 1)

  val numericFileRange: Range =
    (Rules.fileRange.start + 1) to
    (Rules.fileRange.end   + 1)

  def numericRankRange: Range = algebraicRankRange
}