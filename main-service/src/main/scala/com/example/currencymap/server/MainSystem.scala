package com.example.currencymap.server

import com.example.currencymap.{DomainModel, WalkerExchangeRequest}

trait MainSystemModule extends DomainModel {

  this: ServerDomainModel =>

  class MainSystem extends System {

    override def askCeps(exchaneRequest: WalkerExchangeRequest): M[Seq[CurrencyPoint]] = {

      //actorSystem.actorOf()
      ???

      // create actor for request
      // send him message

    }

    override def selectBest(exchangeRequest: WalkerExchangeRequest, candidates: Seq[CurrencyPoint]): M[Seq[CurrencyPoint]] = ???

  }

}
