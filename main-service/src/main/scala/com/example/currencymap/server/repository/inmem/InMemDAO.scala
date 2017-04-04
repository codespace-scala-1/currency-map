package com.example.currencymap.server.repository.inmem

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import java.util.function.BiFunction

import cats.MonadError
import com.example.currencymap.Location
import com.example.currencymap.server.{DAO, DBLenses}
import com.example.currencymap.server.repository.{Near, Query, QueryDSL}


class InMemDAO[M[_],K,T](
                        implicit override val mMonad: MonadError[M, Throwable],
     override val dbLenses: DBLenses[K,T])  extends DAO[M,K,T]
{

  override def select: QueryDSL.SelectExpr[T] =
      new QueryDSL.SelectExpr[T] {}

  override def query(q: Query[T]): M[Seq[T]] = {
    def distance(a:Location,b:Location):Long = {
      val dx = (a.lat - b.lat).toDouble
      val dy = (a.lon - b.lon).toDouble
      // TODO: read correct projection
      Math.sqrt( dx*dx + dy*dy ).toLong
    }

    val predicat: T => Boolean = {
      q match {
        case Near(la, l, d) =>
               t => distance(la.location(t),l) < d
      }
    }

    val r:Seq[T] = values.reduce[Seq[T]](
      1,
      new BiFunction[K,T,Seq[T]] {
        override def apply(t: K, u: T): Seq[T] =
        {  if (predicat(u)) Seq(u) else Seq()  }
      },
      new BiFunction[Seq[T],Seq[T],Seq[T]] {
        override def apply(t: Seq[T], u: Seq[T]): Seq[T] = t ++ u
      }
    )

    mMonad pure r

  }

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
