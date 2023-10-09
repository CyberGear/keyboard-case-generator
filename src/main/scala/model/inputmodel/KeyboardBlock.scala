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
) {
  val size: Size = Size(
    layout.keys.maxByOption(_.x).map(k => k.w + k.x).getOrElse(0),
    layout.keys.maxByOption(_.y).map(k => k.h + k.y).getOrElse(0)
  )
}
