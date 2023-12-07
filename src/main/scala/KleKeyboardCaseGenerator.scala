import KeyShapes._
import constants._
import geometry.AdvCube
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
          Part("minimal-case-bottom", generateMinimalCaseBottom(keyboard, block))
//          Part("switchLayout", switchLayout(keyboard, block))
        ),
      )
    ),
  )

  private def generateTopPlateWithBrim(keyboard: Keyboard, block: KeyboardBlock): Solid = {
    val box      = outerBox(block, 7.5 mm)
    val plate    = outerBox(block, 1.5 mm)
    val keySpace = block.layout.keys.map(_.kPlace(7.5 mm)).combine
    val switches = block.layout.keys.map(_.switch(block.size, 1.5 mm)).combine

    (box - keySpace) + (plate - switches)
  }

  private def generateToplessThickPlate(keyboard: Keyboard, block: KeyboardBlock): Solid = {
    val plate       = outerBox(block, 5 mm, 2 mm)
    val switches    = block.layout.keys.map(_.switch(block.size, 5 mm)).combine
    val switchSpace = block.layout.keys.map(_.switchClearance(block.size, 3.5 mm)).combine

    plate - switches - switchSpace
  }

  private def generateMinimalCaseTop(keyboard: Keyboard, block: KeyboardBlock): Solid = {
    val plate       = outerBox(block, 15 mm, 1.2 mm, AdvCube.topChamferXyR(_, _, _, _, 0.5.mm))
    val space       = outerBox(block, 10 mm, -1.8 mm)
    val cover       = outerBox(block, 2 mm, 0 mm)
    val switches    = block.layout.keys.map(_.switch(block.size, 15 mm)).combine
    val switchSpace = block.layout.keys.map(_.switchClearance(block.size, 13.5 mm)).combine

    val x = block.mcuGravity match {
      case Gravity.Center => block.size.width.pu / 2
      case Gravity.Left   => block.mcu.width / 2
      case Gravity.Right  => block.size.width.pu - block.mcu.width / 2
    }
    val y = block.size.height.pu - 1.81.mm

    val top = (plate - switches - switchSpace - space - cover) - block.mcu.cutOut().move(x, y, 1.mm) -
      outerBox(block, 3.mm, -1.5 mm, (x, y, z, _) => AdvCube(x, y, z, xyzr = Some(1.5.mm))).moveZ(1.5.mm)

    val screws =
      screwPole.moveXY(1.pu, 1.pu) +
        screwPole.moveXY(block.size.width.pu - 1.pu, 1.pu) +
        screwPole.moveXY(1.pu, block.size.height.pu - 1.pu) +
        screwPole.moveXY(block.size.width.pu - 1.pu, block.size.height.pu - 1.pu)

    top - screws
  }

  def screwPole: Solid = {
    Cylinder(2.mm, 2.mm, 0.5.mm) +
      Cylinder(2.mm, 1.mm, 1.mm).moveZ(0.5.mm) +
      Cylinder(1.mm, 1.mm, 13.8.mm) +
      Cylinder(1.5.mm, 1.5.mm, 8.mm).moveZ(2.mm) +
      Cylinder(2.mm, 2.mm, 1.mm).moveZ(14.mm)
  }

  private def generateMinimalCaseBottom(keyboard: Keyboard, block: KeyboardBlock): Solid = {
    val cover = outerBox(block, 2 mm, 0 mm, AdvCube.bottomChamferXyR(_, _, _, _, 0.5.mm)) +
      outerBox(block, 3.mm, -1.5 mm, (x, y, z, _) => AdvCube(x, y, z, xyzr = Some(1.5.mm))).moveZ(1.5.mm) -
      outerBox(block, 2 mm, 0 mm).moveZ(4.mm) -
      outerBox(block, 2 mm, -2.7 mm, AdvCube.bottomChamferXyR(_, _, _, _, 1.mm, 2.mm)).moveZ(2.mm)

    val x = block.mcuGravity match {
      case Gravity.Center => block.size.width.pu / 2
      case Gravity.Left   => block.mcu.width / 2
      case Gravity.Right  => block.size.width.pu - block.mcu.width / 2
    }
    val y = block.size.height.pu - 1.80.mm

    cover + block.mcu.addOn().move(x, y, 1.mm) - block.mcu.cutOut().move(x, y, 1.mm) -
      (screwPole.moveXY(1.pu, 1.pu) +
        screwPole.moveXY(block.size.width.pu - 1.pu, 1.pu) +
        screwPole.moveXY(1.pu, block.size.height.pu - 1.pu) +
        screwPole.moveXY(block.size.width.pu - 1.pu, block.size.height.pu - 1.pu))
  }

  private def switchLayout(keyboard: Keyboard, block: KeyboardBlock): Solid = {
    val switches    = block.layout.keys.map(_.switch(block.size, 5 mm)).combine
    val switchSpace = block.layout.keys.map(_.switchClearance(block.size, 3.5 mm)).combine

    switches + switchSpace
  }

  private def outerBox(
      block: KeyboardBlock,
      thickness: Length,
      brim: Length = CaseBrim,
      makeSolid: (Length, Length, Length, Length) => Solid = (x, y, z, r) => AdvCube(x, y, z, xyr = Some(r)),
  ): Solid = {
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
    makeSolid(rightEdge - leftEdge, topEdge - bottomEdge, thickness, 1.5.mm).moveXY(-brim, -brim)
  }

}
