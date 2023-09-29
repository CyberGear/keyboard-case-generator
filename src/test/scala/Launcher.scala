import scadla.Cube
import scadla.backends.OpenSCAD
import squants.space.LengthConversions.LengthConversions
import scadla._
import utils._
import Trig._
import InlineOps._

import scala.sys.process._
import scala.language.postfixOps
import squants.space.LengthConversions._

import java.io.File
import scadla._
import scadla.utils.fold
import dzufferey.utils.SysCmd
import parser.YamlMapper

import java.io._
import squants.space.{Length, LengthUnit, Millimeters}

object Launcher extends App {

  case class Name(name: String, age: Int, stuff: List[String])

  println(YamlMapper.writeValueAsString(Name("Jonas", 20, List("Reddit", "4chan"))))

  println(YamlMapper.readValue[Map[String, Any]](new FileInputStream("MK-TKL-split.yml")))

//  def preview(obj: Solid): Unit = {
//    val file: File = File.createTempFile("temp-", ".stl")
//    OpenSCAD.toSTL(obj, file.getAbsolutePath)
//
//    s"""xdg-open ${file.getAbsolutePath}""" !
//
//    s"""rm ${file.getAbsolutePath}""" !
//
//  }
//
//  preview(Cube(20 mm, 20 mm, 20 mm))

}
