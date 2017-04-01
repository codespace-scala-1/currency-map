package com.example.currencymap.server.repository

import com.example.currencymap.server.model.CepRecord
import com.example.currencymap.{Distance, Location}

sealed trait Query[T]

trait Locationable[T]
{
  def location(t:T):Location
}

object QueryDSL
{

  trait TerminationExpr[T]
  {
    def query: Query[T]
  }

  class SelectExpr[T]
  {
    def within(d:Distance)(implicit lt: Locationable[T]) = new WithinExpr[T](this,d)
  }

  case class WithinExpr[T:Locationable](select:SelectExpr[T],distance:Distance) {
    def from(l:Location) = new WithinFromExpr(select,distance,l)
  }

  case class WithinFromExpr[T:Locationable](select:SelectExpr[T],distance:Distance, location:Location) extends TerminationExpr[T]
  {
    override def query: Query[T] = Near(location,distance)
  }


  // select cep within <distance> from <request.location>

}

case class Near[T:Locationable](x:Location, d:Distance) extends Query[T]
