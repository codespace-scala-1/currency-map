package com.example.currencymap

import scala.concurrent.{ExecutionContext, Future}

import cats._
import cats.syntax.all._
import scala.language.higherKinds

sealed trait ExchangeOperationType
object ExchangeOperationType
{
  case object Sell extends ExchangeOperationType
  case object Buy extends ExchangeOperationType
}

import com.example.currencymap._

case class CepRequest(currency: Currency,
                      amount: BigDecimal,
                      operationType: ExchangeOperationType)

case class Location(lat:BigDecimal,lon:BigDecimal)

case class CepView(name:String,location:Location,rate:Rate)

trait DomainModel {

  // We have no generic monad laws.

  type M[X]
  implicit val mm:MonadError[M,Throwable]



  trait Person
  {
    def createRequest(currency: Currency,
                      amount:BigDecimal,
                      operationType: ExchangeOperationType): M[WalkerExchangeRequest] =
    {
      if (amount <= 0) {
        mm.raiseError(new IllegalArgumentException("amount must be > 0"))
      }
      mm.pure(
        WalkerExchangeRequest(location(),
          allowedRadius(),
          currency,
          amount,
          operationType)
      )
    }


    // ???
    def location(): Location

    def allowedRadius(): Distance
  }



  trait CurrencyPoint
  {

    def endpoint: String

    def handleRequest(rq: CepRequest): M[Option[Rate]]

    def location: Location

  }



  trait System
  {


    //def handleWalkerRequest(rq:WalkerExchangeRequest):M[Seq[CurrencyPoint]] =


    def handleWalkerRequest(rq:WalkerExchangeRequest):M[Seq[CepView]] =
    {
      for{ceps <- askCeps(rq)
          best <- selectBest(rq,ceps)
      } yield best
    }


    def askCeps(exchaneRequest: WalkerExchangeRequest):M[Seq[CepView]]


    def selectBest(exchangeRequest: WalkerExchangeRequest, candidates: Seq[CepView]): M[Seq[CepView]]


  }




  //  Walker: search for best currency exchange

  // produce  Http request from phone App, which knowns current location and sum




}
