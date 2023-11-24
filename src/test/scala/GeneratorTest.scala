import constants.KeyboardConversions
import geometry.{AdvCube, DiamondPyramid, SquarePyramid}
import model.inputmodel.Keyboard
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import parser.YamlMapper
import scadla.InlineOps.Ops
import scadla.Solid
import squants.space.LengthConversions.LengthConversions

import scala.language.postfixOps

class GeneratorTest extends AnyFlatSpec with Matchers with Utils {

  it should "generate something" taggedAs Wip in {
    val layout = readLayoutYaml(
      """---
        |layout: >
        |  ["",""],
        |  ["",""],
        |  ["",""]
        |""".stripMargin
    )

    val keyboard     = YamlMapper.readValue[Keyboard](ClassLoader.getSystemResource("MK-LTK-minimal-yaml.yml"))
    val testKeyboard = keyboard.copy(blocks = keyboard.blocks.take(1).map(_.copy(layout = layout)))

    val generator = new KleKeyboardCaseGenerator(testKeyboard)

    Util.storeCase("/home/marius/Documents", generator.generateCase.copy(version = version))

    val solids: List[Solid] = generator.generateCase.blocks.head.parts.map(_.solid)
    Util.preview(solids.reduce(_ + _))
  }

  it should "have advanced cube" taggedAs Wip in {

    val w = 10.mm
    val d = 10.mm
    val h = 5.mm
    val c = 1.mm

//    Util.preview(
//      Hull(
//        Hull(
//          SquarePyramid(c * 2, c * 2, -c).moveZ(c),
//          SquarePyramid(c * 2, c * 2, -c).moveZ(c).moveX(w - c * 2),
//          SquarePyramid(c * 2, c * 2, -c).moveZ(c).moveY(d - c * 2),
//          SquarePyramid(c * 2, c * 2, -c).moveZ(c).moveX(w - c * 2).moveY(d - c * 2)
//        ), Hull (
//          SquarePyramid(c * 2, c * 2, c).moveZ(h - c),
//          SquarePyramid(c * 2, c * 2, c).moveZ(h - c).moveX(w - c * 2),
//          SquarePyramid(c * 2, c * 2, c).moveZ(h - c).moveY(d - c * 2),
//          SquarePyramid(c * 2, c * 2, c).moveZ(h - c).moveX(w - c * 2).moveY(d - c * 2)
//        )
//      )
//    )

//    Util.preview(
//      Hull(
//        Hull(
//          DiamondPyramid(c * 2, c * 2, -c).moveZ(c),
//          DiamondPyramid(c * 2, c * 2, -c).moveZ(c).moveX(w - c * 2),
//          DiamondPyramid(c * 2, c * 2, -c).moveZ(c).moveY(d - c * 2),
//          DiamondPyramid(c * 2, c * 2, -c).moveZ(c).moveX(w - c * 2).moveY(d - c * 2)
//        ), Hull(
//          DiamondPyramid(c * 2, c * 2, c).moveZ(h - c),
//          DiamondPyramid(c * 2, c * 2, c).moveZ(h - c).moveX(w - c * 2),
//          DiamondPyramid(c * 2, c * 2, c).moveZ(h - c).moveY(d - c * 2),
//          DiamondPyramid(c * 2, c * 2, c).moveZ(h - c).moveX(w - c * 2).moveY(d - c * 2)
//        )
//      )
//    )

    val o = 0.mm
    Util.preview(
      AdvCube(1.mm, 10.mm, 5.mm).moveX(-1.mm) +
      AdvCube(10.mm, 1.mm, 5.mm).moveY(-1.mm) +
      AdvCube.topChamferXyR(10.mm, 10.mm, 5.mm, 2.mm, 1.mm)
    )

  }

}
