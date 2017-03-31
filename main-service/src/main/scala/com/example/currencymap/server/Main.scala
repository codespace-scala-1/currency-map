package com.example.currencymap.server

import akka.actor.ActorSystem


object Main {


  implicit val mainActorSystem:ActorSystem = ActorSystem()



  def main(args:Array[String]):Unit =
  {

    val domainModel = new ServerDomainModel
                        with MainSystemModule
     {
      override val actorSystem: ActorSystem = implicitly[ActorSystem]
     }


    val system = new domainModel.MainSystem

  }

}
