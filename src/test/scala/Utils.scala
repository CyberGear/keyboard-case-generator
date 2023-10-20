import model.TestLayout
import model.inputmodel.Layout
import parser.YamlMapper

trait Utils {

  implicit class AnyLog[A](a: A) {
    def log: A = {
      println(a)
      a
    }
  }

  def readLayoutYaml(yaml: String): Layout = {
    YamlMapper.readValue[TestLayout](yaml).layout
  }

}
