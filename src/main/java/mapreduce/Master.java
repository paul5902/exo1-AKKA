package mapreduce;

import java.util.ArrayList;
import java.util.List;

import akka.actor.AbstractActor;
import akka.actor.AbstractActor.Receive;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.io.Tcp.Message;

public class Master extends AbstractActor {
	
	List<ActorSelection> mappers;
	int position;
	
	public Master(ArrayList<ActorSelection> mappers) {
		this.mappers = mappers;
		position = 0;
	}

	@Override
	public Receive createReceive() {
		
		return receiveBuilder()
				.match(String.class, this::msg1).build();
	}
	
	protected void msg1 (String msg) {
		
		String[] sentences = msg.toString().split(System.getProperty("line.separator"));
		
		for(String sentence : sentences) {
			ActorSelection destinationMapper = mappers.get(position);
			
			destinationMapper.tell(sentence, this.getSelf());
			System.out.println("Envoi de la phrase : "+sentence + " au mapper numero "+position);
			position++;
			if(position>2) { position = 0; }
		}
	}

}
