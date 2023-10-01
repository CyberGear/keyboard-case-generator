import model.inputmodel.Keyboard
import parser.YamlMapper
import scadla.Solid

import java.io.File

object Launcher extends App {

  val keyboard = args.headOption match {
    case Some(path) => YamlMapper.readValue[Keyboard](path)
    case None       =>
      sys.error("keyboard config path is required")
      sys.exit(1)
  }

  def main(keyboard: Keyboard): Unit = {
    val keyboardCase = new KleKeyboardCaseGenerator(keyboard).generateCase
    Util.storeCase(keyboardCase)
  }

}
