import model.inputmodel.Keyboard
import parser.YamlMapper

object Launcher extends App {

  private val keyboard = args.headOption match {
    case Some(path) => YamlMapper.readValue[Keyboard](path)
    case None       =>
      sys.error("keyboard config path is required")
      sys.exit(1)
  }

  def main(keyboard: Keyboard): Unit = {
    val keyboardCase = new KleKeyboardCaseGenerator(keyboard).generateCase
    Util.storeCase(keyboardCase)
  }

  main(keyboard)

}
