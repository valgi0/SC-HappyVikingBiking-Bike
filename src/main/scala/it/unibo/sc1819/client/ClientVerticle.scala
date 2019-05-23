package it.unibo.sc1819.client

import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.Vertx
import it.unibo.sc1819.client.ClientVerticle.VALUE_SEPARATOR
import it.unibo.sc1819.client.bikeclient.BikeClient
import it.unibo.sc1819.client.web.RequestMessage.{AQMessage, CollisionMessage, GPSMessage}
import it.unibo.sc1819.client.web.WebClient
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
    GPSMessage(values.head, values(1), bikeID)
  }

  private def parseAQMessage(msg:String, bikeID:String):AQMessage = {
    val values = msg.replace(" ","").split(VALUE_SEPARATOR).toStream.map(keyval => keyval.split("=")(1)).toList
    AQMessage(values(2), values.head, values(1), bikeID)
  }

  private def parseCollMessage(msg:String, bikeID:String): CollisionMessage = {
    val tempMessage = parseGPSMessage(msg, bikeID)
    CollisionMessage(tempMessage.latitude, tempMessage.longitude, tempMessage.bikeID)
  }

  private class ClientVerticleImpl(override val bikeID:String, val vertxContext:Vertx,
                                   val remoteAddress:String, val remotePort:Int,
                                   val rackAddress:String, val rackPort:Int) extends ClientVerticle {

    val eventBus = vertxContext.eventBus
    val bikeClient = BikeClient(vertxContext, bikeID ,remoteAddress, remotePort, rackAddress, rackPort)

    this.setup()


    override def onGPSMessageReceived(gpsMessage: String): Unit =
      bikeClient.notifyPosition(parseGPSMessage(gpsMessage, bikeID))

    override def onAQMessageReceived(aqMessage: String): Unit =
      bikeClient.notifyAirQuality(parseAQMessage(aqMessage, bikeID))

    override def onCollisionMessageReceived(collisionMessage: String): Unit =
      bikeClient.notifyCollision(parseCollMessage(collisionMessage, bikeID))

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

object RunTimeMain extends App {
  val string = "lat = 20, long = 30"
  val values = string.replace(" ","").split(VALUE_SEPARATOR).toStream.map(keyval => keyval.split("=")(1)).toList
  println(values)
}
