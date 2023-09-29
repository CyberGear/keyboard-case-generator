package model

import enumeratum._

sealed abstract class Port(shape: Polygon) extends EnumEntry

object Port extends Enum[Port] {

  case object UsbC extends Port(Polygon(Nil))
  case object Sata extends Port(Polygon(Nil))

  val values: IndexedSeq[Port] = findValues
}

case class MicrocontrollerPort(
    `type`: Option[Port],
    shape: Option[Polygon],
    edge: Edge,
    horizontalPosition: BigDecimal,
    verticalOffset: BigDecimal
)

case class PortCutout(
    `type`: Port,
    edge: Edge,
    position: BigDecimal
)
