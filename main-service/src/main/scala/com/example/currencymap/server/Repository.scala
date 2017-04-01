package com.example.currencymap.server

import cats.MonadError
import com.example.currencymap.DomainModel
import com.example.currencymap.server.model.CepRecord
import com.example.currencymap.server.repository.{Query, QueryDSL}


  trait DBLenses[K,T]
  {

    def getId(obj:T): Option[K]

    def setId(obj:T,id:K): T

    def idFromLong(l:Long): K
  }


  trait DAO[M[_],K,T] {

    implicit val dbLenses: DBLenses[K,T]

    implicit val mMonad: MonadError[M,Throwable]

    def select: QueryDSL.SelectExpr[T]

    def query(q: QueryDSL.TerminationExpr[T]):M[Seq[T]] =
      query(q.query)

    def query(q: Query[T]):M[Seq[T]]

    def lookup(k:K):M[Option[T]]

    def create(v:T):M[T]

    def remove(v:T):M[Boolean]

    def update(v:T):M[Unit]

  }

  trait Repository[M[_]] {

    def ceps: DAO[M,CepRecord.Id, CepRecord]

  }

