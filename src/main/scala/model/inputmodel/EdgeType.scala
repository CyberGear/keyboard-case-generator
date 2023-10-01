package model.inputmodel

import enumeratum._

sealed trait EdgeType extends EnumEntry

object EdgeType extends Enum[EdgeType] {

  case object Aligned       extends EdgeType
  case object Free          extends EdgeType
  case object TraceButtons  extends EdgeType
  case object FollowButtons extends EdgeType

  val values: IndexedSeq[EdgeType] = findValues
}
