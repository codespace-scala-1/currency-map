
 Entities:
  
   Jonny Walker:  Person,
                  Need to sell / buy currency with best rate.
                  Situated in some place/ location.
                  // Personized ?

   Currency Exchange Point:  
                  Organization/Part of Organization.  [in UA - affilated with bank]
                  Can sell/buy currency with some fixed rate in some limit.
                  Can be open or closed.
                  Provide API

   System:
             Our Service.
             Can receive request from Walker and choose best [topN] currency exchangre point
               in some location for given request.


 Passive objects:

    Currency,
    Location [ lat, lng ]


 Use cases:

  Walker: search for best currency exchange .  [MUST]
  Registration of new cep. [SHOULD]


Details:
 Walker: search for best currency exchange - details:

   prodice  Http request from phone App, which knowns current location and sum

  System:
    Receive request from app,
    Send request to CEP in radius of [Distance] from Walker
  
  CEP:
    Reply to such requests, if they can satdificy request exchange.

  System:
    Aggregate received requests from CEPs, select N top and send back to Walker
    Exception:  no replies.

  Walker:
    Receive response.

------------------------

  

    

