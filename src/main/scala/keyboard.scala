import Edge.findValues
import enumeratum._

sealed trait SwitchType extends EnumEntry
object SwitchType       extends Enum[SwitchType] {
  case object MX         extends SwitchType
  case object LowProfile extends SwitchType
  val values = findValues
}

case class Size(width: Int, height: Int)

case class Position(x: BigDecimal, y: BigDecimal)
case class PolyEdge(point: Position, radius: BigDecimal = 0)
case class Polygon(edges: List[PolyEdge])

sealed abstract class Port(shape: Polygon) extends EnumEntry
object Port                                extends Enum[Port] {
  case object UsbC extends Port(Polygon(Nil))
  case object Sata extends Port(Polygon(Nil))
  val values = findValues
}

sealed abstract class Edge extends EnumEntry
object Edge                extends Enum[Edge] {
  case object Top    extends Edge
  case object Bottom extends Edge
  case object Left   extends Edge
  case object Right  extends Edge
  val values = findValues
}

case class McPort(
    `type`: Option[Port],
    shape: Option[Polygon],
    edge: Edge,
    horizontalPosition: BigDecimal,
    verticalOffset: BigDecimal
)

case class MountingHole(
    diameter: BigDecimal,
    position: Position
)

case class Microcontroller(
    size: Size,
    thickness: BigDecimal,
    port: McPort,
    mountingHoles: List[MountingHole]
)

sealed trait EdgeType extends EnumEntry
object EdgeType       extends Enum[EdgeType] {
  case object Aligned      extends EdgeType
  case object Free         extends EdgeType
  case object TraceButtons extends EdgeType
  val values = findValues
}

case class MicrocontrollerPosition(
    name: String,
    position: Position,
    angle: BigDecimal,
    anchorPoint: Position
)

case class PortCutout(
    `type`: Port,
    edge: Edge,
    position: BigDecimal
)

case class KeyboardBlock(
    top: EdgeType = EdgeType.Free,
    bottom: EdgeType = EdgeType.Free,
    left: EdgeType = EdgeType.Free,
    right: EdgeType = EdgeType.Free,
    mc: MicrocontrollerPosition,
    layout: String, // will describe a parser later
    ports: List[PortCutout]
)

case class Keyboard(
    name: String,
    version: String,
    switch: SwitchType,
    angle: Int,
    mc: Map[String, Microcontroller],
    blocks: KeyboardBlock
)
