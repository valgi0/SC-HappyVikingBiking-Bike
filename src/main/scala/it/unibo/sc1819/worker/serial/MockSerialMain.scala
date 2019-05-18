package it.unibo.sc1819.worker.serial


object MockSerialMain extends App {

  val defaultSerialPort = "/dev/ttyS80"
  val serialBauldRate = 9600

  SerialChannel(defaultSerialPort, serialBauldRate, SerialListener())

  while(true) {

  }

}
