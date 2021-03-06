package it.unibo.sc1819.worker.serial.messages

/**
  * A trait for defining all the serial messages exchanged between Low level components and high level components
  */
trait SerialMessage {

  /**
    * Define the key identifier of the message
    * @return a string containing the key.
    */
  def key:String
  /**
    * Define the value of the message send/received
    * @return a string containing the value of the message
    */
  def value:String

  /**
    * Convert the object in a serializedMessage
    * @return a string containing the serialized message.
    */
  def toSerializedMessage():String = key + SerialMessage.SEPARATOR + value
}

object SerialMessage {
  val GPS_MESSAGE_KEY = "GPS"
  val AQ_MESSAGE_KEY = "AQ"
  val COLLISION_MESSAGE_KEY = "COLL"
  val LOCK_MESSAGE_KEY  = "LOCK"
  val UNLOCK_MESSAGE_KEY = "UNLOCK"
  val DIRTY_SIGNAL_VALUE = "DONE"
  val SETUP_MESSAGE_KEY = "LIGHT"
  val SEPARATOR = ":"
  val EMPTY_VALUE = ""
  val CONF_SEPARATOR = "="
  val DEFAULT_LIGHT_CODE = "DEFAULT"
  val DEFAULT_LIGHT_VALUE = "50"

  def apply(message:String): SerialMessage = {
    if(message.contains(SEPARATOR)) {
       messageFactory(parseSerializedString(message))
    }
    else {
      if(message equals DEFAULT_LIGHT_CODE) {
        SetupSerialMessage(DEFAULT_LIGHT_VALUE)
      } else {
        SetupSerialMessage(message)
      }

    }
  }


  private def parseSerializedString (serializedMessage:String) = {
    val serialArray = serializedMessage.split(SEPARATOR)
    if(serialArray.size == 1) {
      (serialArray(0), "")
    } else {
      (serialArray(0), serialArray(1))
    }
  }

  private def messageFactory(deserializedMessage:(String, String)) = deserializedMessage match {
    case (key:String, stringValue:String) if key equals GPS_MESSAGE_KEY => GPSSerialMessage(value = stringValue)
    case (key:String, stringValue:String) if key equals  AQ_MESSAGE_KEY => AQSerialMessage(value = stringValue)
    case (key:String, stringValue:String) if key equals  COLLISION_MESSAGE_KEY => CollisionSerialMessage(value = stringValue)
    case (key:String, stringValue:String) if key equals  LOCK_MESSAGE_KEY => LockSerialMessage(value = stringValue)
    case (key:String, stringValue:String) if key contains   UNLOCK_MESSAGE_KEY => UnlockSerialMessage(value = stringValue)
    case _ => println(deserializedMessage); throw new MalformedMessageException()
  }

  /**
    * Exception to be raised when a malformed message is received
    */
  class MalformedMessageException() extends Exception


   case class GPSSerialMessage(override val key:String = GPS_MESSAGE_KEY,
                                      override val value:String = EMPTY_VALUE) extends SerialMessage

   case class AQSerialMessage(override val key:String = AQ_MESSAGE_KEY,
                                      override val value:String = EMPTY_VALUE) extends SerialMessage

   case class CollisionSerialMessage(override val key:String = COLLISION_MESSAGE_KEY,
                                      override val value:String = EMPTY_VALUE) extends SerialMessage

   case class LockSerialMessage(override val key:String = LOCK_MESSAGE_KEY,
                                      override val value:String = EMPTY_VALUE) extends SerialMessage

   case class UnlockSerialMessage(override val key:String = UNLOCK_MESSAGE_KEY,
                                      override val value:String = EMPTY_VALUE) extends SerialMessage

   case class SetupSerialMessage(override val value:String = EMPTY_VALUE,
                                 override val key:String = SETUP_MESSAGE_KEY) extends SerialMessage {
     override def toSerializedMessage(): String = key + CONF_SEPARATOR + value
   }

}



