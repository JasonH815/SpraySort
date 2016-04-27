package jasonh815.spraysort.actors

import akka.actor.{Actor, Props}
import jasonh815.spraysort.SortMessage

/**
  * Created by Jason on 4/27/2016.
  */
class SortActor extends Actor {

  val publisher = context.actorOf(PublishActor.props)

  override def receive: Receive = {
    case msg:SortMessage =>
      println("Sorting on thread: " + Thread.currentThread().getName)
      Thread.sleep(3000) // 3 second delay
      publisher ! SortMessage(msg.data.sorted)
  }
}

object SortActor {
  val props = Props[SortActor]
}

