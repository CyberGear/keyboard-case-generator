import squants.{MetricSystem, SiUnit}
import squants.space.{Length, LengthUnit, Millimeters}
import squants.space.LengthConversions._

import scala.language.postfixOps

package object constants {

  object Unit {
    val Place: Length = 19.05 mm
    val Keycap: Length = 18.00 mm
    val Switch: Length = 14.00 mm
    val Case: Length = 31.05 mm
  }


  implicit class KeyboardConversions[A](n: A)(implicit num: Numeric[A]) {
    def pu: Length = Millimeters(BigDecimal(Millimeters(n).value) * BigDecimal(Unit.Place.value))
    def ku: Length = Millimeters(BigDecimal(Millimeters(n).value) * BigDecimal(Unit.Keycap.value))
    def su: Length = Millimeters(BigDecimal(Millimeters(n).value) * BigDecimal(Unit.Switch.value))
    def cu: Length = Millimeters(BigDecimal(Millimeters(n).value) * BigDecimal(Unit.Case.value))
  }

}
