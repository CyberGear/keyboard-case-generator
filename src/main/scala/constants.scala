import model.inputmodel.Keyboard
import squants.{MetricSystem, SiUnit}
import squants.space.{Length, LengthUnit, Millimeters}
import squants.space.LengthConversions._

import scala.language.postfixOps

package object constants {

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

  implicit class LengthImplicits(length: Length) {
    def + (addition: Length): Length = (length.value + addition.value) mm
  }

  implicit class BigDecimalImplicits(a: BigDecimal) {
    def orBigger(b: BigDecimal): BigDecimal = if (a > b) a else b
  }

}
