package com.akka

import java.util.concurrent.TimeUnit

import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.state.{HostInfo, QueryableStoreTypes, ReadOnlyWindowStore}
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import org.apache.kafka.common.serialization.Serdes

import scala.concurrent.{Await, ExecutionContext, Future}
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.akka.Retry
import org.apache.kafka.streams.errors.InvalidStateStoreException
import org.apache.kafka.streams.kstream._
import spray.json._

import scala.util.{Failure, Success}
import scala.concurrent.duration._




import concurrent.Promise

class RestService(val streams: KafkaStreams) {


  var bindingFuture: Future[Http.ServerBinding] = null

  var count = 0L

  var timeFrom = 0L


  private def to = {timeFrom = timeTo; search}

  val timeTo = System.currentTimeMillis
  implicit val system = ActorSystem("logs-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

 private var search =1507099934000L
  private def inc = {search += 1000L; search}
  def start(): Unit = {

    val route =
      path("errors") {
        get {
          try {

            var future: Future[Long] = null

            future = fetchCountErrors()

            val countings = Await.result(future, 9 seconds)

            complete(countings.toString)
          }
          catch {
            case (ex: Exception) => {

              complete(ex.toString)
            }
          }

        }
      }
    bindingFuture = Http().bindAndHandle(route, "localhost", 8081)
    println(s"Server online at http://localhost:8081/\n")

    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run(): Unit = {
        bindingFuture
          .flatMap(_.unbind()) // trigger unbinding from the port
          .onComplete(_ => system.terminate()) // and shutdown when don
      }
    })

  }

  def fetchCountErrors(): Future[Long] = {

    import ExecutionContext.Implicits.global
    val ec = ExecutionContext.global

    val p = Promise[Long]()
    val f = Future {

      Thread.sleep(6000L)

        try {

          val store: ReadOnlyWindowStore[String, Long] = streams.store("errors_par_minute", QueryableStoreTypes.windowStore[String, Long]())



          val iterator = store.fetch((inc).toString, to, timeTo)

          while ( {
            iterator.hasNext
          }) {
            val next = iterator.next
            count = next.value
            p.success(count)
          }

          println(search)

        } catch {

          case ioe: InvalidStateStoreException => println("error")

        }
     count
    }(ec)

    p.future
  }

}


