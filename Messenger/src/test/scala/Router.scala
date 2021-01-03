import akka.actor.typed.ActorSystem
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import  io.circe.generic.auto._
import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directives, Route}


import scala.concurrent.ExecutionContext

trait Router {
  def route: Route
}
case class RegisterInfo(login : String = "def", password : String = "def")
case class SendMessageInfo(fromPass : String = "!", fromLogin : String ="!", text : String = "!", to : String ="1")
class MyRouter(val data: DataBase)(implicit system: ActorSystem[_],  ex:ExecutionContext)
  extends  Router
    with  Directives
    with HealthCheckRoute
    with ValidatorDirectives
    with AddressDirectives {
    def register = {
      concat(
        pathPrefix("register") {
          pathEndOrSingleSlash {
            entity(as[RegisterInfo]) { r =>
              get {
                complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h2>" + data.register(r.login, r.password)+"</h2>"));
              }
            }
          }
        }
        ,
        pathPrefix("send") {
          pathEndOrSingleSlash{
            get{
              entity(as[SendMessageInfo]) { r =>
                complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h2>" + data.sendMessage(r.fromLogin, r.fromPass, r.text, r.to)+"</h2>"));

              }
            }
          }
        },
        pathPrefix("getAll") {
          pathEndOrSingleSlash{
            get{
              entity(as[RegisterInfo]) { r=>
                complete(data.getAllMessages(r.login, r.password))

              }

            }
          }
        }

      )
    }


  override def route = {
    concat(
      register,
      healthCheck,
    )
  }
}