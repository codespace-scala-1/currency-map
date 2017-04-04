package com.example.currencymap.server

trait Configuration {

  def nCepViewsInReply: Int

}

object DefaultConfiguration extends Configuration
{

  def nCepViewsInReply: Int = 5


}
