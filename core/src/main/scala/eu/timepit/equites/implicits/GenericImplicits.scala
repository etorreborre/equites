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
package implicits

import scala.collection.{IterableLike, TraversableLike}
import scala.math.Ordering
import scalaz._

object GenericImplicits {
  implicit final class CollectionOps[C](val self: C) extends AnyVal {
    def asOption(implicit C: Unapply[IsEmpty, C]): Option[C] =
      if (C.TC.isEmpty(C(self))) None else Some(self)

    def dropLeftRight(n: Int)(implicit ev: C => IterableLike[_, C]): C =
      self.drop(n).dropRight(n)

    def minGroupBy[A, B](f: A => B)(implicit ev: C => TraversableLike[A, C],
                                    cmp: Ordering[B]): C = {
      val grouped = self.groupBy(f)
      if (grouped.nonEmpty) grouped.minBy(_._1)._2 else self.take(0)
    }
  }
}
