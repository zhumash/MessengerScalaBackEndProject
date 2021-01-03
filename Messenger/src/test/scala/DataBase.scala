import java.util
import java.util.TreeSet
import java.util.TreeMap
import java.util.ArrayList
import scala.concurrent.{ExecutionContext, Future}
class DataBase {
  val getPassword = new TreeMap[String, String]()
  val usermsg = new TreeMap[String, ArrayList [Message]]
  def sendMessage(fromLogin : String, fromPassword : String, text : String, to : String): String = {
    println(fromLogin + " " + fromPassword + " " + text + " " + to)
    if(getPassword.containsKey(to) == false || getPassword.containsKey(fromLogin) == false)
      return "Such user doest exists"
    if(getPassword.get(fromLogin) != fromPassword)
      return "Authorization problems"

    if(usermsg.containsKey(to) == false)
      usermsg.put(to, new ArrayList[Message]())
    usermsg.get(to).add(Message(fromLogin, text, false))
    println(usermsg.get(to).size())
    return "send successfully to" + to;
  }
  def getAllMessages(login : String, password : String): String = {
    if(getPassword.containsKey(login) == false)
      return "Authorization problems"
    if(getPassword.get(login)  != password)
      return "Authorization problems"
    var ans = ""
    val c = usermsg.getOrDefault(login, new util.ArrayList[Message]());


    for(i <- 0 until  c.size()){
      val cur = c.get(i)
      ans = ans + cur.from + ":" + cur.text + "\n";
    }

    return ans

  }

  def register(login : String, password : String): String ={

    if(getPassword.containsKey(login) == true)
      return "ERROR: SUCH LOGIN ALREAY EXISTS"
    if(login == "")
      return "empty string"
    println("added user " + login + password)
    getPassword.put(login, password)
    return "ok"
  }
  def check(login : String, password : String): Unit =  {
    return getPassword.getOrDefault(login, "") == password;
  }
}
