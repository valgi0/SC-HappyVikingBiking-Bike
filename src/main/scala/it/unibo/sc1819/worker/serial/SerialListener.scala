package it.unibo.sc1819.worker.serial


import io.vertx.scala.core.Vertx
import it.unibo.sc1819.util.messages.Topic
import it.unibo.sc1819.worker.serial.messages.SerialMessage
import it.unibo.sc1819.worker.serial.messages.SerialMessage.{AQSerialMessage, CollisionSerialMessage, GPSSerialMessage, LockSerialMessage, UnlockSerialMessage}

/**
  * Simple interface that extends a SerialPortEventListener
  */
trait SerialListener {

  /**
    * Handler for received message.
    * @param message a string containing the message.
    */
  def onMessageReceived(message:String)
}

object SerialListener {

  def apply(): SerialListener = new MockSerialListener()

  private class MockSerialListener extends SerialListener {

    override def onMessageReceived(message: String): Unit = println(message)

  }

  private class SerialListenerImpl(context:Vertx) extends SerialListener {

    val eventBus = context.eventBus

    override def onMessageReceived(message: String): Unit = {
      SerialMessage(message) match {
        case GPSSerialMessage(_, value) =>  writeOnGPSChannel(value)
        case AQSerialMessage(_, value) => writeOnAPChannel(value)
        case CollisionSerialMessage(_, value) => writeOnCollisionSerialChannel(value)
        case LockSerialMessage(_, value) => writeOnLockChannel(value)
        case UnlockSerialMessage(_, value) => writeOnUnlockChannel(value)
        case _ =>
      }
    }

    private def writeOnGPSChannel(message:String):Unit = eventBus.publish(Topic.GPS_TOPIC_WORKER, message)

    private def writeOnAPChannel(message:String):Unit = eventBus.publish(Topic.AQ_TOPIC_WORKER, message)

    private def writeOnCollisionSerialChannel(message:String):Unit = eventBus.publish(Topic.COLLISION_TOPIC_WORKER, message)

    private def writeOnLockChannel(message:String):Unit = eventBus.publish(Topic.LOCK_TOPIC_WORKER, message)

    private def writeOnUnlockChannel(message:String):Unit = eventBus.publish(Topic.UNLOCK_TOPIC_WORKER, message)
  }
}
