import constants.KeyboardConversions
import model.inputmodel.{Keyboard, KeyboardBlock}
import model.outputmodel.{Block, Case, Part}
import scadla.InlineOps._
import scadla.{Solid, _}
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
    val casePart = switches.map(key =>
      Cube(key.size.width * 1 cu, key.size.height * 1 cu, 2 mm)
        .moveX(key.position.x * 1 pu)
        .moveY(key.position.y * 1 pu)
    ).combine
    val topSwitches = switches.map(key =>
      Cube(key.size.width * 1 pu, key.size.height * 1 pu, 2 mm)
        .moveX(key.position.x * 1 pu)
        .moveY(key.position.y * 1 pu)
    ).combine

    casePart
  }


  implicit class SolidImplicits(solids: List[Solid]) {
    def combine: Solid = solids match {
      case head :: Nil => head
      case a :: b :: Nil => a + b
      case head :: neck :: tail => tail.foldLeft(head + neck)((sum, part)  =>  sum + part)
    }
  }

}
