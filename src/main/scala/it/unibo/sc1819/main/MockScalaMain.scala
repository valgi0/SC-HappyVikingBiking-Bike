package it.unibo.sc1819.main

import io.vertx.scala.core.Vertx
import it.unibo.sc1819.client.ClientVerticle
import it.unibo.sc1819.util.messages.Topic

object MockScalaMain extends App {

 val remoteaddress = "127.0.0.1"
 val remoteport = 8080
 val mockBikeID = "bk012541655"
 val gpsMessage = "lat=10.778977, lon=187.098098"
 val aqMessage = "aq=1000, lat = 5, lon= 89898.9"
 val collMessage = gpsMessage
 val lockMessage = ""

 val vertx = Vertx.vertx

 val clientVerticle = ClientVerticle(mockBikeID, vertx, remoteaddress, remoteport, remoteaddress, remoteport)

 vertx.deployVerticle(clientVerticle)

 Thread.sleep(5000)

 vertx.eventBus().publish(Topic.LOCK_TOPIC_WEB, lockMessage)

}
