import constants._
import model.inputmodel.{EdgeType, Key, Keyboard, KeyboardBlock}
import model.outputmodel.{Block, Case, Part}
import scadla.InlineOps._
import scadla._
import squants.space.Length
import squants.space.LengthConversions.LengthConversions

import scala.language.postfixOps

class KleKeyboardCaseGenerator(val keyboard: Keyboard) {

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
    def box: Solid    =
      Cube(key.w.pu + CaseBrim * 2, key.h.pu + CaseBrim * 2, 2 mm)
        .moveXY(-CaseBrim, -CaseBrim)
        .moveXY(key.x pu, key.y pu)
    def cap: Solid    =
      Cube(key.w.pu - CapInset * 2, key.h.pu - CapInset * 2, 2 mm)
        .moveXY(CapInset, CapInset)
        .moveXY(key.x pu, key.y pu)
    def kPlace: Solid =
      Cube(key.w pu, key.h pu, 2 mm)
        .moveXY(key.x pu, key.y pu)
    def switch: Solid = {
      val switchMount = Cube(1 su, 1 su, 2 mm)
        .moveXY((key.w.pu - 1.su) / 2, (key.h.pu - 1.su) / 2)
        .moveXY(key.x pu, key.y pu)

      if (key.w >= 2 && key.w < 3)
        switchMount +
          Cube(32.2 mm, 2.8 mm, 2 mm).moveXY((key.w.pu - 32.2.mm) / 2, (key.h.pu - 2.9.mm) / 2 + 0.9.mm).moveXY(key.x pu, key.y pu) +
          (Cube(3 mm, 13.5 mm, 2 mm) + Cube(3 mm, 13.5 mm, 2 mm).moveX(23.8 mm))
            .moveXY((key.w.pu - 26.8.mm) / 2, (key.h.pu - 13.5.mm) / 2 - 1.22.mm).moveXY(key.x pu, key.y pu) +
          (Cube(6.7 mm, 12.3 mm, 2 mm) + Cube(6.7 mm, 12.3 mm, 2 mm).moveX(23.8 mm))
            .moveXY((key.w.pu - 30.5.mm) / 2, (key.h.pu - 12.3.mm) / 2 - 0.55.mm).moveXY(key.x pu, key.y pu) +
          Cube(18 mm, 10.7 mm, 2 mm).moveXY((key.w.pu - 18.mm) / 2, (key.h.pu - 10.7.mm) / 2 - 0.55.mm).moveXY(key.x pu, key.y pu)
      else switchMount
    }
  }

  implicit class SolidsImplicits(solids: List[Solid]) {
    def combine: Solid = solids match {
      case Nil                  => throw new IllegalStateException("Can't combine an empty List")
      case head :: Nil          => head
      case a :: b :: Nil        => a + b
      case head :: neck :: tail => tail.foldLeft(head + neck)(_ + _)
    }
  }

  /**
   *  keycap size | 2, 2.25, 2.75 | 3    | 7     | 8, 9, 10 |
   *         A in | 0.94          | 1.5  | 4.5   | 5.25     |
   *         A cm | 23.876        | 38.1 | 11.43 | 13.335   |
   *
   *  23.8  38.1  69.343  85.725  95  100   104.76  114.3
   *  < 3   3     4.5     5.5     6   6.25  6.5     7
   */

  implicit class SolidImplicits(solid: Solid) {
    def moveXY(x: Length, y: Length): Translate =
      solid.moveX(x).moveY(y)
  }

}
