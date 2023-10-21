import model.inputmodel.Keyboard
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import parser.YamlMapper

class GeneratorTest extends AnyFlatSpec with Matchers with Utils {

  it should "generate something" taggedAs Wip in {
    val layout = readLayoutYaml(
        """---
          |layout: >
          |  [ "", {x:2}, "" ],
          |  [ {x:1,w:2}, "" ],
          |  [ "", {x:0.5}, "", {x:0.5}, "" ],
          |  [ {x:0.75}, "", {x:0.5}, "" ]
          |""".stripMargin
    )

    val keyboard     = YamlMapper.readValue[Keyboard](ClassLoader.getSystemResource("MK-LTK.yml"))
    val testKeyboard = keyboard.copy(blocks = keyboard.blocks.take(1).map(_.copy(layout = layout)))

    val generator = new KleKeyboardCaseGenerator(testKeyboard)

//    Util.storeCase("/home/marius/Documents", generator.generateCase)

    Util.preview(generator.generateCase.blocks.head.parts.head.solid)
  }

}
