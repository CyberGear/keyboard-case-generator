import model.outputmodel.{Block, Case, Part}
import scadla.{Solid, Translate}
import scadla.backends.OpenSCAD
import squants.space.Length

import java.io.File
import scala.language.postfixOps
import scala.sys.process._

object Util {

  def storeCase(basePath: String, keyboardCase: Case): Unit = {
    keyboardCase.blocks.foreach { case Block(blockName, parts) =>
      parts.foreach { case Part(name, solid) =>
        val bName = blockName.map(name => s"_${name}").getOrElse("")
        val partName = s"${keyboardCase.name}_${keyboardCase.version}${bName}_$name.stl"
        val target = new File(s"$basePath/${keyboardCase.name}/${keyboardCase.version}/$partName")
        target.getParentFile.mkdirs()
        OpenSCAD.toSTL(solid, target.getAbsolutePath)
        println(target)
      }
    }
  }

  def preview(obj: Solid): Unit = {
    val file: File = File.createTempFile("temp-", ".stl")
    OpenSCAD.toSTL(obj, file.getAbsolutePath)

    s"""xdg-open ${file.getAbsolutePath}""" !

    s"""rm ${file.getAbsolutePath}""" !

  }

}
