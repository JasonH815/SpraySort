package jasonh815.akkasort.actors

import akka.actor.{Actor, Props}

/**
  * Created by Jason on 4/27/2016.
  */
class PublishActor extends Actor {
  override def receive: Receive = {
    case msg:AkkaSortMessage =>
      //println("Sorted result: " + msg)

      //check if the result is sorted
      val butFirst = msg.data.tail
      var prev = msg.data.head
      if (butFirst.forall{ current =>
        val temp = prev
        prev = current
        temp <= current
      }) println("The result is sorted") else println("The result is not sorted")
  }
}

object PublishActor {
  val props = Props[PublishActor]
}
