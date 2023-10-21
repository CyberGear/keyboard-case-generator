package parser

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer}
import model.inputmodel._

import scala.annotation.tailrec

class KleJsonDeserializer extends JsonDeserializer[Layout] {

  private val Row    = """(?s).*?(\[(.+?)]),.*""".r
  private val Mods   = """(?s)^[^"]*?(\{(.*?)}.*?,.*?".*?".*?,).*""".r
  private val Switch = """(?s)^.*?(".*?".*?,).*""".r

  override def deserialize(p: JsonParser, context: DeserializationContext): Layout =
    Layout(readAllKeys(normalizeKleJson(context.readValue(p, classOf[String])))).invert

  private def normalizeKleJson(kleJson: String): String =
    kleJson.patch(kleJson.lastIndexOf("]"), "],", 1)

  @tailrec
  private def readAllKeys(input: String, rows: List[List[Key]] = Nil): List[Key] =
    input match {
      case Row(row, group) =>
        readAllKeys(
          input.drop(row.length + 1).trim,
          rows :+ readKeys(s"${group.trim},", rows.lastOption.map(_.head)),
        )
      case _               => rows.flatten
    }

  @tailrec
  private def readKeys(row: String, previousRowKey: Option[Key], keys: List[Key] = Nil): List[Key] =
    row match {
      case Mods(block, mods) =>
        readKeys(
          row.drop(block.length).trim,
          previousRowKey,
          keys :+ nextKey(previousRowKey, keys.lastOption, parseMods(mods)),
        )
      case Switch(letter)    =>
        readKeys(
          row.drop(letter.length).trim,
          previousRowKey,
          keys :+ nextKey(previousRowKey, keys.lastOption),
        )
      case _                 => keys
    }

  private def parseMods(modsString: String): Map[String, String] =
    modsString
      .split(",")
      .map(_.split(":"))
      .map(p => p.head.strip() -> p.last.strip())
      .toMap

  private def nextKey(
      previousRowKey: Option[Key],
      previous: Option[Key],
      args: Map[String, String] = Map.empty,
  ): Key = {
    val xKey = previous.getOrElse(Key(0, 0, 0, 0))
    val yKey = previousRowKey.getOrElse(Key(0, 0, 0, 0))

    Key(
      xKey.x + xKey.w + BigDecimal(args.getOrElse("x", "0")),
      previous.map(_.y).getOrElse(1 + yKey.y + BigDecimal(args.getOrElse("y", "0"))),
      BigDecimal(args.getOrElse("w", "1")),
      BigDecimal(args.getOrElse("h", "1")),
      args.contains("l"),
      args.contains("d"),
    )
  }

}
