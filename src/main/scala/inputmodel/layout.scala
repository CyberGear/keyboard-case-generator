package inputmodel

case class Layout(rows: List[Row])
object Layout {
  def apply(rows: Row*): Layout = Layout(rows.toList)
}

case class Row(keys: List[Key])
object Row {
  def apply(keys: Key*): Row = Row(keys.toList)
}

case class Key(
    position: Position,
    size: Size,
    stepped: Boolean,
    decal: Boolean
)          {
  override def toString: String = s"Key($position $size)"
}
object Key {
  def apply(
      x: BigDecimal = 0,
      y: BigDecimal = 0,
      w: BigDecimal = 1,
      h: BigDecimal = 1,
      s: Boolean = false,
      d: Boolean = false
  ): Key = this(Position(x, y), Size(w, h), s, d)
}
