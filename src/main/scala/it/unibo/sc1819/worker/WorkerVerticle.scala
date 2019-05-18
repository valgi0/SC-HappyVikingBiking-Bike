package it.unibo.sc1819.worker

import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.Vertx
import it.unibo.sc1819.util.messages.Topic
import it.unibo.sc1819.worker.serial.messages.SerialMessage
import it.unibo.sc1819.worker.serial.{SerialChannel, SerialListener}

/**
  * This trait will define the behaviour of the low level verticle inside each bike.
  * The purpouse of this verticle is to intercept the serial data, elaborate them and send it to the web client
  */
trait WorkerVerticle extends ScalaVerticle {

  /**
    * Method to be called when the bike is locked.
    */
  def onBikeLock():Unit

  /**
    * Method to be call when the bike is unlocked.
    */
  def onBikeUnlock():Unit

  /**
    * Callback for setting a configuration
    * @param configuration a string containing the configuration to be sent to the low level components
    */
  def onConfigurationRetreived(configuration:String)

  /**
    * CallBack to be called when a GPS message is notified.
    * @param gpsString the string containing the GPS information
    */
  def onGPSMessage(gpsString:String)

  /**
    * CallBack to be called when a AQ message is notified.
    * @param aqString the string containing the AQ information
    */
  def onAQMessage(aqString:String)

  /**
    * CallBack to be called when a collision message is notified.
    * @param collisionString the string containing the collision information
    */
  def onCollisionMessage(collisionString:String)
}

object WorkerVerticle {

  def apply(serialPort:String, rate:Int, vertx: Vertx): WorkerVerticle =
    new WorkerVerticleImpl(rate, serialPort, vertx)

  private class WorkerVerticleImpl(val rate:Int, serialPort:String, vertxContext:Vertx) extends WorkerVerticle {

    val eventBus = vertxContext.eventBus
    var serialChannel:SerialChannel = _
    setup()

    /**
      * Method to be called when the bike is locked.
      */
    override def onBikeLock(): Unit = {
      println("Bici bloccata")
      sendMessageOnChannel(Topic.LOCK_TOPIC_WEB)
    }

    /**
      * Method to be call when the bike is unlocked.
      */
    override def onBikeUnlock(): Unit =  {
      println("Bici Sbloccata")
      sendMessageOnChannel(Topic.UNLOCK_TOPIC_WEB)
    }

    /**
      * Callback for setting a configuration
      *
      * @param configuration a string containing the configuration to be sent to the low level components
      */
    override def onConfigurationRetreived(configuration: String): Unit = {
      println("Configurazione ritornata " + configuration)
      //serialChannel.sendMessage(SerialMessage(configuration).toSerializedMessage)
    }


    /**
      * CallBack to be called when a GPS message is notified.
      *
      * @param gpsString the string containing the GPS information
      */
    override def onGPSMessage(gpsString: String): Unit = {
      val fancyGPMessage = gpsString
      //TODO PARSE MESSAGE INTO SOMETHING NICE
      println("GPS message ricevuto: " + gpsString)
      //sendMessageOnChannel(Topic.GPS_TOPIC_WEB, fancyGPMessage)
    }

    /**
      * CallBack to be called when a AQ message is notified.
      *
      * @param aqString the string containing the AQ information
      */
    override def onAQMessage(aqString: String): Unit = {
      val fancyAQMessage = aqString
      //TODO Parse message
      println("AQ message ricevuto: " + aqString)
      sendMessageOnChannel(Topic.AQ_TOPIC_WEB, fancyAQMessage)
    }

    /**
      * CallBack to be called when a collision message is notified.
      *
      * @param collisionString the string containing the collision information
      */
    override def onCollisionMessage(collisionString: String): Unit = {
      val fancyCollisionString = collisionString
      //TODO Parse message
      println("Collision message ricevuto: " + collisionString)
      sendMessageOnChannel(Topic.AQ_TOPIC_WEB, fancyCollisionString)
    }

    private def setup(): Unit = {

      /*listenForMessages(Topic.GPS_TOPIC_WORKER, onGPSMessage)
      listenForMessages(Topic.AQ_TOPIC_WORKER, onAQMessage)
      listenForMessages(Topic.COLLISION_TOPIC_WORKER, onCollisionMessage)
      listenForMessagesNoBody(Topic.LOCK_TOPIC_WORKER, onBikeLock)
      listenForMessagesNoBody(Topic.UNLOCK_TOPIC_WORKER, onBikeUnlock)
      listenForMessages(Topic.SETUP_TOPIC_WORKER, onConfigurationRetreived)*/
      eventBus.consumer[String](Topic.SETUP_TOPIC_WORKER)
        .handler(message => onConfigurationRetreived(message.body()))
          .completionHandler( _ => {
            println("Worker sviluppato")
      eventBus.consumer[String](Topic.GPS_TOPIC_WORKER).handler(message => {
        onGPSMessage(message.body())
      }).completionHandler(_ => {
        println("GPS propagato")
        serialChannel = SerialChannel(serialPort, rate, SerialListener(vertxContext))
      })})
    }

    /**
      * Define a listener method to make possible listening on every channel
      * @param topic the topic on which the listener will listen
      * @param messageHandler the handler to call when a message is received.
      */
    private def listenForMessages(topic:String, messageHandler:String => Unit ):Unit = {
      eventBus.consumer[String](topic).handler(message => {
        messageHandler(message.body())
      })
    }

    private def listenForMessagesNoBody(topic:String, messageHandler:  => Unit ):Unit = {
      eventBus.consumer[String](topic).handler(_ => {
        messageHandler
      })
    }
    private def sendMessageOnChannel(topic:String, message:String = ""): Unit = {
      eventBus.publish(topic, message)
    }

  }


}
