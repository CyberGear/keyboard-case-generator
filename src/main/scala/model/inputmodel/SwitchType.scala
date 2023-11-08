package model.inputmodel

import enumeratum._

sealed trait SwitchType extends EnumEntry

object SwitchType extends Enum[SwitchType] {

  case object Mx         extends SwitchType

  val values: IndexedSeq[SwitchType] = findValues
}
