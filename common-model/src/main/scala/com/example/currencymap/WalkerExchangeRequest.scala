package com.example.currencymap


  //
  case class WalkerExchangeRequest(
                                    location: Location,
                                    radius:  Distance,
                                    currency: Currency,
                                    amount: BigDecimal,
                                    operationType: ExchangeOperationType
                                  )


