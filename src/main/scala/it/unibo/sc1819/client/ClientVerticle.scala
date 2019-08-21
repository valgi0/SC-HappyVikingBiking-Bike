package it.unibo.sc1819.client

import java.util.Date

import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.Vertx
import it.unibo.sc1819.client.ClientVerticle.VALUE_SEPARATOR
import it.unibo.sc1819.client.bikeclient.BikeClient
import it.unibo.sc1819.client.web.RequestMessage.{AQMessage, CollisionMessage, GPSMessage, LogMessage}
import it.unibo.sc1819.util.messages.Topic


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

  def apply(bikeID:String,  vertxContext:Vertx, remoteAddress:String, remotePort:Int,
  rackAddress:String, rackPort:Int): ClientVerticle =
    new ClientVerticleImpl(bikeID, vertxContext, remoteAddress, remotePort, rackAddress, rackPort)

  private def parseGPSMessage(msg:String, bikeID:String):GPSMessage = {
   val values = msg.replace(" ","").split(VALUE_SEPARATOR).toStream.map(keyval => keyval.split("=")(1)).toList
    GPSMessage(values.head, values(1), bikeID, new Date().getTime)
  }

  private def parseAQMessage(msg:String, bikeID:String):AQMessage = {
    val values = msg.replace(" ","").split(VALUE_SEPARATOR).toStream.map(keyval => keyval.split("=")(1)).toList
    AQMessage(values(2), values.head, values(1), bikeID, new Date().getTime)
  }

  private def parseCollMessage(msg:String, bikeID:String): LogMessage = {
    val tempMessage = parseGPSMessage(msg, bikeID)
    LogMessage(tempMessage.bikeId, web.crashMessageCode,
      "Crash registered at " + tempMessage.lat + ", " + tempMessage.lon)
  }

  private class ClientVerticleImpl(override val bikeID:String, val vertxContext:Vertx,
                                   val remoteAddress:String, val remotePort:Int,
                                   val rackAddress:String, val rackPort:Int) extends ClientVerticle {

    val eventBus = vertxContext.eventBus
    val bikeClient = BikeClient(vertxContext, bikeID ,remoteAddress, remotePort, rackAddress, rackPort)

    this.setup()


    override def onGPSMessageReceived(gpsMessage: String): Unit = {
      println("GPS MESSAGE TO PARSE: " + gpsMessage)
      val parsedGPSMessage = parseGPSMessage(gpsMessage, bikeID)
      if(parsedGPSMessage.lon.toFloat > 1 && parsedGPSMessage.lat.toFloat > 1) {
        println("GPS MESSAGE PARSATO: " + gpsMessage)
        bikeClient.notifyPosition(parseGPSMessage(gpsMessage, bikeID))
      }
    }


    override def onAQMessageReceived(aqMessage: String): Unit = {
      println("AQ MESSAGE TO PARSE: " + aqMessage)
      val parsedAQMessage = parseAQMessage(aqMessage, bikeID)
      if(parsedAQMessage.air.toFloat != 0 && parsedAQMessage.lon.toFloat > 0 &&
      parsedAQMessage.lat.toFloat > 0) {
        println("PARSATO MESSAGGIO AQ: " + aqMessage)
        bikeClient.notifyAirQuality(parseAQMessage(aqMessage, bikeID))
      }
    }


    override def onCollisionMessageReceived(collisionMessage: String): Unit = {
      println("COLLISION MESSAGE TO PARSE: " + collisionMessage)
      bikeClient.notifyCollision(parseCollMessage(collisionMessage, bikeID))
    }


    override def onUnLockMessageReceived(): Unit = bikeClient.fetchConfiguration()

    override def onLockMessageReceived(): Unit = bikeClient.notifyLock()


    private def setup():Unit = {
      listenForMessages(Topic.GPS_TOPIC_WEB, onGPSMessageReceived)
      listenForMessages(Topic.AQ_TOPIC_WEB, onAQMessageReceived)
      listenForMessages(Topic.COLLISION_TOPIC_WEB, onCollisionMessageReceived)
      listenForMessagesNoBody(Topic.LOCK_TOPIC_WEB, () => this.onLockMessageReceived())
      listenForMessagesNoBody(Topic.UNLOCK_TOPIC_WEB, () => this.onUnLockMessageReceived())
    }

    /**
      * Define a listener method to make possible listening on every channel
      * @param topic the topic on which the listener will listen
      * @param messageHandler the handler to call when a message is received.
      */
    private def listenForMessages(topic:String, messageHandler:String => Unit ) = {
      eventBus.consumer[String](topic).handler(message => {
        messageHandler(message.body())
      })
    }

    private def listenForMessagesNoBody(topic:String, messageHandler: ()  => Unit ) = {
      eventBus.consumer[String](topic).handler(_ => {
        messageHandler()
      })
    }
  }
}

