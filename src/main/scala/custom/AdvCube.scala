package custom

import scadla.InlineOps.{AngleConversions, Ops}
import scadla._
import squants.space.Length

import scala.language.postfixOps

object AdvCube {
  def apply(
      x: Length,
      y: Length,
      z: Length,
      xyr: Option[Length] = None,
      xzr: Option[Length] = None,
      yzr: Option[Length] = None,
      xyzr: Option[Length] = None,
  ): Solid =
    if (xyzr.exists(_.value > 0))
      if (xyr.nonEmpty || xzr.nonEmpty || yzr.nonEmpty)
        throw new IllegalAccessException("CustomCube: when xyzr is defined no other **r can be defined")
      else xyzrCube(x, y, z, xyzr.get, xyzr.get * 2)
    else
      List(
        xyr.filter(_.value > 0).map(r => xyrCube(x, y, z, r, r * 2)),
        xzr.filter(_.value > 0).map(r => xzrCube(x, y, z, r, r * 2)),
        yzr.filter(_.value > 0).map(r => yzrCube(x, y, z, r, r * 2)),
      ).flatten.reduceOption(_ * _).getOrElse(Cube(x, y, z))

  private def xyrCube(x: Length, y: Length, z: Length, r: Length, d: Length): Solid =
    if (d > x || d > y) throw new IllegalAccessException("CustomCube: xyr radius is too big")
    else
      Hull(
        Cylinder(r, z),
        Cylinder(r, z).moveY(y - d),
        Cylinder(r, z).moveX(x - d),
        Cylinder(r, z).moveX(x - d).moveY(y - d),
      ).moveX(r).moveY(r)

  private def xzrCube(x: Length, y: Length, z: Length, r: Length, d: Length): Solid =
    if (d > x || d > z) throw new IllegalAccessException("CustomCube: xzr radius is too big")
    else
      Hull(
        Cylinder(r, y).rotateX(-90 °),
        Cylinder(r, y).rotateX(-90 °).moveZ(z - d),
        Cylinder(r, y).rotateX(-90 °).moveX(x - d),
        Cylinder(r, y).rotateX(-90 °).moveX(x - d).moveZ(z - d),
      ).moveX(r).moveZ(r)

  private def yzrCube(x: Length, y: Length, z: Length, r: Length, d: Length): Solid =
    if (d > y || d > z) throw new IllegalAccessException("CustomCube: yzr radius is too big")
    else
      Hull(
        Cylinder(r, x).rotateY(90 °),
        Cylinder(r, x).rotateY(90 °).moveZ(z - d),
        Cylinder(r, x).rotateY(90 °).moveY(y - d),
        Cylinder(r, x).rotateY(90 °).moveY(y - d).moveZ(z - d),
      ).moveZ(r).moveY(r)

  private def xyzrCube(x: Length, y: Length, z: Length, r: Length, d: Length): Solid =
    if (d > x || d > y || d > z) throw new IllegalAccessException("CustomCube: 'xyzr' radius is too big")
    else
      Minkowski(
        Translate(r, r, r, AdvCube(x - d, y - d, z - d)),
        Sphere(r),
      )

}
