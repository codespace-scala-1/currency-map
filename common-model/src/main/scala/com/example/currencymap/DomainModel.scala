package com.example.currencymap

import scala.concurrent.{ExecutionContext, Future}


trait DomainModel {

  // We have no generic monad laws.

  type M[X] = Future[X]



  trait Person
  {
    def createRequest(currency: Currency,
                      amount:BigDecimal,
                      operationType: OperationType): M[WalkerExchangeRequest]
    // ???
    def location(): Location

    def allowedRadius(): Distance
  }



  trait CurrencyPoint
  {

    def handleRequest(rq: CepRequest): M[Option[Rate]]

  }



  trait System
  {

    def internalExecutionContext: ExecutionContext

    def handleWalkerRequest(rq:WalkerExchangeRequest):M[Seq[CurrencyPoint]] =
    {
      implicit val ec = internalExecutionContext
      for{ceps <- askCeps(rq)
          best <- selectBest(rq,ceps)
      } yield best
    }


    def askCeps(exchaneRequest: WalkerExchangeRequest):M[Seq[CurrencyPoint]]


    def selectBest(exchangeRequest: WalkerExchangeRequest, candidates: Seq[CurrencyPoint]): M[Seq[CurrencyPoint]]


  }

  type Rate = BigDecimal

  /**
    * Distance in meters
    */
  type Distance = Int

  case class Location(lat:BigDecimal,lon:BigDecimal)

  type Currency = Symbol

  //  Walker: search for best currency exchange

  // produce  Http request from phone App, which knowns current location and sum
  sealed trait OperationType
  object OperationType
  {
    case object Sell extends OperationType
    case object Buy extends OperationType
  }

  //
  case class WalkerExchangeRequest(
                              location: Location,
                              radius:  Distance,
                              currency: Currency,
                              amount: BigDecimal,
                              operationType: OperationType
                            )

  case class CepRequest(currency: Currency,
                        amount: BigDecimal,
                        operationType: OperationType)



}
