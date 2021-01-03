import akka.actor.typed.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object  Server {
  def startHttpServer(routes: Route, host: String, port: Int)(implicit system: ActorSystem[_], ex: ExecutionContext): Unit = {
    // Akka HTTP still needs a classic ActorSystem to start
    val futureBinding = Http().newServerAt(host, port).bind(routes)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        println("Server online at http://" + address.getHostString+":"+address.getPort+"/")
      case Failure(ex) =>
        println("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }
}
