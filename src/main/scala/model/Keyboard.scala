package model

case class Keyboard(
    name: String,
    version: String,
    switch: SwitchType,
    angle: Int,
    mc: Map[String, Microcontroller],
    blocks: List[KeyboardBlock]
)
