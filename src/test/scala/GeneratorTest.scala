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
    val layout       = keyboardSpec.blocks.head.layout

    val Row = """(?s).*?(\[(.+?)]).*""".r

    @tailrec
    def readRows(input: String, out: List[String] = Nil): List[String] =
      input match {
        case Row(row, group) => readRows(input.replace(row, ""), out :+ s"${group.trim},")
        case _               => out
      }

    val rows = readRows(layout)

    val Modifiers = """(?s)^(\{(.*?)},".*?",).*""".r
    val Switch    = """(?s)^(".*?",).*""".r

    def parseMods(modsString: String): Map[String, String] =
      modsString.split(",").map(_.split(":")).map(p => p.head.strip() -> p.last.strip()).toMap

    @tailrec
    def readArgs(row: String, keys: List[Key] = Nil): List[Key] = row match {
      case Modifiers(block, mods) =>
        readArgs(
          row.drop(block.length),
          keys :+ Key.next(keys.lastOption, parseMods(mods))
        )
      case Switch(letter)            =>
        readArgs(
          row.drop(letter.length),
          keys :+ Key.next(keys.lastOption)
        )
      case _                      => keys
    }

    println(rows.head)
    readArgs(rows.head).map(a => s"$a").foreach(print)
    println()
    println("<<<<")
  }

}

case class Key(
    position: Position,
    size: Size = Size(1, 1),
    stepped: Boolean = false,
    decal: Boolean = false
) {
  override def toString: String =
    s"{${position.x};${position.y}|${size.width};${size.height}}"
}

object Key {
  def next(previous: Option[Key], args: Map[String, String] = Map.empty): Key = {
    val position = previous.map(_.position).getOrElse(Position(0, 0))
    val size = previous.map(_.size).getOrElse(Size(0, 0))
    Key(
      Position(
        position.x + size.width + BigDecimal(args.getOrElse("x", "0")),
        position.y + size.height + BigDecimal(args.getOrElse("y", "0"))
      ),
      Size(
        args.getOrElse("w", "1").toInt,
        args.getOrElse("h", "1").toInt
      ),
      args.contains("l"),
      args.contains("d")
    )
  }
}
