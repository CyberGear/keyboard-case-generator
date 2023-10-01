package inputmodel

import enumeratum._

sealed abstract class Edge extends EnumEntry

object Edge extends Enum[Edge] {

  case object Top    extends Edge
  case object Bottom extends Edge
  case object Left   extends Edge
  case object Right  extends Edge

  val values: IndexedSeq[Edge] = findValues
}
