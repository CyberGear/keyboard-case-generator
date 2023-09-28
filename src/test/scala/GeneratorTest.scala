import org.scalatest.flatspec.AnyFlatSpec

import java.io.File
import scala.annotation.tailrec

class GeneratorTest extends AnyFlatSpec {

  it should "parse input file" in {

    val keyboardSpec = YamlMapper.readValue[Keyboard](new File("./MK-TKL-split.yml"))

//    println(YamlMapper.writeValueAsString(Edge.values))

    println(keyboardSpec)

  }

  it should "parse KLE rows" in {
    val keyboardSpec = YamlMapper.readValue[Keyboard](new File("./MK-TKL-split.yml"))
    val layout       = keyboardSpec.blocks.last.layout

    val Row                                                            = """(?s).*?(\[(.+?)]).*""".r
    @tailrec
    def readRows(input: String, out: List[String] = Nil): List[String] =
      input match {
        case Row(row, group) => readRows(input.replace(row, ""), out :+ group)
        case _               => out
      }
    val rows                                                           = readRows(layout)

    println(">>>>")
    rows.map(l => s">> $l").foreach(println)
    println("<<<<")

    val Row = """(?s).*?(\{.*?},|".*?",).*""".r

//    def readArgs(row: String, keys: List[Key] = Nil)

  }

}

case class Key(
    position: Position,
    size: Size = Size(1, 1),
    stepped: Boolean = false,
    decal: Boolean = false
)

object Key {
  def next(previous: Key, size: Size): Key =
    Key(
      Position(
        previous.position.x + size.width,
        previous.position.y
      ),
      size
    )
}
