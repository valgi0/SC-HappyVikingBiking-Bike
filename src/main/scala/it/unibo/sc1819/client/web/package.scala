package it.unibo.sc1819.client

import io.vertx.core.{AsyncResult, Handler}
import io.vertx.core.buffer.Buffer
import io.vertx.scala.ext.web.client.HttpResponse

package object web {

  val OK_CODE = 200

  /**
    * A converter that merges together two handlers
    * @param onSuccess the handler if the status code is 200
    * @param onFailure the handler for every other situation.
    * @return a AsyncResult => Unit method
    */
  def handlerToSuccessFailureConversion(onSuccess:Option[String] => Unit,
                                                 onFailure:Option[String] => Unit):
  AsyncResult[HttpResponse[Buffer]] => Unit = ar => {
    if(ar.succeeded()) {
      if(ar.result().statusCode() == OK_CODE) {
        onSuccess(ar.result().bodyAsString())
      } else {
        println(ar.result().statusCode())
        println(ar.result().bodyAsString())
        onFailure(ar.result().bodyAsString())
      }
    } else {
      onFailure(Some(ar.cause().getMessage))
    }
  }

  def handlerToOnlyFailureConversion(onFailure:Option[String] => Unit):
  AsyncResult[HttpResponse[Buffer]] => Unit = ar => {
    if(ar.succeeded()) {
      if(ar.result().statusCode() != OK_CODE) {
        onFailure(ar.result().bodyAsString())
        println(ar.result().statusCode())
        println(ar.result().bodyAsString())
      }
    } else {
      onFailure(Some(ar.cause().getMessage))
    }
  }

  val infoMessageCode = "INFO"
  val warningMessageCode = "WARNING"
  val errorMessageCode = "ERROR"
  val crashMessageCode = "CRASH"

}
