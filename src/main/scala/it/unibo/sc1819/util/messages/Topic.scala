package it.unibo.sc1819.util.messages

/**
  * Topics for the message exchange between verticles
  */
object Topic {
  val GPS_TOPIC_WORKER = "gps_worker"
  val GPS_TOPIC_WEB = "gps_web"

  val AQ_TOPIC_WORKER = "aq_worker"
  val AQ_TOPIC_WEB = "aq_web"

  val COLLISION_TOPIC_WORKER = "collision_worker"
  val COLLISION_TOPIC_WEB = "collision_web"

  val LOCK_TOPIC_WORKER = "lock_worker"
  val LOCK_TOPIC_WEB = "lock_web"

  val UNLOCK_TOPIC_WORKER = "unlock_worker"
  val UNLOCK_TOPIC_WEB = "unlock_web"

  val SETUP_TOPIC_WORKER = "gps_worker"
}
