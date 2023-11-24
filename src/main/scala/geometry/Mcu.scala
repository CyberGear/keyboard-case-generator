package geometry

import enumeratum.{Enum, EnumEntry}
import scadla.InlineOps.{AngleConversions, Ops}
import scadla.{Cube, Cylinder, Solid}
import squants.space.Length
import squants.space.LengthConversions.LengthConversions

sealed abstract class Mcu(pcbWidth: Length, pcbLength: Length, pcbThickness: Length, port: Port) extends EnumEntry {

  val width: Length = 15.mm + pcbWidth + 4.mm

  def cutOut(): Solid = {
    val initialShape  = List[Solid](
      Cube(pcbWidth, pcbLength, pcbThickness * 3),
      Cube(pcbWidth, pcbLength, pcbThickness * 2).moveZ(pcbThickness).moveY(-2.mm),
      port.cutOut().move(pcbWidth / 2, pcbLength, pcbThickness),
    ).reduce(_ + _)
    val snap          = Cylinder(1.5.mm, pcbWidth / 2).rotateY(90.°).move(pcbWidth / 4, -1.1.mm, pcbThickness + 1.mm)
    val negativeSpace = List[Solid](
      Cube(0.5.mm, 2.mm, pcbThickness).moveY(-2.mm).moveX(pcbWidth / 4 - 0.5.mm),
      Cube(0.5.mm, 2.mm, pcbThickness).moveY(-2.mm).moveX(pcbWidth / 4 * 3),
      Cube(pcbWidth / 2 + 1.mm, 1.mm, pcbThickness * 3).moveX(pcbWidth / 4 - 0.5.mm).moveY(-3.mm),
      Cube(pcbWidth / 2, 3.mm, 1.mm).move(pcbWidth / 4, -2.mm, 3.25.mm),
      AdvCube(15.mm, 10.mm, 10.mm, xzr = Some(2.mm))
        .moveX((pcbWidth - 15.mm) / 2)
        .moveY(pcbLength + 1.5.mm)
        .moveZ(-2.5.mm),
    ).reduce(_ + _)

    (initialShape - snap + negativeSpace).moveX(-pcbWidth / 2).moveY(-pcbLength - 1.5.mm)
  }

  def addOn(): Solid = {
    val backBrace        = Cube(pcbWidth, 3.mm, pcbThickness * 3).moveY(-2.mm)
    val frontPanel       = AdvCube(pcbWidth + 4.mm, 1.5.mm, 9.mm, xzr = Some(1.mm)).moveY(pcbLength).moveX(-2.mm)
    val frontPanelBrace1 =
      AdvCube(2.mm, 5.mm, pcbThickness * 3, xyr = Some(1.mm), yzr = Some(1.mm)).moveY(pcbLength - 4.mm).moveX(-2.mm)
    val frontPanelBrace2 =
      AdvCube(2.mm, 5.mm, pcbThickness * 3, xyr = Some(1.mm), yzr = Some(1.mm)).moveY(pcbLength - 4.mm).moveX(pcbWidth)
    (backBrace + frontPanel + frontPanelBrace1 + frontPanelBrace2).moveX(-pcbWidth / 2).moveY(-pcbLength - 1.5.mm)
  }

}

object Mcu extends Enum[Mcu] {

  case class Generic(pcbWidth: Length, pcbLength: Length, pcbThickness: Length, port: Port)
      extends Mcu(pcbWidth, pcbLength, pcbThickness, port)

  case object ProC extends Mcu(19.mm, 35.15.mm, 1.65.mm, Port.UsbC)

  override def values: IndexedSeq[Mcu] = findValues
}
