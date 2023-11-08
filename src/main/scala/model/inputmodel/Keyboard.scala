package model.inputmodel

case class Keyboard(
    name: String,
    version: String,
    blocks: List[KeyboardBlock],
    switch: SwitchType = SwitchType.Mx,             // no other option yet
    tilt: Int = 0,                                  // no other tilt is supported yet
    mc: Map[String, Microcontroller] = Map.empty,   // no custom mc is supported yet
)
