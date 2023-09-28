import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.scala.ClassTagExtensions

object YamlMapper extends ObjectMapper(new YAMLFactory) with ClassTagExtensions {
  findAndRegisterModules()
}
