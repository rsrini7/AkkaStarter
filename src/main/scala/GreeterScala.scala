import akka.actor.{ ActorRef, ActorSystem, Props, Actor, Inbox }
import scala.concurrent.duration._

case object Greet
case class WhoToGreet(who: String)
case class Greeting(message: String)

class Greeter extends Actor {
  var greeting = ""

  def receive = {
    case WhoToGreet(who) => greeting = s"hello, $who"
    case Greet           => sender ! Greeting(greeting)
  }
}

object GreeterScala extends App {

  override def main(args: Array[String]) = {

    val system = ActorSystem("helloakka")
    val greeter = system.actorOf(Props[Greeter], "greeter")
    //greeter.tell(WhoToGreet("akka"), ActorRef.noSender)
    greeter ! WhoToGreet("akka")

    // Create an "actor-in-a-box"
    val inbox = Inbox.create(system)

    // Tell the 'greeter' to change its 'greeting' message
    //greeter tell WhoToGreet("akka")

    // Ask the 'greeter for the latest 'greeting'
    // Reply should go to the mailbox
    inbox.send(greeter, Greet)

    // Wait 5 seconds for the reply with the 'greeting' message
    val Greeting(message) = inbox.receive(5.seconds)
    println(s"Greeting: $message")
  }

}