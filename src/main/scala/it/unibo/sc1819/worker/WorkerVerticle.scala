package it.unibo.sc1819.worker

import io.vertx.lang.scala.ScalaVerticle

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
