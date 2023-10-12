import model.inputmodel.Keyboard
import squants.{MetricSystem, SiUnit}
import squants.space.{Length, LengthUnit, Millimeters}
import squants.space.LengthConversions._

import scala.language.postfixOps

package object constants {

  val StabsGap: Map[BigDecimal, Length] = Map(
    BigDecimal(2) -> 23.8.mm,
    BigDecimal(2.25) -> 23.8.mm,
    BigDecimal(2.75) -> 23.8.mm,
    BigDecimal(3) -> 38.1.mm,
    BigDecimal(4) -> 57.15.mm,
    BigDecimal(4.5) -> 69.3.mm,
    BigDecimal(5.5) -> 85.7.mm,
    BigDecimal(6) -> 95.mm,
    BigDecimal(6.25) -> 100.mm,
    BigDecimal(6.5) -> 104.76.mm,
    BigDecimal(7) -> 114.3.mm,
    BigDecimal(8) -> 133.35.mm,
    BigDecimal(9) -> 133.35.mm,
    BigDecimal(10) -> 133.35.mm,
  )

  object Unit {
    val Place: Length = 19.05 mm
    val Keycap: Length = 18.00 mm
    val Switch: Length = 14.00 mm
  }

  val CaseBrim: Length = 6 mm
  val CapInset: Length = (Unit.Place - Unit.Keycap) / 2

  implicit class KeyboardConversions[A](n: A)(implicit num: Numeric[A]) {
    def pu: Length = Millimeters(BigDecimal(Millimeters(n).value) * BigDecimal(Unit.Place.value))
    def ku: Length = Millimeters(BigDecimal(Millimeters(n).value) * BigDecimal(Unit.Keycap.value))
    def su: Length = Millimeters(BigDecimal(Millimeters(n).value) * BigDecimal(Unit.Switch.value))
  }

}
