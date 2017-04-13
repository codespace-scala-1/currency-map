package com.example.currencymap.server.model

import com.example.currencymap.Location
import com.example.currencymap.server.repository.Locationable

/**
  * Represemtation of CEP in our DB
  */
case class CepRecord(id:Option[CepRecord.Id],
                     endpoint:String,
                     location:Location)

object CepRecord
{

  type Id = IdType[Long,CepRecord]

  def Id(v:Long) = new Id(v)

  //Phantom types:
  //type @@[A,B] = A { type Tag = B }
  //type Id = Long @@ CepRecord


}
