

package it.unibo.sc1819.client.web

/**
  * This class is used for define the message accepted to the RouterRequest.
  *
  */

object RequestMessage {

  sealed trait JsonRequest

  case class Message(message: String) extends JsonRequest

  case class BikeIDMessage(bikeID:String) extends JsonRequest

  case class GPSMessage(latitude:String, longitude:String, bikeID:String) extends JsonRequest

  case class AQMessage(airQuality:String, latitude:String, longitude:String, bikeID:String) extends JsonRequest

  case class CollisionMessage(latitude:String, longitude:String, bikeID:String) extends JsonRequest

  case class LockMessage(bikeID:String) extends JsonRequest

  case class ConfigurationMessage() extends JsonRequest {
    def toBikeMessage():String = "" //TODO PARSE THE STRING
  }

  case class Error(cause: Option[String] = None) extends JsonRequest

  case class ErrorLogMessage(bikeID:String, errorMessage:String) extends JsonRequest
}



