package com.example.currencymap.server

trait TimeService {

  def now(): Long

}


object DefaultTimeService extends TimeService
{
  override def now(): Long = System.currentTimeMillis()
}