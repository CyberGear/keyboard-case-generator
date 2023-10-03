package model.inputmodel

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

case class Layout(keys: List[Key])