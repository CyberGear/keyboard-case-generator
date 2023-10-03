package model.inputmodel

import model.inputmodel.Layout

case class KeyboardBlock(
    name: String,
    mc: MicrocontrollerPosition,
    layout: Layout,
    ports: List[PortCutout],
    top: EdgeType = EdgeType.Free,
    bottom: EdgeType = EdgeType.Free,
    left: EdgeType = EdgeType.Free,
    right: EdgeType = EdgeType.Free,
)
