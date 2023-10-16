import model.inputmodel.Keyboard
import parser.YamlMapper

import java.io.File

object Launcher extends App {

  if (args.isEmpty || args.length > 2) {
    sys.error("Usage\n\n\tkeyboard-case-generator [mandatory: keyboard config file] [optional: output path]\n\n")
  }

  private val keyboard = YamlMapper.readValue[Keyboard](args(0))
  private val outputPath = args.get(1).map(new File(_).getAbsolutePath).getOrElse("./")

  println(s"## $outputPath")

  val keyboardCase = new KleKeyboardCaseGenerator(keyboard).generateCase
  Util.storeCase(outputPath, keyboardCase)

  implicit class ArrayImplicits[A](arr: Array[A]) {
    def get(n: Int): Option[A] =
      if (arr.length > n) Some(arr(n))
      else None
  }

}
