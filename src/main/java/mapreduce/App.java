package mapreduce;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class App {

	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("mySystem");
		
		ActorRef reducerRef1 = system.actorOf(Props.create(Reducer.class));
		ActorRef reducerRef2 = system.actorOf(Props.create(Reducer.class));
		
		ArrayList<ActorRef> reducers = new ArrayList<ActorRef>();
		reducers.add(reducerRef1);
		reducers.add(reducerRef2);
		
		ActorRef mapperRef1 = system.actorOf(Props.create(Mapper.class,reducers));
		ActorRef mapperRef2 = system.actorOf(Props.create(Mapper.class,reducers));
		ActorRef mapperRef3 = system.actorOf(Props.create(Mapper.class,reducers));
		
		ArrayList<ActorRef> mappers = new ArrayList<ActorRef>();
		mappers.add(mapperRef1);
		mappers.add(mapperRef2);
		mappers.add(mapperRef3);
		
		
		ActorRef masterRef = system.actorOf(Props.create(Master.class,mappers));
		masterRef.tell("à la pêche aux moules à pêche docteur cantonais à la pêche\n aux moules à pêche docteur cantonais\n à la pêche aux moules à pêche docteur cantonais à la pêche aux moules à pêche docteur cantonais",ActorRef.noSender());
	}
}
