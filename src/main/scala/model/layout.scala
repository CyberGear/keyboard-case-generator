package model

case class Layout(rows: List[Row])

case class Row(keys: List[Key])

case class Key(
    position: Position,
    size: Size = Size(1, 1),
    stepped: Boolean = false,
    decal: Boolean = false
)
