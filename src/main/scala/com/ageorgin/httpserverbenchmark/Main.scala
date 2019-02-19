package com.ageorgin.httpserverbenchmark

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.{Controller, HttpServer}
import com.twitter.finatra.http.routing.HttpRouter

import scala.io.Source

object Main extends HttpServerBenchmark

class HttpServerBenchmark extends HttpServer {

  override val defaultHttpPort: String = ":8080"

  override def configureHttp(router: HttpRouter): Unit = {
    router.add[HttpServerBenchmarkController]
  }
}

class HttpServerBenchmarkController extends Controller {
  get("/test") { request: Request =>
    "it works"
  }

  get("/cpu/:iterations") { request: Request =>
    val result = slowFunction(request.getIntParam("iterations"))
    result.toString
  }

  get("/cpu-print/:iterations") { request: Request =>
    val result = slowFunctionWithPrint(request.getIntParam("iterations"))
    result.toString
  }

  get("/io") { request: Request =>
    val result = ioFunction()
    result.toString
  }

  get("/io-print") { request: Request =>
    val result = ioFunctionWithPrint()
    result.toString
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
}
