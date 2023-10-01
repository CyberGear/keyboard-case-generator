import model.inputmodel.Keyboard
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import parser.YamlMapper

class GeneratorTest extends AnyFlatSpec with Matchers {

  it should "generate something" in {
    val keyboard = YamlMapper.readValue[Keyboard](ClassLoader.getSystemResource("MK-TKL-split.yml"))
    val generator = new KleKeyboardCaseGenerator(keyboard)

    Util.preview(generator.generateCase.blocks.head.parts.head.solid)
  }

}
