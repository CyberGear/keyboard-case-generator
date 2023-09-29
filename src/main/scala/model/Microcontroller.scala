package model

case class Microcontroller(
    size: Size,
    thickness: BigDecimal,
    port: MicrocontrollerPort,
    mountingHoles: List[MountingHole]
)

case class MountingHole(
    diameter: BigDecimal,
    position: Position
)
