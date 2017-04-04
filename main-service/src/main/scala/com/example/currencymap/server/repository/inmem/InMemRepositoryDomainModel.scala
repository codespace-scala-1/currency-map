package com.example.currencymap.server.repository.inmem

import akka.actor.ActorSystem
import com.example.currencymap.server.{Repository, ServerDomainModel}

import scala.concurrent.Future

trait InMemRepositoryDomainModel extends ServerDomainModel {



  override val repository: Repository[M] = new InMemRepository[M]()(this.mm)

}
