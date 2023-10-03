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

case class Layout(keys: List[Key]) {
  def invert: Layout = {
    val max = keys.map(k => k.y + k.h).max
    Layout(keys.map(k => k.copy(y = max - k.y - k.h)))
  }
}