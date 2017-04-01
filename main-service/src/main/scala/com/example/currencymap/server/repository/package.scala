package com.example.currencymap.server

import com.example.currencymap.Location
import com.example.currencymap.server.model.CepRecord
import com.example.currencymap.server.model.CepRecord.Id

package object repository {

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
  }

}
