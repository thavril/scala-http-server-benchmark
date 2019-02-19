package com.ageorgin.httpserverbenchmark

import cats.effect.IO
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Http, Service}
import com.twitter.util.Await
import io.finch._
import io.finch.catsEffect._

object Main extends App {

  def test: Endpoint[IO, String] = get("test") {
    Ok("It works")
  }

  def cpu: Endpoint[IO, String] = get("cpu" :: path[Int]) { i: Int =>
    val result = slowFunction(i)
    Ok(result.toString)
  }

  def cpuPrint: Endpoint[IO, String] = get("cpu-print" :: path[Int]) { i: Int =>
    val result = slowFunctionWithPrint(i)
    Ok(result.toString)
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

  def service: Service[Request, Response] = Bootstrap
      .serve[Text.Plain](test :+: cpu :+: cpuPrint)
      .toService

  Await.ready(Http.server.serve("0.0.0.0:8080", service))
}
