package model.inputmodel

import geometry.Mcu

case class KeyboardBlock(
    name: Option[String],
    mcu: Mcu,
    mcuGravity: Gravity = Gravity.Center,
    layout: Layout,
    additionalPorts: List[PortCutout],
    top: EdgeType = EdgeType.Free,
    bottom: EdgeType = EdgeType.Free,
    left: EdgeType = EdgeType.Free,
    right: EdgeType = EdgeType.Free,
) {
  val size: Size = Size(
    layout.keys.maxByOption(_.x).map(k => k.w + k.x).getOrElse(0),
    layout.keys.maxByOption(_.y).map(k => k.h + k.y).getOrElse(0),
  )
}
