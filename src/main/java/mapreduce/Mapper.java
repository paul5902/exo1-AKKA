package mapreduce;

import java.util.ArrayList;
import java.util.List;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.io.Tcp.Message;

public class Mapper extends AbstractActor{
	
	List<ActorRef> reducers;
	
	public Mapper(ArrayList<ActorRef> reducers) {
		this.reducers = reducers;
	}

	@Override
	public Receive createReceive() {
		
		return receiveBuilder()
				.match(String.class, this::msg1).build();
	}
	
	protected void msg1 (String msg) {
		
		String[] words = msg.toString().split("[\\p{Punct}\\s]+");
		
		for(String word : words) {
			int hashId = Math.abs(word.hashCode()%reducers.size());
			ActorRef destinationReducer = reducers.get(hashId);
			System.out.println("Mapper send word :"+word + " to reducer number "+hashId);
			destinationReducer.tell(word, this.getSelf());
		}
	}

}
