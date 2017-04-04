package com.example.currencymap.server

import akka.actor.Props
import akka.pattern._
import akka.util.Timeout

import scala.concurrent._
import scala.concurrent.duration._

import scala.language.postfixOps

import com.example.currencymap.server.model.CepRecord
import com.example.currencymap.{CepView, DomainModel, Rate, WalkerExchangeRequest}
import repository._

trait MainSystemModule extends DomainModel {

  this: ServerDomainModel =>


  class MainSystem extends System {



    override def askCeps(exchangeRequest: WalkerExchangeRequest): M[Seq[CepView]] = {

      implicit val timeout = Timeout(1.minute)

      val replyGather = actorSystem.actorOf(Props(classOf[WalkerRequestActor]))

      val views = for {ceps <- repository.ceps.query(
            select[CepRecord](v =>
              v.within(exchangeRequest.radius) from exchangeRequest.location
            ))
           reply <- (replyGather ? ProcessRequest(exchangeRequest, ceps )).mapTo[RequestReply]
      } yield {
        reply.replies map { case (cep,rate) => CepView(cep.endpoint,cep.location,rate) }
      }

      views

    }

    override def selectBest(exchangeRequest: WalkerExchangeRequest, candidates: Seq[CepView]): M[Seq[CepView]] = {
      {
        val plain = candidates.sortBy(_.rate).take(config.nCepViewsInReply)
        mm.pure(plain)
      }

    }

  }

}
