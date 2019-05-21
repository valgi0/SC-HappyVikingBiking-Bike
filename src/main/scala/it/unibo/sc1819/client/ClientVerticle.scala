package it.unibo.sc1819.client

import io.vertx.lang.scala.ScalaVerticle
import it.unibo.sc1819.client.ClientVerticle.VALUE_SEPARATOR
import it.unibo.sc1819.client.web.RequestMessage.{AQMessage, CollisionMessage, GPSMessage}

trait ClientVerticle extends ScalaVerticle{

  /**
    * The id of the bike the client is running on
    * @return
    */
  def bikeID:String

  /**
    * Handler for a received GPS message
    * @param gpsMessage the gps message to parse
    */
  def onGPSMessageReceived(gpsMessage:String)

  /**
    * Handler for a received AQ message
    * @param aqMessage the aq message received.
    */
  def onAQMessageReceived(aqMessage:String)

  /**
    * Handler for a received CollisionMessage
    * @param collisionMessage the collision message to be sent
    */
  def onCollisionMessageReceived(collisionMessage:String)

  /**
    * Handler for a lock message
    */
  def onUnLockMessageReceived()

  /**
    * Handler for an unlock message
    */
  def onLockMessageReceived()
}

object ClientVerticle {

  val VALUE_SEPARATOR = ","

  private def parseGPSMessage(msg:String, bikeID:String):GPSMessage = {
   val values = msg.replace(" ","").split(VALUE_SEPARATOR).toStream.map(keyval => keyval.split("=")(1)).toList
    GPSMessage(values.head, values(1), bikeID)
  }

  private def parseAQMessage(msg:String, bikeID:String):AQMessage = {
    val values = msg.replace(" ","").split(VALUE_SEPARATOR).toStream.map(keyval => keyval.split("=")(1)).toList
    AQMessage(values(2), values.head, values(1), bikeID)
  }

  private def parseCollMessage(msg:String, bikeID:String): Unit = {
    val tempMessage = parseGPSMessage(msg, bikeID)
    CollisionMessage(tempMessage.latitude, tempMessage.longitude, tempMessage.bikeID)
  }
}

object RunTimeMain extends App {
  val string = "lat = 20, long = 30"
  val values = string.replace(" ","").split(VALUE_SEPARATOR).toStream.map(keyval => keyval.split("=")(1)).toList
  println(values)
}
