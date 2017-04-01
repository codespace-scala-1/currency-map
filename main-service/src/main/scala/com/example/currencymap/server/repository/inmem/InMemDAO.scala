package com.example.currencymap.server.repository.inmem

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

import cats.MonadError
import com.example.currencymap.server.{DAO, DBLenses}
import com.example.currencymap.server.repository.QueryDSL


class InMemDAO[M[_],K,T](
                        implicit override val mMonad: MonadError[M, Throwable],
     override val dbLenses: DBLenses[K,T])  extends DAO[M,K,T]
{

  override def select: QueryDSL.SelectExpr[T] = ???

  override def query(q: QueryDSL.TerminationExpr[T]): M[Seq[T]] = ???

  override def lookup(k: K): M[Option[T]] = {
    mMonad.pure(Option(values.get(k)))
  }

  override def create(v: T): M[T] = {
     val newId = dbLenses.idFromLong(idGenerator.incrementAndGet())
     val withId = dbLenses.setId(v,newId)
     values.put(newId,withId)
     mMonad pure withId
  }

  override def remove(v: T): M[Boolean] = {
     dbLenses.getId(v) match {
       case None => mMonad.raiseError(new IllegalArgumentException("attempt to remove unattached object"))
       case Some(id) => mMonad pure (values.remove(id) == null)
     }

  }

  override def update(v: T): M[Unit] = {
      dbLenses.getId(v) match {
        case None => mMonad.raiseError(new IllegalArgumentException("attempt to remove unattached object"))
        case Some(id) =>
            Option(values.get(id)) match {
              case None => mMonad.raiseError(new IllegalArgumentException("attempt to remove unattached object"))
              case Some(v) =>
                  values.replace(id,v)
                  mMonad pure (())
            }
      }
  }


  val idGenerator = new AtomicLong(0L)
  val values: ConcurrentHashMap[K,T] = new ConcurrentHashMap[K,T]

}
