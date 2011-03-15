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

import org.specs2.mutable._

class VectorSpec extends Specification {
  "Vector" should {
    "be correctly added and subtracted" in {
      Vector(1, 1) + Vector( 2,  2) must_== Vector( 3,  3)
      Vector(1, 1) + Vector(-2, -2) must_== Vector(-1, -1)

      Vector(1, 1) - Vector( 2,  2) must_== Vector(-1, -1)
      Vector(1, 1) - Vector(-2, -2) must_== Vector( 3,  3) 
    }
  }
}
