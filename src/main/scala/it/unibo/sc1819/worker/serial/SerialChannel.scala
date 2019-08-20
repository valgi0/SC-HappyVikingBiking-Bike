package it.unibo.sc1819.worker.serial

import java.io.{BufferedReader, InputStreamReader}

import gnu.io.{CommPortIdentifier, SerialPort, SerialPortEvent, SerialPortEventListener}

/**
  * Simple interface for an async msg communication channel
  *
  */
trait SerialChannel {

  /**
    * The serial port on which the Channel is listening
    * @return
    */
  def serialPortPath:String

  /**
    * The arrival rate of the messages
    * @return the frequency number.
    */
  def rate:Int


  /**
    * To send an message.
    *
    * Asynchronous model.
    *
    * @param message: the string to be sent.
    */
  def sendMessage(message:String)

  /**
    * Close the serial channel
    */
  def closeSerial()

}

object SerialChannel {

  def apply(serialPortPath:String, rate:Int, listener: SerialListener): SerialChannel =
    new SerialChannelImpl(serialPortPath, rate, listener)

  private class SerialChannelImpl(override val serialPortPath:String,
                                  override val rate:Int, listener: SerialListener) extends SerialChannel with SerialPortEventListener {
    val portID = CommPortIdentifier.getPortIdentifier(serialPortPath)
    val serialPort: SerialPort = portID.open(this.getClass.getName, 2000).asInstanceOf[SerialPort]
    serialPort.setSerialPortParams(rate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE)
    val input = new BufferedReader(new InputStreamReader(serialPort.getInputStream))
    val output = serialPort.getOutputStream
    serialPort.addEventListener(this)
    serialPort.notifyOnDataAvailable(true)
    var tempMessage:String = ""


    override def sendMessage(message: String): Unit = sendAsyncMessage(message)


    override def serialEvent(serialPortEvent: SerialPortEvent): Unit = {
      if (serialPortEvent.getEventType == SerialPortEvent.DATA_AVAILABLE) try {
        val readInput = input.readLine
        //println(readInput)
        listener.onMessageReceived(readInput)
      } catch {
        case e: Exception =>
          System.err.println(e.getStackTrace)
      }
      if(serialPortEvent.getEventType == SerialPortEvent.OUTPUT_BUFFER_EMPTY) {
        if(tempMessage != "") {
          println("Sto inviando il messaggio")
          sendAsyncMessage(tempMessage)
          tempMessage = ""
        }
      }
    }

    override def closeSerial(): Unit = {
      if (serialPort != null) {
        serialPort.removeEventListener()
        serialPort.close()
      }
    }

    private def sendAsyncMessage(message: String): Unit = {
      val array = message.toCharArray
      val bytes = new Array[Byte](array.length)
      for(i <- 0 until array.length) {
        bytes(i) = array(i).toByte
      }
      try {
        output.write(bytes)
        output.flush()
      } catch {
        case ex: Exception =>
          ex.printStackTrace()
      }
    }
  }
}
