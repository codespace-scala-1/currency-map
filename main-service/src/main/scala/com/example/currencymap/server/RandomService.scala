package com.example.currencymap.server

import scala.util.Random

trait RandomService {

  def nextInt() = random.nextInt()

  def nextDouble() = random.nextDouble()

  private val seed = 1

  private val random = new Random(seed)

}
