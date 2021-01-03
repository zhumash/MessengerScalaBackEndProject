import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
//import org.slf4j.{Logger, LoggerFactory}

import scala.util.Try


object HttpServerSample {

  def main(args: Array[String]): Unit = {


    val rootBehavior = Behaviors.setup[Nothing] { context =>

      val data = new DataBase();
      val router = new MyRouter(data)(context.system, context.executionContext)
      val host = "0.0.0.0"
      val port = Try(System.getenv("PORT")).map(_.toInt).getOrElse(8090)
      data.register("A", "A")
      data.register("B", "B")
      Server.startHttpServer(router.route, host, port)(context.system, context.executionContext)
      Behaviors.empty
    }
    val system = ActorSystem[Nothing](rootBehavior, "HelloAkkaHttpServer")
  }
}