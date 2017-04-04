package com.example.currencymap.server

import com.example.currencymap.Location
import com.example.currencymap.server.model.CepRecord
import com.example.currencymap.server.model.CepRecord.Id
import com.example.currencymap.server.repository.QueryDSL.SelectExpr

package object repository {

  object select {
    import QueryDSL._
    def apply[T](f: SelectExpr[T] => TerminationExpr[T]): TerminationExpr[T] = {
      f(new SelectExpr[T])
    }
  }

  implicit object CepLocationable extends Locationable[CepRecord]
  {
    override def location(t: CepRecord): Location = t.location
  }

  implicit object CepDBLenses extends DBLenses[CepRecord.Id,CepRecord]
  {

    override def getId(obj: CepRecord): Option[CepRecord.Id] = obj.id

    override def setId(obj: CepRecord, id: CepRecord.Id): CepRecord =
       obj.copy(id = Some(id))

    override def idFromLong(l: Long): Id = new CepRecord.Id(l)


   // def endpoint = Field("endpoint",_.endpoing, _.copy(endpoiont=_))

  }

}
