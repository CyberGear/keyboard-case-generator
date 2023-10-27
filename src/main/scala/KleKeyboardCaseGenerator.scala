import KeyShapes._
import constants._
import custom.{AdvCube, McuProC}
import model.inputmodel._
import model.outputmodel._
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
//          Part("top-plate-with-brim", generateTopPlateWithBrim(keyboard, block)),
//          Part("topless-thick-plate", generateToplessThickPlate(keyboard, block)),
          Part("minimal-case-top", generateMinimalCaseTop(keyboard, block)),
          Part("minimal-case-bottom", generateMinimalCaseBottom(keyboard, block)),
//          Part("switchLayout", switchLayout(keyboard, block))
        ),
      )
    ),
  )

  private def generateTopPlateWithBrim(keyboard: Keyboard, block: KeyboardBlock): Solid = {
    val box      = buildBox(block, 7.5 mm)
    val plate    = buildBox(block, 1.5 mm)
    val keySpace = block.layout.keys.map(_.kPlace(7.5 mm)).combine
    val switches = block.layout.keys.map(_.switch(block.size, 1.5 mm)).combine

    (box - keySpace) + (plate - switches)
  }

  private def generateToplessThickPlate(keyboard: Keyboard, block: KeyboardBlock): Solid = {
    val plate = buildBox(block, 5 mm, 2 mm)
    val switches = block.layout.keys.map(_.switch(block.size, 5 mm)).combine
    val switchSpace = block.layout.keys.map(_.switchClearance(block.size, 3.5 mm)).combine

    plate - switches - switchSpace
  }

  private def generateMinimalCaseTop(keyboard: Keyboard, block: KeyboardBlock): Solid = {
    val plate = buildBox(block, 15 mm, 1.2 mm)
    val space = buildBox(block, 10 mm, -1.8 mm)
    val cover = buildBox(block, 2 mm, 0 mm)
    val switches = block.layout.keys.map(_.switch(block.size, 15 mm)).combine
    val switchSpace = block.layout.keys.map(_.switchClearance(block.size, 13.5 mm)).combine

    val x = block.size.width.pu / 2
    val y = block.size.height.pu -1.81.mm

    (plate - switches - switchSpace - space - cover) - McuProC.cutOut().move(x, y, 1.mm)
  }

  private def generateMinimalCaseBottom(keyboard: Keyboard, block: KeyboardBlock): Solid = {
    val cover = buildBox(block, 2 mm, 0 mm)

    val x = block.size.width.pu / 2
    val y = block.size.height.pu - 1.80.mm

    cover + McuProC.addOn().move(x, y, 1.mm) - McuProC.cutOut().move(x, y, 1.mm)
  }

  private def switchLayout(keyboard: Keyboard, block: KeyboardBlock): Solid = {
    val switches = block.layout.keys.map(_.switch(block.size, 5 mm)).combine
    val switchSpace = block.layout.keys.map(_.switchClearance(block.size, 3.5 mm)).combine

    switches + switchSpace
  }

//  private def generateTestSolid(keyboard: Keyboard, block: KeyboardBlock): Solid = {
//    val box      = buildBox(block)
//    val keySpace = block.layout.keys.map(_.kPlace).combine
//    val caps     = block.layout.keys.map(_.cap).combine
//    val switches = block.layout.keys.map(_.switch(block.size)).combine
//
//    val axis = (Cube(30 mm, 1 mm, 1 mm) + Cube(1 mm, 20 mm, 1 mm) + Cube(1 mm, 1 mm, 10 mm)).moveZ(-5 mm)
////    box - caps + switches
//    keySpace - caps + switches
//  }

  private def buildBox(block: KeyboardBlock, thickness: Length, brim: Length = CaseBrim): Solid = {
    val topEdge    = block.top match {
      case EdgeType.Free => block.layout.keys.map(k => k.y.pu + k.h.pu + brim).max
    }
    val bottomEdge = block.bottom match {
      case EdgeType.Free => block.layout.keys.map(_.y.pu - brim).min
    }
    val leftEdge   = block.left match {
      case EdgeType.Free => block.layout.keys.map(_.x.pu - brim).min
    }
    val rightEdge  = block.right match {
      case EdgeType.Free => block.layout.keys.map(k => k.x.pu + k.w.pu + brim).max
    }
    AdvCube(rightEdge - leftEdge, topEdge - bottomEdge, thickness, xyr = Some(1.5 mm)).moveXY(-brim, -brim)
  }

}
