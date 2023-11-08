package geometry

import scadla.InlineOps.{AngleConversions, Ops}
import scadla._
import squants.space.Length
import squants.space.LengthConversions.LengthConversions

import scala.language.postfixOps

object SquarePyramid {

  private val o = 0 mm

  def apply(x: Length, y: Length, z: Length): Solid = {
    val baseA = Point(o, o, o)
    val baseB = Point(x, o, o)
    val baseC = Point(x, y, o)
    val baseD = Point(o, y, o)
    val peakE = Point(x / 2, y / 2, z)
    Polyhedron(List(
      Face(baseA, baseB, baseC), // half base
      Face(baseA, baseC, baseD), // other half base
      Face(baseA, baseD, peakE), // left side
      Face(baseC, baseB, peakE), // right side
      Face(baseB, baseA, peakE), // front side
      Face(baseD, baseC, peakE), // back side
    ))
  }
}
