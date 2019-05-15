package it.unibo.sc1819.worker.serial

import gnu.io.{SerialPortEvent, SerialPortEventListener}

object MockSerialMain extends App {


}

class MockSerialListener extends SerialPortEventListener {
  override def serialEvent(serialPortEvent: SerialPortEvent): Unit = {
    if(serialPortEvent.getEventType == SerialPortEvent.DATA_AVAILABLE) {
      println()
    }
  }
}