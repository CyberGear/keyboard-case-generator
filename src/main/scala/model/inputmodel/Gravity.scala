package model.inputmodel

import enumeratum.{Enum, EnumEntry}

sealed trait Gravity extends EnumEntry
object Gravity extends Enum[Gravity] {
  case object Left extends Gravity
  case object Center extends Gravity
  case object Right extends Gravity

  val values: IndexedSeq[Gravity] = findValues
}