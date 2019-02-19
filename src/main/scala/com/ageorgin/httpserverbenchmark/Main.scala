package com.ageorgin.httpserverbenchmark

import akka.actor.ActorSystem
import colossus.core.{IOSystem, InitContext, ServerContext}
import colossus.protocols.http.Http
import colossus.protocols.http.HttpMethod._
import colossus.protocols.http.UrlParsing._
import colossus.protocols.http.{HttpServer, Initializer, RequestHandler}
import colossus.service.Callback
import colossus.service.GenRequestHandler.PartialHandler

object Main extends App {

  implicit val actorSystem = ActorSystem()
  implicit val ioSystem    = IOSystem()

  HttpServer.start("hello-world", 8080) { context =>
    new HelloInitializer(context)
  }

}

class HelloInitializer(context: InitContext) extends Initializer(context) {

  override def onConnect: RequestHandlerFactory = context => new HelloRequestHandler(context)

}

class HelloRequestHandler(context: ServerContext) extends RequestHandler(context) {

  override def handle: PartialHandler[Http] = {
    case request @ Get on Root / "test" => {
      Callback.successful(request.ok("It Works!"))
    }
    case request @ Get on Root / "cpu" / Long(iterations) => {
      val result = slowFunction(iterations.toInt)
      Callback.successful(request.ok(result.toString))
    }
    case request @ Get on Root / "cpu-print" / Long(iterations) => {
      val result = slowFunctionWithPrint(iterations.toInt)
      Callback.successful(request.ok(result.toString))
    }
  }

  private def slowFunction(iterations: Int): Double = {
    val t0 = System.nanoTime()
    var result: Double = 0

    for (i <- 0 until iterations) {
      result += Math.atan(i.toDouble) * Math.tan(i.toDouble)
    }

    val t1 = System.nanoTime()

    result
  }

  private def slowFunctionWithPrint(iterations: Int): Double = {
    val t0 = System.nanoTime()
    var result: Double = 0

    for (i <- 0 until iterations) {
      result += Math.atan(i.toDouble) * Math.tan(i.toDouble)
    }

    val t1 = System.nanoTime()
    println(s"elapsed time: ${(t1 - t0) / 1000000}")

    result
  }
}
