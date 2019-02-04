package com.ageorgin.httpserverbenchmark

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpEntity
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.ConfigFactory
import akka.http.scaladsl.server.Directives._

object Main extends App {
  val conf = ConfigFactory.load()
  implicit val system = ActorSystem(conf.getString("application.name"), conf)
  implicit val materializer: Materializer = ActorMaterializer()


  val route = path("test") {
      get {
        complete(HttpEntity("it works"))
      }
    }


  Http().bindAndHandle(route, "localhost", 8080)
}
