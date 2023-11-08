import model.TestLayout
import model.inputmodel.Layout
import org.joda.time.DateTime
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

  def version: String = {
    val now = DateTime.now()
    val year = now.getYear.toString.takeRight(2)
    val dayOfYear = now.dayOfYear().get()
    val minuteOfTheDay = now.minuteOfDay().get()

    s"$year.$dayOfYear.$minuteOfTheDay"
  }

}
