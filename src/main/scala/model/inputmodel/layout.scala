package model.inputmodel

case class Layout(rows: List[Row])
object Layout {
  def apply(rows: Row*): Layout = Layout(rows.toList)
}

case class Row(keys: List[Key])
object Row {
  def apply(keys: Key*): Row = Row(keys.toList)
}

case class Key(
    x: BigDecimal = 0,
    y: BigDecimal = 0,
    w: BigDecimal = 1,
    h: BigDecimal = 1,
    stepped: Boolean = false,
    decal: Boolean = false,
) {
  override def toString: String = s"Key($x;$y ${w}x$h)"
}
