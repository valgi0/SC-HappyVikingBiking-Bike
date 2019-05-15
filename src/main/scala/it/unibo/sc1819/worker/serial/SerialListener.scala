package it.unibo.sc1819.worker.serial

import gnu.io.SerialPortEventListener

/**
  * Simple interface that extends a SerialPortEventListener
  */
trait SerialListener extends SerialPortEventListener {

  /**
    * Handler for received message.
    * @param message a string containing the message.
    */
  def onMessageReceived(message:String)
}
