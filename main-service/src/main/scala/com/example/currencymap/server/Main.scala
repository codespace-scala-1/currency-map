package com.example.currencymap.server

import akka.actor.ActorSystem
import com.example.currencymap.server.repository.inmem.InMemRepositoryDomainModel


object Main {


  implicit val mainActorSystem:ActorSystem = ActorSystem()



  def main(args:Array[String]):Unit =
  {

    val domainModel = new ServerDomainModel
                        with MainSystemModule
                        with InMemRepositoryDomainModel
     {
      override val actorSystem: ActorSystem = implicitly[ActorSystem]
     }


    val system = new domainModel.MainSystem

  }

}
