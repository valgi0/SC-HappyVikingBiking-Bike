package it.unibo.sc1819.client.bikeclient

/**
  * Contains all the api routes
  */
object RoutesAPI {

  val GPS_REMOTE_PATH = "/api/gps"
  val AQ_REMOTE_PATH = "/api/air"
  val COLLISION_REMOTE_PATH = "/api/bikelog"
  //TODO CHANGE THIS API
  val LOCK_REMOTE_PATH = "/lockbike"
  val UNLOCK_REMOTE_PATH = "/unlockbike"
  val CONFIGURATION_REMOTE_PATH = "/api/getConfig"
  //TODO SET LOG LEVEL IN THE MESSAGE
  val LOG_NOTIFICATION_PATH = "/api/bikelog"
}
