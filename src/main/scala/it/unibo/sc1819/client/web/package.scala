package it.unibo.sc1819.client

import io.vertx.core.{AsyncResult, Handler}
import io.vertx.core.buffer.Buffer
import io.vertx.scala.ext.web.client.HttpResponse

package object web {

  val OK_CODE = 200

  def handlerToSuccessFailureConversion(onSuccess:Option[String] => Unit,
                                                 onFailure:Option[String] => Unit):
  AsyncResult[HttpResponse[Buffer]] => Unit = ar => {
    if(ar.succeeded()) {
      if(ar.result().statusCode() == OK_CODE) {
        onSuccess(ar.result().bodyAsString())
      } else {
        onFailure(ar.result().bodyAsString())
      }
    }
  }

}
