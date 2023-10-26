import model.inputmodel.Keyboard
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import parser.YamlMapper

import scala.language.postfixOps

class GeneratorTest extends AnyFlatSpec with Matchers with Utils {

  it should "generate something" taggedAs Wip in {
    val layout = readLayoutYaml(
      """---
        |layout: >
        |  ["-","*","/","Num Lock"],
        |  [{h:2},"+","7\nHome","8\n↑","9\nPgUp"],
        |  [{x:1},"4\n←","5","6\n→"],
        |  [{h:2},"Enter","1\nEnd","2\n↓","3\nPgDn"],
        |  [{x:1},".\nDel",{w:2},"0\nIns"]
        |""".stripMargin
    )

    val keyboard     = YamlMapper.readValue[Keyboard](ClassLoader.getSystemResource("MK-LTK.yml"))
    val testKeyboard = keyboard.copy(blocks = keyboard.blocks.take(1).map(_.copy(layout = layout)))

    val generator = new KleKeyboardCaseGenerator(testKeyboard)

//    Util.storeCase("/home/marius/Documents", generator.generateCase.copy(name = s"kb-1"))

    Util.preview(generator.generateCase.blocks.head.parts.head.solid)
  }

}
