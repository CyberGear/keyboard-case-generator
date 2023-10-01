package model.outputmodel

import scadla.Solid

case class Case(name: String, version: String, blocks: List[Block])

case class Block(name: String, parts: List[Part])

case class Part(name: String, solid: Solid)
