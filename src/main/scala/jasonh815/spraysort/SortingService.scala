package jasonh815.spraysort

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import jasonh815.akkasort.actors.AkkaSortMessage
import jasonh815.spraysort.actors.{SortActor, SortMessage}
import spray.httpx.SprayJsonSupport
import spray.json.{CompactPrinter, DefaultJsonProtocol}

import scala.language.postfixOps
import scala.util.Random



object SortMessageProtocol extends DefaultJsonProtocol {
  implicit val sortMessageFormat = jsonFormat1(SortMessage)
  implicit val akkaSortMessageFormat = jsonFormat1(AkkaSortMessage)
}


// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class SortingServiceActor extends Actor with SortingService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)

  val akkaSorter = actorRefFactory.actorOf(SortActor.props)
}


// this trait defines our service behavior independently from the service actor
trait SortingService extends HttpService with SprayJsonSupport {

  def generateRandomData(count:Int) = SortMessage(Seq.fill[Int](count)(Random.nextInt(100)))

  import SortMessageProtocol._
  implicit val jsonPrinter = CompactPrinter

  val myRoute =
    path("") {
      get {
        respondWithMediaType(`text/html`) { // XML is marshalled to `text/xml` by default, so we simply override here
          complete {
            <html>
              <body>
                <h1>Say hello to <i>spray-routing</i> on <i>spray-can</i>!</h1>
              </body>
            </html>
          }
        }
      }
    } ~
    pathPrefix("sort") {
      pathPrefix("akka") {
        pathPrefix("local") {
          pathEnd {
            post {
              entity(as[SortMessage]) { msg =>
                actorRefFactory.actorOf(SortActor.props) ! msg
                complete {
                  "received: " + msg
                }
              }
            }
          } ~
            pathPrefix("random" / IntNumber) { count =>
              pathEnd {
                post {
                  val data = generateRandomData(count)
                  actorRefFactory.actorOf(SortActor.props) ! data
                  complete {
                    "sorting " + count + " numbers."
                  }
                }
              }
            }
        }~
        pathPrefix("remote") {
          val remote = actorRefFactory.actorSelection("akka.tcp://AkkaSortActorSystem@127.0.0.1:2556/user/sortActor")
          pathEnd {
            post {
              entity(as[AkkaSortMessage]) { msg =>
                remote ! msg
                complete {
                  "received: " + msg
                }
              }
            }
          } ~
          pathPrefix("random" / IntNumber) { count =>
            pathEnd {
              post {
                val msg = generateRandomData(count)
                remote ! AkkaSortMessage(msg.data)
                complete {
                  "sorting " + count + " numbers."
                }
              }
            }
          }
        }
      } ~
      path("mq") {
        import SortMessageProtocol._
        get {
          complete {
            SortMessage(Seq(1, 2, 3))
          }
        }
      }
    }

}