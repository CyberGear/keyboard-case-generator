import model.inputmodel.Layout
import model.inputmodel.Keyboard
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import parser.YamlMapper

class GeneratorTest extends AnyFlatSpec with Matchers with Utils {

  it should "generate something" in {
    val layout = YamlMapper.readValue[Layout](
      asYamlString(
        """[{x:1,a:7,w:2},""],
          |[{h:2},"",{x:2,h:2},""],
          |[{y:1,x:1,w:2},""]
          |""".stripMargin
      )
    )

    val keyboard     = YamlMapper.readValue[Keyboard](ClassLoader.getSystemResource("MK-LTK.yml"))
    val testKeyboard = keyboard.copy(blocks = keyboard.blocks.take(1).map(_.copy(layout = layout)))

    val generator = new KleKeyboardCaseGenerator(keyboard)
    Util.preview(generator.generateCase.blocks.head.parts.head.solid)
  }

}
