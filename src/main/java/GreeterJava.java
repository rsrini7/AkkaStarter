import java.io.Serializable;

import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class GreeterJava extends UntypedActor{

	public static class Greet implements Serializable {
	}

	public static class WhoToGreet implements Serializable {
		public final String who;

		public WhoToGreet(String who) {
			this.who = who;
		}
	}

	public static class Greeting implements Serializable {
		public final String message;

		public Greeting(String message) {
			this.message = message;
		}
	}
	
	  String greeting = "";

	    public void onReceive(Object message) {
	        if (message instanceof WhoToGreet)
	            greeting = "hello, " + ((WhoToGreet) message).who;

	        else if (message instanceof Greet)
	            getSender().tell(new Greeting(greeting), getSelf());

	        else unhandled(message);
	    }
	    
	    public static void main(String[] args) {
	    	final ActorSystem system = ActorSystem.create("helloakka");
	    	final ActorRef greeter = system.actorOf(Props.create(GreeterJava.class), "greeter");
	    	
	    	greeter.tell(new WhoToGreet("akka"), ActorRef.noSender());
	    	
	    	// Create an "actor-in-a-box"
	    	final Inbox inbox = Inbox.create(system);

	    	// Tell the 'greeter' to change its 'greeting' message
	    	//greeter.tell(new WhoToGreet("akka"), ActorRef.noSender());

	    	// Ask the 'greeter for the latest 'greeting'
	    	// Reply should go to the mailbox
	    	inbox.send(greeter, new Greet());

	    	// Wait 5 seconds for the reply with the 'greeting' message
	    	Greeting greeting = (Greeting) inbox.receive(Duration.create(5, "seconds"));
	    	System.out.println("Greeting: " + greeting.message);
		}

}