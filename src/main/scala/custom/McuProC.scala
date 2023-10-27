package custom

import scadla.InlineOps.{AngleConversions, Ops}
import scadla.{Cube, Cylinder, Solid}
import squants.space.LengthConversions.LengthConversions

//noinspection NonAsciiCharacters
object McuProC {

  private val pcbX  = 19.mm
  private val pcbY  = 35.mm
  private val pcbZ  = 1.8.mm
  private val usbCx = 9.mm
  private val usbCz = 3.6.mm

  def apply(): Solid =
    Cube(pcbX, pcbY, pcbZ) +
      AdvCube(usbCx, 7.35.mm, usbCz, xzr = Some(usbCz / 2)).move((pcbX - usbCx) / 2, 29.1.mm, pcbZ) +
      Cube(12.5.mm, 27.3.mm, 3.3.mm).moveX((pcbX - 12.5.mm) / 2).moveY(1.8.mm)

  def cutOut(): Solid = {
    val initialShape = List[Solid](
      Cube(pcbX, pcbY, pcbZ * 3),
      Cube(pcbX, pcbY, pcbZ * 2).moveZ(pcbZ).moveY(-2.mm),
      AdvCube(usbCx, 17.35.mm, usbCz, xzr = Some(usbCz / 2)).move((pcbX - usbCx) / 2, 29.1.mm, pcbZ),
    ).reduce(_ + _)
    val snap = Cylinder(1.5.mm, pcbX / 2).rotateY(90.°).move(pcbX / 4, -1.1.mm, pcbZ + 1.mm)
    val negativeSpace = List[Solid](
      Cube(0.5.mm, 2.mm, pcbZ).moveY(-2.mm).moveX(pcbX / 4 - 0.5.mm),
      Cube(0.5.mm, 2.mm, pcbZ).moveY(-2.mm).moveX(pcbX / 4 * 3),
      Cube(pcbX / 2 + 1.mm, 1.mm, pcbZ*3).moveX(pcbX / 4 - 0.5.mm).moveY(-3.mm),
      Cube(pcbX / 2, 3.mm, 1.mm).move(pcbX / 4, -2.mm, 3.25.mm),
      AdvCube(15.mm, 10.mm, 10.mm, xzr = Some(2.mm)).moveX((pcbX - 15.mm) / 2).moveY(pcbY + 1.5.mm).moveZ(-2.mm)
    ).reduce(_ + _)

    (initialShape - snap + negativeSpace).moveX(-pcbX / 2).moveY(-pcbY - 1.5.mm)
  }

  def addOn(): Solid = {
    val backBrace = Cube(pcbX, 3.mm, pcbZ * 3).moveY(-2.mm)
    val frontPanel = AdvCube(pcbX + 4.mm, 1.5.mm, 8.5.mm, xzr = Some(1.mm)).moveY(pcbY).moveX(-2.mm)
    val frontPanelBrace1 = AdvCube(2.mm, 5.mm, pcbZ * 3, xyr = Some(1.mm), yzr = Some(1.mm)).moveY(pcbY - 4.mm).moveX(-2.mm)
    val frontPanelBrace2 = AdvCube(2.mm, 5.mm, pcbZ * 3, xyr = Some(1.mm), yzr = Some(1.mm)).moveY(pcbY - 4.mm).moveX(pcbX)
    (backBrace + frontPanel + frontPanelBrace1 + frontPanelBrace2).moveX(-pcbX / 2).moveY(-pcbY - 1.5.mm)
  }

}