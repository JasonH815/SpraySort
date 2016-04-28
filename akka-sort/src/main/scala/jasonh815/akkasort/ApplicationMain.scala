package jasonh815.akkasort

import akka.actor.ActorSystem
import jasonh815.akkasort.actors.DispatchActor

object ApplicationMain extends App {
  val system = ActorSystem("AkkaSortActorSystem")
  val sortActor = system.actorOf(DispatchActor.props, "sortActor")

}