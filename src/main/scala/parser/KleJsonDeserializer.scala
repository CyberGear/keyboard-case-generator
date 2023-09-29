package parser

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer}
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import model.{Key, Layout, Position, Size}

import scala.annotation.tailrec

class KleJsonDeserializer extends JsonDeserializer[Layout] {

  private val Row       = """(?s).*?(\[(.+?)]).*""".r
  private val Modifiers = """(?s)^(\{(.*?)},".*?",).*""".r
  private val Switch    = """(?s)^(".*?",).*""".r

  override def deserialize(p: JsonParser, ctxt: DeserializationContext): Layout = {
    val kleJson = ctxt.readValue(p, classOf[String])

    readLayout(kleJson)
  }

  @tailrec
  private def readLayout(input: String, rows: List[model.Row] = Nil): Layout = input match {
    case Row(row, group) => readLayout(input.replace(row, ""), rows :+ readKeys(s"${group.trim},"))
    case _               => Layout(rows)
  }

  @tailrec
  private def readKeys(row: String, keys: List[Key] = Nil): model.Row = row match {
    case Modifiers(block, mods) => readKeys(row.drop(block.length), keys :+ nextKey(keys.lastOption, parseMods(mods)))
    case Switch(letter)         => readKeys(row.drop(letter.length), keys :+ nextKey(keys.lastOption))
    case _                      => model.Row(keys)
  }

  private def parseMods(modsString: String): Map[String, String] =
    modsString.split(",").map(_.split(":")).map(p => p.head.strip() -> p.last.strip()).toMap

  private def nextKey(previous: Option[Key], args: Map[String, String] = Map.empty): Key = {
    val position = previous.map(_.position).getOrElse(Position(0, 0))
    val size     = previous.map(_.size).getOrElse(Size(0, 0))
    Key(
      Position(
        position.x + size.width + BigDecimal(args.getOrElse("x", "0")),
        position.y + size.height + BigDecimal(args.getOrElse("y", "0"))
      ),
      Size(
        BigDecimal(args.getOrElse("w", "1")),
        BigDecimal(args.getOrElse("h", "1"))
      ),
      args.contains("l"),
      args.contains("d")
    )
  }

}
