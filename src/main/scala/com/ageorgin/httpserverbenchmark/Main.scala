package com.ageorgin.httpserverbenchmark

import cats.effect.IO
import com.twitter.finagle.Http
import com.twitter.util.Await
import io.finch._
import io.finch.catsEffect._

object Main extends App {

  def test: Endpoint[IO, String] = get("test") {
    Ok("It works")
  }

  Await.ready(Http.server.serve("0.0.0.0:8080", test.toServiceAs[Text.Plain]))
}
