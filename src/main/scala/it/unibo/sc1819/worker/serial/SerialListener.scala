package it.unibo.sc1819.worker.serial

import gnu.io.{SerialPortEvent, SerialPortEventListener}

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
}
