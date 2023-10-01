import model.{Key, Layout, Row}
import org.scalatest.AppendedClues.convertToClueful
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import parser.YamlMapper

import scala.language.implicitConversions

class KleLayoutParserTest extends AnyFlatSpec with Matchers {

  it should "parse yaml" in {
    val actual = YamlMapper.readValue[Layout](
      asYamlString(
        """      [ { x:2.25,c:"#ffffff",p:"XDA",a:7},"Esc",{x:0.25},"F1","F2","F3","F4",{x:0.25},"F5","F6"],
        |      [ { y:0.25 },"M1","M2",{ x:0.25,a:5 },"~\n`","!\n1","@\n2","#\n3","$\n4","%\n5","^\n6" ],
        |      [ { a:7 },"M3","M4",{ x:0.25,w:1.5 },"Tab","Q","W","E","R","T" ],
        |      [ "M5","M6",{ x:0.25,w:1.25,w2:1.75,l:true },"Caps Lock",{ x:-1.25,w:1.75 },"Caps Lock","A","S","D","F","G" ],
        |      [ "M7","M8",{ x:0.25,w:2.25 },"Shift","Z","X","C","V","B" ],
        |      [ "M9","M0",{ x:0.25,w:1.25 },"Ctrl","Fn",{ w:1.25 },"Win",{ w:1.25 },"Alt",{ w:2.5 },"" ]
        |""".stripMargin
      )
    )

    actual.rows should not be Nil
  }

  it should "correct number of rows" in {
    val actual = YamlMapper.readValue[Layout](
      asYamlString(
        """[ "" ], [ "" ], [ "" ], [ "" ], [ "" ], [ "" ]"""
      )
    )

    actual.rows.length should be(6)
  }

  it should "correct number of columns" in {
    YamlMapper
      .readValue[Layout](
        asYamlString(
          """[ "", "", "", "", "", "", "" ]"""
        )
      )
      .rows
      .head
      .keys
      .length should be(7)

    YamlMapper
      .readValue[Layout](
        asYamlString(
          """[ "A", "B" ]"""
        )
      )
      .rows
      .head
      .keys
      .length should be(2)

    YamlMapper
      .readValue[Layout](
        asYamlString(
          """[ "A", {w:10},"B" ]"""
        )
      )
      .rows
      .head
      .keys
      .length should be(2)
  }

  it should "read correct width and position" in {
    YamlMapper
      .readValue[Layout](
        asYamlString(
          """[ "", "", "", "", "", "", "" ]"""
        )
      )
      .rows
      .head
      .keys
      .last
      .position
      .x should be(6)

    YamlMapper
      .readValue[Layout](
        asYamlString(
          """[ "", {x:3.75, w:0.25}, "", "" ]"""
        )
      )
      .rows
      .head
      .keys
      .last
      .position
      .x should be(5)

    YamlMapper
      .readValue[Layout](
        asYamlString(
          """[ {x:3.75}, "", {w:0.25}, "", "" ]"""
        )
      )
      .rows
      .head
      .keys
      .last
      .position
      .x should be(5)
  }

  it should "read custom horizontal keyboard alignment" in {
    val layout = YamlMapper.readValue[Layout](
      asYamlString(
        """[ "", {x:2}, "" ],
        |[ {x:1,w:2}, "" ],
        |[ "", {x:0.5}, "", {x:0.5}, "" ],
        |[ {x:0.75}, "", {x:0.5}, "" ]""".stripMargin
      )
    ) should be(
      Layout(
        Row(Key(), Key(x = 3)),
        Row(Key(x = 1, y = 1, w = 2)),
        Row(Key(y = 2), Key(x = 1.5, y = 2), Key(x = 3, y = 2)),
        Row(Key(x = 0.75, y = 3), Key(x = 2.25, y = 3))
      )
    )
  }

  it should "read custom vertical keyboard alignment" in {
    YamlMapper.readValue[Layout](
      asYamlString(
        """[ "", {x:2}, "" ],
          |[ {y:-0.5,x:1,w:2}, "" ],
          |[ {y:-0.5}, "", {x:2}, "" ],
          |[ {y:0.25,w:4}, "" ],
          |[ {y:0.25,w:1}, "", "", "", "" ]""".stripMargin
      )
    ) should be(
      Layout(
        Row(Key(), Key(x = 3)),
        Row(Key(x = 1, y = 0.5, w = 2)),
        Row(Key(y = 1), Key(x = 3, y = 1)),
        Row(Key(y = 2.25, w = 4)),
        Row(Key(y = 3.5), Key(x = 1, y = 3.5), Key(x = 2, y = 3.5), Key(x = 3, y = 3.5))
      )
    )
  }

  private def asYamlString(kle: String): String =
    s""""${kle.replace("\"", "\\\"")}""""

  implicit class AnyLog[A](a: A) {
    def log: A = {
      println(a)
      a
    }
  }

}
