package it.unibo.sc1819

import io.vertx.scala.core.Vertx
import it.unibo.sc1819.client.ClientVerticle
import it.unibo.sc1819.worker.WorkerVerticle

object Main extends App {

  val remoteaddress = "192.168.1.155"
  val remoteport = 8080
  val mockBikeID = "bk012541655"
  val defaultSerialPort = "/dev/ttyS80"
  val serialBauldRate = 9600

  val vertx = Vertx.vertx

  val workerVerticle = WorkerVerticle(defaultSerialPort, serialBauldRate, vertx)
  val clientVerticle = ClientVerticle(mockBikeID, vertx, remoteaddress, remoteport, remoteaddress, remoteport)

  vertx.deployVerticle(workerVerticle)
  vertx.deployVerticle(clientVerticle)

}
