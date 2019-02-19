package com.ageorgin.httpserverbenchmark

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpEntity
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.ConfigFactory
import akka.http.scaladsl.server.Directives._

import scala.io.Source

object Main extends App {
  val conf = ConfigFactory.load()
  implicit val system = ActorSystem(conf.getString("application.name"), conf)
  implicit val materializer: Materializer = ActorMaterializer()


  val route = path("test") {
      get {
        complete(HttpEntity("it works"))
      }
    } ~
    pathPrefix("cpu" / IntNumber) { iterations =>
      get {
        val result = slowFunction(iterations)
        complete(HttpEntity(result.toString))
      }
    } ~
    pathPrefix("cpu-print" / IntNumber) { iterations =>
      get {
        val result = slowFunctionWithPrint(iterations)
        complete(HttpEntity(result.toString))
      }
    } ~
    path("io") {
      get {
        val result = ioFunction()
        complete(HttpEntity(result.toString))
      }
    } ~
    path("io-print") {
      get {
        val result = ioFunctionWithPrint()
        complete(HttpEntity(result.toString))
      }
    }

  private def slowFunction(iterations: Int): Double = {
    var result: Double = 0

    for (i <- 0 until iterations) {
      result += Math.atan(i.toDouble) * Math.tan(i.toDouble)
    }

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

  private def ioFunction(): Int = {
    var result = 0

    Source.fromFile("file.txt").foreach(char => if (char == 'e') result += 1)

    result
  }

  private def ioFunctionWithPrint(): Int = {
    val t0 = System.nanoTime()
    var result = 0

    Source.fromFile("file.txt").foreach(char => if (char == 'e') result += 1)

    val t1 = System.nanoTime()
    println(s"elapsed time: ${(t1 - t0) / 1000000}")

    result
  }

  Http().bindAndHandle(route, "localhost", 8080)
}
