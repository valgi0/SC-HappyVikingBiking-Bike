package it.unibo.sc1819

import io.vertx.scala.core.Vertx
import it.unibo.sc1819.client.ClientVerticle
import it.unibo.sc1819.worker.WorkerVerticle
import org.rogach.scallop.{ScallopConf, ScallopOption}

class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
  val remoteaddress: ScallopOption[String] = opt[String]()
  val remoteport: ScallopOption[Int] = opt[Int]()
  val rackaddress: ScallopOption[String] = opt[String]()
  val rackport: ScallopOption[Int] = opt[Int]()
  val serialaddress: ScallopOption[String] = opt[String]()
  val serialrate: ScallopOption[Int] = opt[Int]()
  val bikeid: ScallopOption[String] = opt[String]()
  verify()
}

object Main extends App {

  val conf = new Conf(args)
  var remoteaddress = "192.168.1.155"
  var remoteport = 8080
  var rackaddress = "192.168.1.155"
  var rackport = 8888
  var bikeID = "bk-000000"
  var serialAddress = "/dev/ttyS80"
  var serialBauldRate = 9600

  if (conf.remoteaddress.supplied) remoteaddress = conf.remoteaddress()
  if (conf.remoteport.supplied) remoteport = conf.remoteport()
  if (conf.rackaddress.supplied) rackaddress = conf.rackaddress()
  if (conf.rackport.supplied) rackport = conf.rackport()
  if (conf.bikeid.supplied) bikeID = conf.bikeid()
  if (conf.serialaddress.supplied) serialAddress = conf.serialaddress()
  if (conf.serialrate.supplied) serialBauldRate = conf.serialrate()


  val vertx = Vertx.vertx
  val workerVerticle = WorkerVerticle(serialAddress, serialBauldRate, vertx)
  val clientVerticle = ClientVerticle(bikeID, vertx, remoteaddress, remoteport, remoteaddress, remoteport)
  vertx.deployVerticle(workerVerticle)
  vertx.deployVerticle(clientVerticle)

}
