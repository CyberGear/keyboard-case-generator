import constants.KeyboardConversions
import model.inputmodel.{Keyboard, KeyboardBlock}
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
    val switches = block.layout.rows.flatMap(_.keys)

    val casePart    =
      switches.map(key => CenterU.cube(key.w * 1 cu, key.h * 1 cu, 2 mm).moveXY(key.x * 1 pu, key.y * 1 pu)).combine
    val topSwitches =
      switches.map(key => CenterU.cube(key.w * 1 pu, key.h * 1 pu, 2 mm).moveXY(key.x * 1 pu, key.y * 1 pu)).combine

    casePart - topSwitches
  }

  implicit class SolidsImplicits(solids: List[Solid]) {
    def combine: Solid = solids match {
      case Nil                  => throw new IllegalStateException("Can't combine an empty List")
      case head :: Nil          => head
      case a :: b :: Nil        => a + b
      case head :: neck :: tail => tail.foldLeft(head + neck)((sum, part) => sum + part)
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
