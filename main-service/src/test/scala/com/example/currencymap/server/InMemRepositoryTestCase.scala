package com.example.currencymap.server

import com.example.currencymap.server.repository.inmem.InMemRepository
import org.scalatest.FunSuite

import scala.util.Try
import cats.instances.try_._
import com.example.currencymap.Location
import com.example.currencymap.server.model.CepRecord


class InMemRepositoryTestCase extends FunSuite {

  test("We shoudl be able do select in InMemRepsotiry") {

    val repository = new InMemRepository[Try]
    repository.ceps.create(CepRecord(None,"r1",Location(50,30)))
    repository.ceps.create(CepRecord(None,"r1",Location(51,30)))
    repository.ceps.create(CepRecord(None,"r1",Location(52,30)))
    repository.ceps.create(CepRecord(None,"r1",Location(53,30)))

    val ceps = repository.ceps.select within(2) from Location(50,30)

    System.err.println(s"ceps=$ceps")


  }

}
