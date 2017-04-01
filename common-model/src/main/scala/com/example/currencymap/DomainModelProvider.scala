package com.example.currencymap

import cats.MonadError


trait DomainModelProvider {

  type M[X]
  implicit val me:MonadError[M,Throwable]

}

object DomainModelProvider
{

  type Aux[T[_]] = DomainModelProvider { type M[X] = T[X]  }

}


