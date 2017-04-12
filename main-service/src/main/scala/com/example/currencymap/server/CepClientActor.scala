package com.example.currencymap.server

import akka.actor.{Actor, ActorContext, ActorRef, ActorRefFactory, Props}
import akka.routing.RoundRobinPool

/**
  * Workf of cepActor is send request ot endppiit and receove re[plu
  */
class CepClientActor(randomService: RandomService) extends Actor {

  override def receive: Receive = {
    case CepRequest(cep) =>
      System.err.println("receoive request")
      val endpoint = cep.endpoint
      // TODO: call endpoint
      val rate = 27.02
      if (randomService.nextDouble() < 0.9) {
        sender() ! CepReply(cep,Some(rate+randomService.nextDouble()))
      }
  }

}

object CepClientActor
{

  def props(randomService: RandomService):Props= {
    Props(classOf[CepClientActor], randomService)
  }

  def createAll(factory: ActorRefFactory, nInstances:Int, randomService: RandomService):ActorRef =
  {
    factory.actorOf(
      RoundRobinPool(nInstances).props(props(randomService)), "ceps")
  }

}

