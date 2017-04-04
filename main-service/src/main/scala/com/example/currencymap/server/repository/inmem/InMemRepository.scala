package com.example.currencymap.server.repository.inmem

import cats.MonadError

import scala.language.higherKinds
import com.example.currencymap.server.model.CepRecord
import com.example.currencymap.server.{DAO, Repository}
import com.example.currencymap.server.repository._

class InMemRepository[M[_]](implicit mMonad: MonadError[M,Throwable])  extends  Repository[M] {

  override val ceps: DAO[M, CepRecord.Id, CepRecord] =
    new InMemDAO[M,CepRecord.Id,CepRecord]()

}
