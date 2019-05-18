package it.unibo.sc1819.worker

import io.vertx.scala.core.Vertx
import it.unibo.sc1819.util.messages.Topic

object MockWorkerMain extends App {

  val defaultSerialPort = "/dev/ttyS80"
  val serialBauldRate = 9600

  val vertx = Vertx.vertx

  val workerVerticle = WorkerVerticle(defaultSerialPort, serialBauldRate, vertx)

  vertx.deployVerticle(workerVerticle)

  while (true) {

      vertx.eventBus.publish(Topic.SETUP_TOPIC_WORKER,"MOCK_CONFIGURATION")
    Thread.sleep(10000)
  }

}
