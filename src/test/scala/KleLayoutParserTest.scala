import model.inputmodel.Key
import model.inputmodel.Layout
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import parser.YamlMapper

import scala.language.implicitConversions

class KleLayoutParserTest extends AnyFlatSpec with Matchers with Utils {

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

    actual.keys should not be Nil
  }

  it should "correct number of keys" in {
    YamlMapper
      .readValue[Layout](
        asYamlString(
          """[ "", "", "", "", "", "", "" ],
            |[ "", "" ]
            |""".stripMargin
        )
      )
      .keys
      .length should be(9)

    val actual1 = YamlMapper.readValue[Layout](asYamlString("""[ "A", "B" ]"""))
    actual1.keys.length should be(2)

    val actual2 = YamlMapper.readValue[Layout](asYamlString("""[ "A", {w:10},"B" ]"""))
    actual2.keys.length should be(2)
  }

  it should "read correct width and position" in {
    YamlMapper
      .readValue[Layout](
        asYamlString(
          """[ "", "", "", "", "", "", "" ]"""
        )
      )
      .keys
      .last
      .x should be(6)

    YamlMapper
      .readValue[Layout](
        asYamlString(
          """[ "", {x:3.75, w:0.25}, "", "" ]"""
        )
      )
      .keys
      .last
      .x should be(5)

    YamlMapper
      .readValue[Layout](
        asYamlString(
          """[ {x:3.75}, "", {w:0.25}, "", "" ]"""
        )
      )
      .keys
      .last
      .x should be(5)
  }

  it should "read custom horizontal keyboard alignment" in {
    YamlMapper.readValue[Layout](
      asYamlString(
        """[ "", {x:2}, "" ],
          |[ {x:1,w:2}, "" ],
          |[ "", {x:0.5}, "", {x:0.5}, "" ],
          |[ {x:0.75}, "", {x:0.5}, "" ]""".stripMargin
      )
    ) should be(
      Layout(List(
        Key(y = 3), Key(x = 3, y = 3),
        Key(x = 1, y = 2, w = 2),
        Key(y = 1), Key(x = 1.5, y = 1), Key(x = 3, y = 1),
        Key(x = 0.75), Key(x = 2.25)
      ))
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
      Layout(List(
        Key(y = 3.50), Key(x = 3, y = 3.50),
        Key(x = 1, y = 3.00, w = 2),
        Key(y = 2.50), Key(x = 3, y = 2.50),
        Key(y = 1.25, w = 4),
        Key(), Key(1), Key(2), Key(3)))
    )
  }

  ignore should "parse ANSI 104 layout from kle" in {
    YamlMapper.readValue[Layout](
      asYamlString(
        """["Esc",{x:1},"F1","F2","F3","F4",{x:0.5},"F5","F6","F7","F8",{x:0.5},"F9","F10","F11","F12",{x:0.25},"PrtSc","Scroll Lock","Pause\nBreak"],
          |[{y:0.5},"~\n`","!\n1","@\n2","#\n3","$\n4","%\n5","^\n6","&\n7","*\n8","(\n9",")\n0","_\n-","+\n=",{w:2},"Backspace",{x:0.25},"Insert","Home","PgUp",{x:0.25},"Num Lock","/","*","-"],
          |[{w:1.5},"Tab","Q","W","E","R","T","Y","U","I","O","P","{\n[","}\n]",{w:1.5},"|\n\\",{x:0.25},"Delete","End","PgDn",{x:0.25},"7\nHome","8\n↑","9\nPgUp",{h:2},"+"],
          |[{w:1.75},"Caps Lock","A","S","D","F","G","H","J","K","L",":\n;","\"\n'",{w:2.25},"Enter",{x:3.5},"4\n←","5","6\n→"],
          |[{w:2.25},"Shift","Z","X","C","V","B","N","M","<\n,",">\n.","?\n/",{w:2.75},"Shift",{x:1.25},"↑",{x:1.25},"1\nEnd","2\n↓","3\nPgDn",{h:2},"Enter"],
          |[{w:1.25},"Ctrl",{w:1.25},"Win",{w:1.25},"Alt",{a:7,w:6.25},"",{a:4,w:1.25},"Alt",{w:1.25},"Win",{w:1.25},"Menu",{w:1.25},"Ctrl",{x:0.25},"←","↓","→",{x:0.25,w:2},"0\nIns",".\nDel"]
          |""".stripMargin
      )
    ) shouldBe YamlMapper.readValue[Layout](
      asYamlString(
        """[{a:7},"",{x:1},"","","","",{x:0.5},"","","","",{x:0.5},"","","","",{x:0.25},"","",""],
          |[{y:0.5},"","","","","","","","","","","","","",{w:2},"",{x:0.25},"","","",{x:0.25},"","","",""],
          |[{w:1.5},"","","","","","","","","","","","","",{w:1.5},"",{x:0.25},"","","",{x:0.25},"","","",{h:2},""],
          |[{w:1.75},"","","","","","","","","","","","",{w:2.25},"",{x:3.5},"","",""],
          |[{w:2.25},"","","","","","","","","","","",{w:2.75},"",{x:1.25},"",{x:1.25},"","","",{h:2},""],
          |[{w:1.25},"",{w:1.25},"",{w:1.25},"",{w:6.25},"",{w:1.25},"",{w:1.25},"",{w:1.25},"",{w:1.25},"",{x:0.25},"","","",{x:0.25,w:2},"",""]
          |""".stripMargin
      )
    )
  }

  ignore should "parse ANSI 104 (big-ass enter) layout from kle" in {
    YamlMapper.readValue[Layout](
      asYamlString(
        """["Esc",{x:1},"F1","F2","F3","F4",{x:0.5},"F5","F6","F7","F8",{x:0.5},"F9","F10","F11","F12",{x:0.25},"PrtSc","Scroll Lock","Pause\nBreak"],
          |[{y:0.5},"~\n`","!\n1","@\n2","#\n3","$\n4","%\n5","^\n6","&\n7","*\n8","(\n9",")\n0","_\n-","+\n=","|\n\\","Back Space",{x:0.25},"Insert","Home","PgUp",{x:0.25},"Num Lock","/","*","-"],
          |[{w:1.5},"Tab","Q","W","E","R","T","Y","U","I","O","P","{\n[","}\n]",{w:1.5,h:2,w2:2.25,h2:1,x2:-0.75,y2:1},"Enter",{x:0.25},"Delete","End","PgDn",{x:0.25},"7\nHome","8\n↑","9\nPgUp",{h:2},"+"],
          |[{w:1.75},"Caps Lock","A","S","D","F","G","H","J","K","L",":\n;","\"\n'",{x:5.75},"4\n←","5","6\n→"],
          |[{w:2.25},"Shift","Z","X","C","V","B","N","M","<\n,",">\n.","?\n/",{w:2.75},"Shift",{x:1.25},"↑",{x:1.25},"1\nEnd","2\n↓","3\nPgDn",{h:2},"Enter"],
          |[{w:1.25},"Ctrl",{w:1.25},"Win",{w:1.25},"Alt",{a:7,w:6.25},"",{a:4,w:1.25},"Alt",{w:1.25},"Win",{w:1.25},"Menu",{w:1.25},"Ctrl",{x:0.25},"←","↓","→",{x:0.25,w:2},"0\nIns",".\nDel"]
          |""".stripMargin
      )
    ) shouldBe YamlMapper.readValue[Layout](
      asYamlString(
        """[{a:7},"",{x:1},"","","","",{x:0.5},"","","","",{x:0.5},"","","","",{x:0.25},"","",""],
          |[{y:0.5},"","","","","","","","","","","","","","","",{x:0.25},"","","",{x:0.25},"","","",""],
          |[{w:1.5},"","","","","","","","","","","","","",{w:1.5,h:2,w2:2.25,h2:1,x2:-0.75,y2:1},"",{x:0.25},"","","",{x:0.25},"","","",{h:2},""],
          |[{w:1.75},"","","","","","","","","","","","",{x:5.75},"","",""],
          |[{w:2.25},"","","","","","","","","","","",{w:2.75},"",{x:1.25},"",{x:1.25},"","","",{h:2},""],
          |[{w:1.25},"",{w:1.25},"",{w:1.25},"",{w:6.25},"",{w:1.25},"",{w:1.25},"",{w:1.25},"",{w:1.25},"",{x:0.25},"","","",{x:0.25,w:2},"",""]
          |""".stripMargin
      )
    )
  }

  ignore should "parse ISO 105 layout from kle" in {
    YamlMapper.readValue[Layout](
      asYamlString(
        """["Esc",{x:1},"F1","F2","F3","F4",{x:0.5},"F5","F6","F7","F8",{x:0.5},"F9","F10","F11","F12",{x:0.25},"PrtSc","Scroll Lock","Pause\nBreak"],
          |[{y:0.5},"¬\n`","!\n1","\"\n2","£\n3","$\n4","%\n5","^\n6","&\n7","*\n8","(\n9",")\n0","_\n-","+\n=",{w:2},"Backspace",{x:0.25},"Insert","Home","PgUp",{x:0.25},"Num Lock","/","*","-"],
          |[{w:1.5},"Tab","Q","W","E","R","T","Y","U","I","O","P","{\n[","}\n]",{x:0.25,w:1.25,h:2,w2:1.5,h2:1,x2:-0.25},"Enter",{x:0.25},"Delete","End","PgDn",{x:0.25},"7\nHome","8\n↑","9\nPgUp",{h:2},"+"],
          |[{w:1.75},"Caps Lock","A","S","D","F","G","H","J","K","L",":\n;","@\n'","~\n#",{x:4.75},"4\n←","5","6\n→"],
          |[{w:1.25},"Shift","|\n\\","Z","X","C","V","B","N","M","<\n,",">\n.","?\n/",{w:2.75},"Shift",{x:1.25},"↑",{x:1.25},"1\nEnd","2\n↓","3\nPgDn",{h:2},"Enter"],
          |[{w:1.25},"Ctrl",{w:1.25},"Win",{w:1.25},"Alt",{a:7,w:6.25},"",{a:4,w:1.25},"AltGr",{w:1.25},"Win",{w:1.25},"Menu",{w:1.25},"Ctrl",{x:0.25},"←","↓","→",{x:0.25,w:2},"0\nIns",".\nDel"]
          |""".stripMargin
      )
    ) shouldBe YamlMapper.readValue[Layout](
      asYamlString(
        """[{a:7},"",{x:1},"","","","",{x:0.5},"","","","",{x:0.5},"","","","",{x:0.25},"","",""],
          |[{y:0.5},"","","","","","","","","","","","","",{w:2},"",{x:0.25},"","","",{x:0.25},"","","",""],
          |[{w:1.5},"","","","","","","","","","","","","",{x:0.25,w:1.25,h:2,w2:1.5,h2:1,x2:-0.25},"",{x:0.25},"","","",{x:0.25},"","","",{h:2},""],
          |[{w:1.75},"","","","","","","","","","","","","",{x:4.75},"","",""],
          |[{w:1.25},"","","","","","","","","","","","",{w:2.75},"",{x:1.25},"",{x:1.25},"","","",{h:2},""],
          |[{w:1.25},"",{w:1.25},"",{w:1.25},"",{w:6.25},"",{w:1.25},"",{w:1.25},"",{w:1.25},"",{w:1.25},"",{x:0.25},"","","",{x:0.25,w:2},"",""]
          |""".stripMargin
      )
    )
  }

}
