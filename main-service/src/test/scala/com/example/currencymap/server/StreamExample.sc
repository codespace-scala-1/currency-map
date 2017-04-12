
import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._

implicit val actorSystem = ActorSystem()
implicit val actorMaterializer = ActorMaterializer()

val source: Source[Int, NotUsed] = Source(1 to 100)

source.scan()

val sink = Sink.foreach[Int](System.out.print(_))

source.runWith(sink)