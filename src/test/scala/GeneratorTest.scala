import custom.McuProC
import model.inputmodel.Keyboard
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import parser.YamlMapper
import scadla.InlineOps.Ops
import scadla.Solid

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

    val keyboard     = YamlMapper.readValue[Keyboard](ClassLoader.getSystemResource("MK-LTK.yml"))
    val testKeyboard = keyboard.copy(blocks = keyboard.blocks.take(1).map(_.copy(layout = layout)))

    val generator = new KleKeyboardCaseGenerator(testKeyboard)

    Util.storeCase("/home/marius/Documents", generator.generateCase.copy(name = s"kb-3"))

    val solids: List[Solid] = generator.generateCase.blocks.head.parts.map(_.solid)
    Util.preview(solids.reduce(_ + _))
  }

}
