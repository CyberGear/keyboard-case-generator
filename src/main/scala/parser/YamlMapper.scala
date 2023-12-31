package parser

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.scala.{ClassTagExtensions, DefaultScalaModule}
import com.github.pjfanning.enumeratum.EnumeratumModule
import model.inputmodel.Layout

object YamlMapper extends ObjectMapper(new YAMLFactory) with ClassTagExtensions {
  registerModule(DefaultScalaModule)
  registerModule(EnumeratumModule)
  findAndRegisterModules()

  registerModule(new SimpleModule().addDeserializer(classOf[Layout], new KleJsonDeserializer()))

}
