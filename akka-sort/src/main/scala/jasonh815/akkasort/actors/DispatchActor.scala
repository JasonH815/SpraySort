package jasonh815.akkasort.actors

import akka.actor.{Actor, Props}

/**
  * Created by jason on 4/28/16.
  */
class DispatchActor extends Actor {
  override def receive: Receive = {
    case msg:AkkaSortMessage => context.actorOf(SortActor.props) ! msg
  }
}

object DispatchActor {
  val props = Props[DispatchActor]
}
