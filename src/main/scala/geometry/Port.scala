package geometry

import enumeratum.{Enum, EnumEntry}
import scadla.InlineOps.Ops
import scadla.Solid
import squants.space.LengthConversions.LengthConversions

sealed trait Port extends EnumEntry {
  def cutOut(): Solid
}

object Port extends Enum[Port] {
  private val cutOutLength = 17.mm

  case object UsbC extends Port {
    private val width = 9.mm
    private val thickness = 3.6.mm
    private val radius = 1.8.mm

    def cutOut(): Solid = AdvCube(width, cutOutLength, thickness, xzr = Some(radius)).moveX(width / -2).moveY(-7.mm)
  }

  override def values: IndexedSeq[Port] = findValues
}
