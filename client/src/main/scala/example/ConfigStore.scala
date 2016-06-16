package example

import org.scalajs.dom.ext._
import scala.scalajs.js.JSON
import org.scalajs.dom
import upickle.default._

object ConfigStore {

  def readConf: Config = {
    LocalStorage("config") match {
      case None       => Config()
      case Some(conf) => read[Config](conf)
    }
  }

  def writeConf(c: Config) = {
    LocalStorage.update("config", write(c))
  }

}

case class Config(
    add: Boolean = true,
    sub: Boolean = false,
    mul: Boolean = false,
    div: Boolean = false) {
  
  def toMap = Map(
    "+" -> add,
    "-" -> sub,
    "x" -> mul,
    "/" -> div)
    
    def updated(label:String, value:Boolean)=label match {
    case "+" => copy(add=value)
    case "-" => copy(sub=value)
    case "x" => copy(mul=value)
    case "/" => copy(div=value)
  }
}
