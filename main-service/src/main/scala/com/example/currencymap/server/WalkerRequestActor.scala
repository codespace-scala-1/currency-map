package com.example.currencymap.server

import akka.actor.Actor
import com.example.currencymap.{Rate, WalkerExchangeRequest}

import scala.concurrent.Promise

sealed trait ActorMessages

case class ProcessRequest(request:WalkerExchangeRequest,
                           endpoints:Seq[String]) extends ActorMessages

case class CepReply(endvpoint:String,
                    value:Option[Rate]) extends ActorMessages
case object RequestTimeout extends ActorMessages

/**
  * Started for each request from walker
  */
class WalkerRequestActor extends Actor
{

  var request: WalkerExchangeRequest = _
  var endpoints: Seq[String] = Seq()

  var replies = Seq[CepReply]()
  var nReceivedReplies = 0
  var nCeps = 0



  override def receive: Receive = {
    case ProcessRequest(request,endpoints) =>
      this.request = request
      this.endpoints = endpoints
      nCeps = endpoints.size
      context.become(process)
  }

  def process: Receive =
  {
    case reply@CepReply(endpoint,value) =>
      nReceivedReplies += 1
      value.foreach { _ =>
        replies = reply +: replies
      }
      if (nReceivedReplies == nCeps) {
          ???
      }

  }

}
