package jasonh815.akkasort.actors

import akka.actor.{Actor, Props}

/**
  * Created by Jason on 4/27/2016.
  */
class SortActor extends Actor {

  val publisher = context.actorOf(PublishActor.props)

  override def receive: Receive = {
    case msg:AkkaSortMessage =>
      println("Sorting on thread: " + Thread.currentThread().getName)
      Thread.sleep(3000) // 3 second delay
      publisher ! AkkaSortMessage(msg.data.sorted)
  }
}

object SortActor {
  val props = Props[SortActor]
}

