

package it.unibo.sc1819.client.web

/**
  * This class is used for define the message accepted to the RouterRequest.
  *
  */

object RequestMessage {

  sealed trait JsonRequest

  case class Message(message: String) extends JsonRequest

  case class BikeIDMessage(bikeID:String) extends JsonRequest

  case class Error(cause: Option[String] = None) extends JsonRequest
}



