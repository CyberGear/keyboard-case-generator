import constants.{CapInset, CaseBrim, KeyboardConversions, StabsGap}
import model.inputmodel.{Key, Size}
import scadla.InlineOps.{AngleConversions, _}
import scadla.{Cube, Solid, Translate}
import squants.space.LengthConversions.LengthConversions
import squants.space._

import scala.language.postfixOps

object KeyShapes {

  implicit class KeyImplicits(key: Key) {
    def box: Solid = Cube(key.w.pu + CaseBrim * 2, key.h.pu + CaseBrim * 2, 2 mm)
      .moveXY(-CaseBrim, -CaseBrim)
      .moveXY(key.x pu, key.y pu)

    def cap: Solid = Cube(key.w.pu - CapInset * 2, key.h.pu - CapInset * 2, 2 mm)
      .moveXY(CapInset, CapInset)
      .moveXY(key.x pu, key.y pu)

    def kPlace(thickness: Length): Solid = Cube(key.w pu, key.h pu, thickness).moveXY(key.x pu, key.y pu)

    def switch(size: Size, thickness: Length): Solid = {
      val switchMount = key.center(Cube(1 su, 1 su, thickness))
      key
        .stabilizer(thickness: Length)
        .map(key.rotate(_, stabilizerAngle(size)))
        .map(_ + switchMount)
        .getOrElse(switchMount)
        .moveXY(key.x pu, key.y pu)
    }

    def switchClearance(size: Size, thickness: Length): Solid = {
      val switchClearance = key.center(Cube(1.su + 1.4.mm, 1.su + 1.4.mm, thickness))
      key.stabilizerClearance(thickness)
        .map(key.rotate(_, stabilizerAngle(size)))
        .map(_ + switchClearance)
        .getOrElse(switchClearance)
        .moveXY(key.x pu, key.y pu)
    }

    def center(cube: Cube): Solid = cube.moveXY((key.w.pu - cube.width) / 2, (key.h.pu - cube.depth) / 2)

    def centerTwoFromCenters(cube: Cube, distance: Length): Solid =
      (cube + cube.moveX(distance)).moveXY(
        (key.w.pu - (cube.width + distance)) / 2,
        (key.h.pu - cube.depth) / 2,
      )

    def centerTwoFromInnerEdges(cube: Cube, distance: Length): Solid =
      (cube + cube.moveX(distance + cube.width)).moveXY(
        (key.w.pu - (cube.width * 2 + distance)) / 2,
        (key.h.pu - cube.depth) / 2,
      )

    def rotate(solid: Solid, angle: Angle): Solid =
      solid.moveXY((key.w / -2) pu, (key.h / -2) pu).rotateZ(angle).moveXY(key.w / 2 pu, key.h / 2 pu)

    def stabilizerAngle(size: Size): Angle =
      if (key.w > key.h) if (key.y < size.height / 2) 0 ° else 180 °
      else if (key.x < size.width / 2) -90 °
      else 90 °

    def stabilizer(thickness: Length): Option[Solid] =
      StabsGap
        .get(key.w.orBigger(key.h))
        .map(stabDistance =>
          List(
            key.centerTwoFromInnerEdges(Cube(4.4 mm, 2.8 mm, thickness), stabDistance).moveY(0.9 mm),
            key.centerTwoFromCenters(Cube(3.4 mm, 13.5 mm, thickness), stabDistance).moveY(-1.22 mm),
            key.centerTwoFromCenters(Cube(7.1 mm, 12.3 mm, thickness), stabDistance).moveY(-0.55 mm),
            if (key.w >= BigDecimal(3)) key.center(Cube(stabDistance, 4.6 mm, thickness))
            else key.center(Cube(18 mm, 10.7 mm, thickness)).moveY(-0.55 mm),
          ).combine
        )

    def stabilizerClearance(thickness: Length): Option[Solid] =
      StabsGap
        .get(key.w.orBigger(key.h))
        .map(stabDistance =>
          List(
            key.centerTwoFromCenters(Cube(8.8 mm, 16 mm, thickness), stabDistance).moveY(-1.85 mm),
            if (key.w >= BigDecimal(3)) key.center(Cube(stabDistance, 12.1 mm, thickness)).moveY(-3.8 mm)
            else key.center(Cube(18 mm, 14 mm, thickness)).moveY(-2.85 mm),
          ).combine
        )
  }

  implicit class SolidsImplicits(solids: List[Solid]) {
    def combine: Solid = solids match {
      case Nil                  => throw new IllegalStateException("Can't combine an empty List")
      case head :: Nil          => head
      case a :: b :: Nil        => a + b
      case head :: neck :: tail => tail.foldLeft(head + neck)(_ + _)
    }
  }

  implicit class OptionSolidImplicits(a: Option[Solid]) {
    def +(b: Option[Solid]): Option[Solid] = (a, b) match {
      case (Some(sA), Some(sB))    => Some(sA + sB)
      case (solid: Some[Solid], _) => solid
      case (_, solid: Some[Solid]) => solid
      case _                       => None
    }
  }

  implicit class SolidImplicits(solid: Solid) {
    def moveXY(x: Length, y: Length): Translate = solid.moveX(x).moveY(y)
  }

  implicit class LengthImplicits(length: Length) {
    def +(addition: Length): Length = (length.value + addition.value) mm
  }

  implicit class BigDecimalImplicits(a: BigDecimal) {
    def orBigger(b: BigDecimal): BigDecimal = if (a > b) a else b
  }

}
