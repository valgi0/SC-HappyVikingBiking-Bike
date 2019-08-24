package it.unibo.sc1819

import io.vertx.scala.core.Vertx
import it.unibo.sc1819.client.ClientVerticle
import it.unibo.sc1819.client.bikeclient.BikeClient
import it.unibo.sc1819.util.messages.Topic
import it.unibo.sc1819.worker.WorkerVerticle
import it.unibo.sc1819.worker.serial.messages.SerialMessage
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

  val MockConfigurationMessage = "LIGHT=75END"
  val gpsQualityMessage = "lat=109.23, lon=134.12"

  val conf = new Conf(args)
  var remoteaddress = "asw-happy-viking-biking.herokuapp.com"
  var remoteport = 80
  var rackaddress = "192.168.1.156"
  var rackport = 8888
  var bikeID = "1"
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
  val clientVerticle = ClientVerticle(bikeID, vertx, remoteaddress, remoteport, rackaddress, rackport)
  vertx.deployVerticle(workerVerticle)
  vertx.deployVerticle(clientVerticle)
}



