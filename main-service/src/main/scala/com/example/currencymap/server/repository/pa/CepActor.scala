package com.example.currencymap.server.repository.pa

import akka.persistence.{PersistentActor, SnapshotOffer}
import com.example.currencymap.Location
import com.example.currencymap.server.model.CepRecord

sealed trait CepOperation

sealed trait CepChangeOperation extends CepOperation
case class ChangeEndpoint(endpoint: String) extends CepChangeOperation
case class ChangeLocation(newLocation: Location) extends CepChangeOperation

case object GenSnapshot extends CepOperation

case object TestAviability extends CepOperation

class CepActor(val id:Long, initState: CepRecord) extends PersistentActor {

  var state: CepRecord = initState.copy(id = Some(CepRecord.Id(id)))


  override def persistenceId: String = id.toString

  override def receiveCommand: Receive =
  {
    case ev:CepChangeOperation =>
             persist(ev)(updateState)
             context.system.eventStream.publish(ev)
    case TestAviability =>
             Console.println("aviability")
    case GenSnapshot =>
             saveSnapshot(state)
  }

  def updateState(change:CepChangeOperation):Unit =
  {
   change match {
     case ev@ChangeEndpoint(newEndpoint) =>
       state = state.copy(endpoint = newEndpoint)
     case ev@ChangeLocation(newLocation) =>
       state = state.copy(location = newLocation)
   }
  }

  override def receiveRecover: Receive = {
    case ev:CepChangeOperation =>
      updateState(ev)
    case SnapshotOffer(_, snapshot) =>
      state = snapshot.asInstanceOf[CepRecord]
  }



}
