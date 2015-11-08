import akka.actor.{ ActorRef, ActorSystem, Props, Actor, Inbox }
import scala.concurrent.duration._

case object Greet
case class WhoToGreet(who: String)
case class Greeting(message: String)

class GreetPrinter extends Actor {
  def receive = {
    case Greeting(message) => println(message)
  }
}

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
    greeter ! WhoToGreet(" srini ")

    // Ask the 'greeter for the latest 'greeting'
    // Reply should go to the mailbox
    inbox.send(greeter, Greet)

    // Wait 5 seconds for the reply with the 'greeting' message
    val Greeting(message) = inbox.receive(5.seconds)
    println(s"Greeting: $message")

    // Change the greeting and ask for it again
    greeter.tell(WhoToGreet("typesafe"), ActorRef.noSender)
    inbox.send(greeter, Greet)
    val Greeting(message2) = inbox.receive(5.seconds)
    println(s"Greeting: $message2")

    val greetPrinter = system.actorOf(Props[GreetPrinter])
    // after zero seconds, send a Greet message every second to the greeter with a sender of the greetPrinter
    system.scheduler.schedule(0.seconds, 1.second, greeter, Greet)(system.dispatcher, greetPrinter)

  }

}