package com.ageorgin.httpserverbenchmark

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.{Controller, HttpServer}
import com.twitter.finatra.http.routing.HttpRouter

// Attention il faut impérativement ajouter un paramètre de lancement -http.port=:8080

object Main extends HttpServerBenchmark

class HttpServerBenchmark extends HttpServer {
  override def configureHttp(router: HttpRouter): Unit = {
    router.add[HttpServerBenchmarkController]
  }
}

class HttpServerBenchmarkController extends Controller {
  get("/test") { request: Request =>
    "it works"
  }
}
