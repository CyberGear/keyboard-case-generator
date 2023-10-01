package model.inputmodel

case class MicrocontrollerPosition(
    name: String,
    position: Position,
    angle: BigDecimal,
    anchorPoint: Position,
)
