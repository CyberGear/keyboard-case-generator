import model.inputmodel.Layout
import model.inputmodel.Keyboard
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import parser.YamlMapper

class GeneratorTest extends AnyFlatSpec with Matchers with Utils {

  it should "generate something" in {
    val layout = YamlMapper.readValue[Layout](
      asYamlString(
        """[{a:7},"",{w:2},"",{h:2},""],
          |[{h:2},"",{w:2,h:2},""],
          |[{x:3},""],
          |[{w:2},"","",""]
          |""".stripMargin
      )
    )

    val keyboard     = YamlMapper.readValue[Keyboard](ClassLoader.getSystemResource("MK-TKL-split.yml"))
    val testKeyboard = keyboard.copy(blocks = keyboard.blocks.take(1).map(_.copy(layout = layout)))

    val generator = new KleKeyboardCaseGenerator(testKeyboard)
    Util.preview(generator.generateCase.blocks.head.parts.head.solid)
  }

}
