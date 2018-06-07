package mapreduce;

import java.util.HashMap;

import akka.actor.AbstractActor;
import akka.actor.AbstractActor.Receive;
import akka.io.Tcp.Message;

public class Reducer extends AbstractActor{
	
	HashMap words;
	
	public Reducer() {
		this.words = new HashMap<String,Integer>();
	}

	@Override
	public Receive createReceive() {
		
		return receiveBuilder()
				.match(String.class, this::msg1).build();
	}
	
	protected void msg1 (String msg) {
		if(words.containsKey(msg) == true) {
			words.replace(msg, (Integer)words.get(msg)+1);
		}
		else {
			words.put(msg, 1);
		}
	}

}
