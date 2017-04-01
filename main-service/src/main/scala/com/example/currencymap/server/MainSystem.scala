package com.example.currencymap.server

import akka.actor.Props
import com.example.currencymap.{DomainModel, WalkerExchangeRequest}

trait MainSystemModule extends DomainModel {

  this: ServerDomainModel =>

  class MainSystem extends System {


    override def askCeps(exchaneRequest: WalkerExchangeRequest): M[Seq[CurrencyPoint]] = {

???
      /*

      val replyGather = actorSystem.actorOf(Props(classOf[WalkerRequestActor])
      replyGather ? ProcessRequest(exchaneRequest, endpoints)

      // create actor for request
      // send him message
      */

    }

    override def selectBest(exchangeRequest: WalkerExchangeRequest, candidates: Seq[CurrencyPoint]): M[Seq[CurrencyPoint]] = ???

  }

}
