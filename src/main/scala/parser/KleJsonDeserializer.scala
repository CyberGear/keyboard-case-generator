package parser

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer}
import model.inputmodel._

import scala.annotation.tailrec

class KleJsonDeserializer extends JsonDeserializer[Layout] {

  private val Row    = """(?s).*?(\[(.+?)]).*""".r
  private val Mods   = """(?s)^[^"]*?(\{(.*?)}.*?,.*?".*?".*?,).*""".r
  private val Switch = """(?s)^.*?(".*?".*?,).*""".r

  override def deserialize(p: JsonParser, ctxt: DeserializationContext): Layout = {
    val kleJson = ctxt.readValue(p, classOf[String])
    readLayout(kleJson)
  }

  @tailrec
  private def readLayout(input: String, rows: List[Row] = Nil): Layout = input match {
    case Row(row, group) =>
      readLayout(
        input.drop(row.length + 1).trim,
        rows :+ readKeys(s"${group.trim},", rows.lastOption.map(_.keys.head))
      )
    case _               => Layout(rows)
  }

  @tailrec
  private def readKeys(row: String, previousRowKey: Option[Key], keys: List[Key] = Nil): Row =
    row match {
      case Mods(block, mods) =>
        readKeys(
          row.drop(block.length).trim,
          previousRowKey,
          keys :+ nextKey(previousRowKey, keys.lastOption, parseMods(mods))
        )
      case Switch(letter)    =>
        readKeys(
          row.drop(letter.length).trim,
          previousRowKey,
          keys :+ nextKey(previousRowKey, keys.lastOption)
        )
      case _                 => model.inputmodel.Row(keys)
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
    val xPos  = previous.map(_.position).getOrElse(Position(0, 0))
    val xSize = previous.map(_.size).getOrElse(Size(0, 0))

    val yPos  = previousRowKey.map(_.position).getOrElse(Position(0, 0))
    val ySize = previousRowKey.map(_.size).getOrElse(Size(0, 0))

    Key(
      Position(
        xPos.x + xSize.width + BigDecimal(args.getOrElse("x", "0")),
        previous.map(_.position.y).getOrElse(yPos.y + ySize.height + BigDecimal(args.getOrElse("y", "0")))
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
