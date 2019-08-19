

package it.unibo.sc1819.client.web

/**
  * This class is used for define the message accepted to the RouterRequest.
  *
  */

object RequestMessage {

  sealed trait JsonRequest

  case class Message(message: String) extends JsonRequest

  case class BikeIDMessage(bikeId:String) extends JsonRequest

  case class GPSMessage(lat:String, lon:String, bikeId:String, data:Long) extends JsonRequest

  case class AQMessage(air:String, lat:String, lon:String, bikeId:String, data:Long) extends JsonRequest

  case class CollisionMessage(lat:String, lon:String, bikeId:String, data:Long) extends JsonRequest

  case class LockMessage(bikeID:String) extends JsonRequest

  case class ConfigurationMessage(luce:String) extends JsonRequest

  case class Error(cause: Option[String] = None) extends JsonRequest

  case class LogMessage(bikeId:String, liv:String, messaggio:String) extends JsonRequest
}



