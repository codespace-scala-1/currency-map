package com.example.currencymap

import com.example.currencymap.MonadPlatform.MyOpt

import language.higherKinds

/**
  * Created by rssh on 3/30/17.
  */
object MonadPlatform {

  sealed trait MyOpt[+A]
  case class MySome[A](value:A) extends MyOpt[A]
  case object MyNone extends MyOpt[Nothing]

  trait MonadProvider[M[_]]
  {
    def pure[A](a:A):M[A]

    def bind[A,B](a:M[A])(f:A=>B):M[B]

    def flatten[A](x:M[M[A]]):M[A]
  }

  implicit object MyOptMapProvider extends MonadProvider[MyOpt]
  {


    override def bind[A, B](a: MyOpt[A])(f: (A) => B): MyOpt[B] =
      a match {
        case MyNone => MyNone
        case MySome(x) => MySome(f(x))
      }

    override def pure[A](a: A): MyOpt[A] = MySome(a)

    override def flatten[A](x: MyOpt[MyOpt[A]]): MyOpt[A] =
      x match {
        case MyNone => MyNone
        case MySome(y) => y
      }

    //def lawIdentiry[A](x:A):Boolean = {
    //  bind(pure(x))(x => x) == pure(x)
    //}


  }

  implicit class MonadSyntax[M[_],T](x:M[T])(implicit mm: MonadProvider[M])
  {
    def map[S](f:T=>S):M[S] =
       mm.bind(x)(f)

    def flatMap[S](f:T=>M[S]):M[S] =
       mm.flatten(map(f))

  }


}



trait Configuration[M[_]]
{

  import MonadPlatform._

  def get[A](name:String):M[A] = ???


  def usage(xName:String, yName:String):Int =
    {
      ???
      /*
      for{x <- get[Int](xName)
          y <- get[Int](yName)} yield x+y
          */
      /*
      get[Int](xName).flatMap(x =>
                       get[Int](yName).map(y => x+y)
                      )

                      */
    }

}

object Usage
{

  def config: Configuration[MyOpt] = ???

  def usageMap[A,B](name:String, f:A => B):MyOpt[B] = {
    for(x <- config.get(name)) yield f(x)
  }


  def usage(xName:String, yName:String):MyOpt[Int] =
  {
    for{x <- config.get[Int](xName)
        y <- config.get[Int](yName)} yield x+y
  }

}
