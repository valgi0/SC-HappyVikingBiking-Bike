package it.unibo.sc1819.client

import io.vertx.lang.scala.ScalaVerticle

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


