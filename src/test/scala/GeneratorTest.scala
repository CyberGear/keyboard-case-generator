import model.Keyboard
import org.scalatest.flatspec.AnyFlatSpec
import parser.YamlMapper

import java.io.File
import scala.annotation.tailrec

class GeneratorTest extends AnyFlatSpec {

  it should "parse input file" in {

    val keyboardSpec = YamlMapper.readValue[Keyboard](new File("./MK-TKL-split.yml"))

//    println(YamlMapper.writeValueAsString(Edge.values))

    println(keyboardSpec)

  }

}
