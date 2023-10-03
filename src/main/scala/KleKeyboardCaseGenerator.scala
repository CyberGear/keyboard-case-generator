import constants._
import model.inputmodel.{Key, Keyboard, KeyboardBlock}
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

    val box    = block.layout.keys.map(_.box).combine
    val keycap = block.layout.keys.map(_.kPlace).combine
    val cap = block.layout.keys.map(_.cap).combine
    val switch = block.layout.keys.map(_.switch).combine

    val axis = (Cube(30 mm, 1 mm, 1 mm) + Cube(1 mm, 20 mm, 1 mm) + Cube(1 mm, 1 mm, 10 mm)).moveZ(-5 mm)
    axis + cap - switch
  }

  implicit class KeyImplicits(key: Key) {
    def box: Solid    =
      Cube(key.w.pu + CaseBrim * 2, key.h.pu + CaseBrim * 2, 2 mm)
        .moveXY(-CaseBrim, -CaseBrim)
        .moveXY(key.x pu, key.y pu)
    def cap: Solid =
      Cube(key.w.pu - CapInset * 2, key.h.pu - CapInset * 2, 2 mm)
        .moveXY(CapInset, CapInset)
        .moveXY(key.x pu, key.y pu)
    def kPlace: Solid =
      Cube(key.w pu, key.h pu, 2 mm)
        .moveXY(key.x pu, key.y pu)
    def switch: Solid =
      Cube(1 su, 1 su, 2 mm)
        .moveXY((key.w.pu - 1.su) / 2, (key.h.pu - 1.su) / 2)
        .moveXY(key.x pu, key.y pu)
  }

  implicit class SolidsImplicits(solids: List[Solid]) {
    def combine: Solid = solids match {
      case Nil                  => throw new IllegalStateException("Can't combine an empty List")
      case head :: Nil          => head
      case a :: b :: Nil        => a + b
      case head :: neck :: tail => tail.foldLeft(head + neck)(_ + _)
    }
  }

  implicit class SolidImplicits(solid: Solid) {
    def moveXY(x: Length, y: Length): Translate =
      solid.moveX(x).moveY(y)
  }

  object CenterU {
    def cube(width: Length, depth: Length, height: Length): Translate =
      Cube(width, depth, height).moveXY(width / -2, depth / -2)
  }

}
