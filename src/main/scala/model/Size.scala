package model

case class Size(width: BigDecimal, height: BigDecimal) {
  override def toString: String = s"${width}x${height}"
}