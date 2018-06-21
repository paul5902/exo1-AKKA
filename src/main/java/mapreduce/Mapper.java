package mapreduce;

import java.util.ArrayList;
import java.util.List;
import static java.util.concurrent.TimeUnit.SECONDS;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Identify;
import akka.actor.ReceiveTimeout;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.UnreachableMember;
import akka.io.Tcp.Message;
import scala.concurrent.duration.Duration;

public class Mapper extends AbstractActor{
	
	private List<ActorRef> reducers;
	private Cluster cluster = Cluster.get(getContext().getSystem());
	
	public Mapper() {
		
	}
	public Mapper(ArrayList<ActorRef> reducers) {
		this.reducers = reducers;
	}
	
	@Override
	public void preStart() throws Exception {
		cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(),MemberEvent.class,UnreachableMember.class);
	}
	
	@Override
	public void postStop() throws Exception {
		cluster.unsubscribe(getSelf());
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
