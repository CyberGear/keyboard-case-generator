package model

import enumeratum._

sealed trait SwitchType extends EnumEntry

object SwitchType extends Enum[SwitchType] {

  case object Mx         extends SwitchType
  case object LowProfile extends SwitchType

  val values: IndexedSeq[SwitchType] = findValues
}
