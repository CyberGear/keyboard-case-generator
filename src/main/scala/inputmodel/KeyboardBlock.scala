package inputmodel

case class KeyboardBlock(
    mc: MicrocontrollerPosition,
    layout: Layout,
    ports: List[PortCutout],
    top: EdgeType = EdgeType.Free,
    bottom: EdgeType = EdgeType.Free,
    left: EdgeType = EdgeType.Free,
    right: EdgeType = EdgeType.Free,
)
