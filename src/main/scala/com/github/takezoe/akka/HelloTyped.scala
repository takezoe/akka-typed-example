package com.github.takezoe.akka

import akka.typed._
import akka.typed.ScalaDSL._
import akka.typed.AskPattern._
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object TypedHello {
  final case class HelloMessage(name: String, replyTo: ActorRef[HelloResult])
  final case class HelloResult(message: String)

  val helloBahavior = ContextAware[HelloMessage] { ctx =>
    Static {
      case HelloMessage(name, replyTo) => replyTo ! HelloResult(s"Hello $name!")
    }
  }
}


object HelloTypedMain extends App {
  implicit val timeout = Timeout(5 seconds)

  val system: ActorSystem[TypedHello.HelloMessage] = ActorSystem("system", Props(TypedHello.helloBahavior))
  val f = system ? (TypedHello.HelloMessage("Naoki", _: ActorRef[TypedHello.HelloResult]))

  f.foreach { result =>
    println(result.message)
  }

}