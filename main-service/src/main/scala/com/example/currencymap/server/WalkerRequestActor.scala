package com.example.currencymap.server

import akka.actor.{Actor, ActorRef, ActorSelection}
import com.example.currencymap.server.model.CepRecord
import com.example.currencymap.{Rate, WalkerExchangeRequest}

import scala.concurrent.Promise
import scala.concurrent.duration._

sealed trait ActorMessages

case class ProcessRequest(request:WalkerExchangeRequest,
                           ceps:Seq[CepRecord]) extends ActorMessages

case class CepRequest(cep:CepRecord) extends ActorMessages

case class CepReply(cep:CepRecord,
                    value:Option[Rate]) extends ActorMessages
case object RequestTimeout extends ActorMessages

case class RequestReply(replies: Seq[(CepRecord,Rate)])


/**
  * Started for each request from walker
  */
class WalkerRequestActor(/* TODO: config:Configuration*/) extends Actor
{

  var request: WalkerExchangeRequest = _
  var ceps: Seq[CepRecord] = Seq()
  var whereToReply: ActorRef = _


  var replies = Seq[CepReply]()
  var nReceivedReplies = 0
  var nCeps = 0

  val overallTimeout: FiniteDuration = 1.minute
  val cepAskActorSelection = context.actorSelection("/user/ceps")

  implicit val ec = context.dispatcher

  override def receive: Receive = {
    case ProcessRequest(request,ceps) =>
      System.err.println("ProcessRequest")
      this.request = request
      this.ceps = ceps
      nCeps = ceps.size
      whereToReply = context.sender()
      context.system.scheduler.scheduleOnce(overallTimeout,self,RequestTimeout)
      for( cep <- ceps) {
        cepAskActorSelection ! CepRequest(cep)
      }
      context.become(process)
  }

  def process: Receive =
  {
    case reply@CepReply(cep,value) =>
      nReceivedReplies += 1
      value.foreach { _ =>
        replies = reply +: replies
      }
      if (nReceivedReplies == nCeps) {
          whereToReply ! RequestReply(repliesWithRates)
          context.stop(self)
      }
    case RequestTimeout =>
      whereToReply ! RequestReply(repliesWithRates)
      context.stop(self)
  }

  def repliesWithRates: Seq[(CepRecord,Rate)] =
  {
    replies.filter(_.value.isDefined).map(x => (x.cep, x.value.get) )
  }

}
