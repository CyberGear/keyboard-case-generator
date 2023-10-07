import constants._
import model.inputmodel.{EdgeType, Key, Keyboard, KeyboardBlock}
import model.outputmodel.{Block, Case, Part}
import scadla.InlineOps._
import scadla._
import squants.space.Length
import squants.space.LengthConversions.LengthConversions

import scala.language.postfixOps

class KleKeyboardCaseGenerator(val keyboard: Keyboard) {

  val A: Map[BigDecimal, Length] = Map(
    BigDecimal(2)    -> 23.8.mm,
    BigDecimal(2.25) -> 23.8.mm,
    BigDecimal(2.75) -> 23.8.mm,
    BigDecimal(3)    -> 38.1.mm,
    BigDecimal(4)    -> 57.15.mm,
    BigDecimal(4.5)  -> 69.3.mm,
    BigDecimal(5.5)  -> 85.7.mm,
    BigDecimal(6)    -> 95.mm,
    BigDecimal(6.25) -> 100.mm,
    BigDecimal(6.5)  -> 104.76.mm,
    BigDecimal(7)    -> 114.3.mm,
    BigDecimal(8)    -> 133.35.mm,
    BigDecimal(9)    -> 133.35.mm,
    BigDecimal(10)   -> 133.35.mm,
  )

  def generateCase: Case = Case(
    keyboard.name,
    keyboard.version,
    keyboard.blocks.map(block =>
      Block(
        block.name,
        List(
          Part("test", generateTestSolid(keyboard, block))
        ),
      )
    ),
  )

  private def generateTestSolid(keyboard: Keyboard, block: KeyboardBlock): Solid = {
    val box      = buildBox(block)
    val keySpace = block.layout.keys.map(_.kPlace).combine
    val caps     = block.layout.keys.map(_.cap).combine
    val switches = block.layout.keys.map(_.switch).combine

    val axis = (Cube(30 mm, 1 mm, 1 mm) + Cube(1 mm, 20 mm, 1 mm) + Cube(1 mm, 1 mm, 10 mm)).moveZ(-5 mm)
//    box - caps + switches
    axis + caps - switches
  }

  private def buildBox(block: KeyboardBlock): Solid = {
    val topEdge    = block.top match {
      case EdgeType.Free => block.layout.keys.map(k => k.y.pu + k.h.pu + CaseBrim).max
    }
    val bottomEdge = block.top match {
      case EdgeType.Free => block.layout.keys.map(_.y.pu - CaseBrim).min
    }
    val leftEdge   = block.top match {
      case EdgeType.Free => block.layout.keys.map(_.x.pu - CaseBrim).min
    }
    val rightEdge  = block.top match {
      case EdgeType.Free => block.layout.keys.map(k => k.x.pu + k.w.pu + CaseBrim).max
    }
    Cube(rightEdge - leftEdge, topEdge - bottomEdge, 2 mm).moveXY(-CaseBrim, -CaseBrim)
  }

  implicit class KeyImplicits(key: Key) {
    def box: Solid = Cube(key.w.pu + CaseBrim * 2, key.h.pu + CaseBrim * 2, 2 mm)
      .moveXY(-CaseBrim, -CaseBrim)
      .moveXY(key.x pu, key.y pu)

    def cap: Solid = Cube(key.w.pu - CapInset * 2, key.h.pu - CapInset * 2, 2 mm)
      .moveXY(CapInset, CapInset)
      .moveXY(key.x pu, key.y pu)

    def kPlace: Solid = Cube(key.w pu, key.h pu, 2 mm).moveXY(key.x pu, key.y pu)

    def switch: Solid = {
      val switchMount = key.center(Cube(1 su, 1 su, 2 mm))
      stabilizer(key).map(_ + switchMount).getOrElse(switchMount).moveXY(key.x pu, key.y pu)
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
  }

  private def stabilizer(key: Key): Option[Solid] =
    A.get(key.w)
      .map(stabDistance =>
        List(
          key.centerTwoFromInnerEdges(Cube(4.2 mm, 2.8 mm, 2 mm), stabDistance).moveY(0.9 mm),
          key.centerTwoFromCenters(Cube(3 mm, 13.5 mm, 2 mm), stabDistance).moveY(-1.22 mm),
          key.centerTwoFromCenters(Cube(6.7 mm, 12.3 mm, 2 mm), stabDistance).moveY(-0.55 mm),
          if (key.w >= BigDecimal(3)) key.center(Cube(stabDistance, 4.6 mm, 2 mm))
          else key.center(Cube(18 mm, 10.7 mm, 2 mm)).moveY(-0.55 mm),
        ).combine
      )

  implicit class SolidsImplicits(solids: List[Solid]) {
    def combine: Solid = solids match {
      case Nil                  => throw new IllegalStateException("Can't combine an empty List")
      case head :: Nil          => head
      case a :: b :: Nil        => a + b
      case head :: neck :: tail => tail.foldLeft(head + neck)(_ + _)
    }
  }

  implicit class SolidImplicits(solid: Solid) {
    def moveXY(x: Length, y: Length): Translate = solid.moveX(x).moveY(y)
  }

}
