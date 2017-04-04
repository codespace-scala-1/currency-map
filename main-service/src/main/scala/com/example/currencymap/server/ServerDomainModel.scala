package com.example.currencymap.server

import akka.actor.ActorSystem
import cats.MonadError
import com.example.currencymap.DomainModel

import scala.concurrent.{ExecutionContext, Future}

trait ServerDomainModel extends DomainModel {

  override type M[X] = Future[X]

  val actorSystem: ActorSystem

  val repository: Repository[M]

  val config: Configuration = DefaultConfiguration

  implicit val ec: ExecutionContext = actorSystem.dispatcher

  override val mm:MonadError[Future,Throwable] = cats.instances.future.catsStdInstancesForFuture

}
