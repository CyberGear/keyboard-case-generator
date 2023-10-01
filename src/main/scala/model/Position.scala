package model

case class Position(x: BigDecimal, y: BigDecimal)  {
  override def toString: String = s"$x;$y"
}