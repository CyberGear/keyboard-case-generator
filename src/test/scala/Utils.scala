trait Utils {

  protected def asYamlString(kle: String): String =
    s""""${kle.replace("\"", "\\\"")}""""

  implicit class AnyLog[A](a: A) {
    def log: A = {
      println(a)
      a
    }
  }

}
