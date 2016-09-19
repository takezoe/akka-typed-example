package com.github.takezoe.akka

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Hello {
  final case class HelloMessage(name: String)
  final case class HelloResult(message: String)

  class HelloActor extends Actor {
    def receive = {
      case HelloMessage(name) => sender ! HelloResult(s"Hello $name!")
    }
  }
}


object HelloActorMain extends App {
  implicit val timeout = Timeout(5 seconds)

  val system = ActorSystem("system")
  val actor = system.actorOf(Props[Hello.HelloActor])
  val f = actor ? Hello.HelloMessage("Naoki")

  f.mapTo[Hello.HelloResult].foreach { result =>
    println(result.message)
  }

}