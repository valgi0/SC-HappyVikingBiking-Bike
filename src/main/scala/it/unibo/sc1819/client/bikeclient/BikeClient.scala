package it.unibo.sc1819.client.bikeclient

import java.util.Date

import io.vertx.core.http.HttpMethod
import io.vertx.scala.core.Vertx
import it.unibo.sc1819.client.web.RequestMessage.{BikeIDMessage, ConfigurationMessage, LogMessage, JsonRequest}
import it.unibo.sc1819.client.web.WebClient
import it.unibo.sc1819.client.web._
import it.unibo.sc1819.util.messages.Topic
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.read

/**
  * A Client which has to handle all the message exchanged from the remote and the low level components
  */
trait BikeClient {

  /**
    * The bike identifier of the bike
    * @return a string containing the identifier.
    */
  def bikeID:String

  /**
    * The remote server address.
    * @return a string containing the remote server address
    */
  def remoteServer:String

  /**
    * The rack server address.
    * @return a string containing the rack server address.
    */
  def rackServer:String

  /**
    * The port on which rack server is open.
    * @return an integer for the port.
    */
  def rackPort:Int

  /**
    * The port on which the remote server is running.
    * @return an integer for the port.
    */
  def remotePort:Int

  /**
    * Start a remote call to the server to store the position
    */
  def notifyPosition(gpsData:JsonRequest):Unit

  /**
    * Start a remote call to the server to store the air quality data
    */
  def notifyAirQuality(airQualityData:JsonRequest):Unit

  /**
    * Start a remote call to the server to store the air quality data
    */
  def notifyCollision(collisionData:JsonRequest):Unit

  /**
    * Notify the remote rack of a lock.
    */
  def notifyLock():Unit

  /**
    * Fetch the user configuration for the bike from the remote server
    */
  def fetchConfiguration():Unit

}

object BikeClient {

  def apply(vertx: Vertx, bikeID: String, remoteServer: String, remotePort: Int,
            rackServer:String, rackPort:Int): BikeClient =
    new BikeClientImpl(vertx, bikeID, remoteServer, remotePort, rackServer, rackPort)

  private class BikeClientImpl(val vertxContext:Vertx, override val bikeID:String,
                               override val remoteServer:String, override val remotePort:Int,
                               override val rackServer:String, override val rackPort:Int) extends BikeClient {

    val eventBus = vertxContext.eventBus
    val webClient = WebClient(vertxContext)
    implicit val formats: DefaultFormats.type = DefaultFormats

    override def notifyPosition(gpsData: JsonRequest): Unit =
      executePOSTRemoteCall(RoutesAPI.GPS_REMOTE_PATH, failureHandler _ , Some(gpsData))

    override def notifyAirQuality(airQualityData: JsonRequest): Unit =
      executePOSTRemoteCall(RoutesAPI.AQ_REMOTE_PATH, failureHandler _ , Some(airQualityData))

    override def notifyCollision(collisionData: JsonRequest): Unit =
      executePOSTRemoteCall(RoutesAPI.COLLISION_REMOTE_PATH, failureHandler _ , Some(collisionData))

    override def notifyLock(): Unit =
      executePOSTRackCall(RoutesAPI.LOCK_REMOTE_PATH, failureHandler _ )

    override def fetchConfiguration(): Unit = {
      println("Chiamata di configurazione partita")
      executePOSTRemoteCall(RoutesAPI.CONFIGURATION_REMOTE_PATH,
        onFetchedConfigurationData, failureHandler, Some(BikeIDMessage(bikeID)))
    }

    private def executePOSTRemoteCall(path:String, onSuccess:Option[String] => Unit,
                                      onFailure: Option[String] => Unit,
                                      payLoad:Option[JsonRequest]):Unit = {
      webClient.executeAPICall(remoteServer, HttpMethod.POST, path ,remotePort,
        handlerToSuccessFailureConversion(onSuccess, onFailure), payLoad )
    }

    private def executePOSTRackCall(path:String, onSuccess:Option[String] => Unit,
                                      onFailure: Option[String] => Unit,
                                      payLoad:Option[JsonRequest]):Unit = {
      webClient.executeAPICall(rackServer, HttpMethod.POST, path ,rackPort,
        handlerToSuccessFailureConversion(onSuccess, onFailure), payLoad )
    }

    private def executePOSTRemoteCall(path:String,
                                      onFailure: Option[String] => Unit,
                                      payLoad:Option[JsonRequest]):Unit = {
      webClient.executeAPICall(remoteServer, HttpMethod.POST, path ,remotePort,
        handlerToOnlyFailureConversion(onFailure), payLoad )
    }

    private def executePOSTRackCall(path:String,
                                    onFailure: Option[String] => Unit,
                                    payLoad:Option[JsonRequest] = None):Unit = {
      webClient.executeAPICall(rackServer, HttpMethod.POST, path ,rackPort,
        handlerToOnlyFailureConversion(onFailure), payLoad )
    }

    //TODO STOP AFTER A WHILE
    private def failureHandler(errorMessage:Option[String]):Unit = errorMessage match {
      case Some(msg) => executePOSTRemoteCall(RoutesAPI.LOG_NOTIFICATION_PATH,
        failureHandler _, Some(LogMessage(bikeID, warningMessageCode,new Date().toString + ":" + msg )))
      case _ => executePOSTRemoteCall(RoutesAPI.LOG_NOTIFICATION_PATH,
        failureHandler _ , Some(LogMessage(bikeID, warningMessageCode ,new Date().toString + ":" + "ERROR CAUSE NOT SPECIFIED" )))
    }

    private def onFetchedConfigurationData(configuration:Option[String]) = {
      val lightValue = read[ConfigurationMessage](configuration.get).luce
      eventBus.publish(Topic.SETUP_TOPIC_WORKER, lightValue)
    }
  }
}
