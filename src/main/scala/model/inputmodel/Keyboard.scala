package model.inputmodel

case class Keyboard(
    name: String,
    version: String,
    switch: SwitchType,
    tilt: Int,
    mc: Map[String, Microcontroller],
    blocks: List[KeyboardBlock],
)
