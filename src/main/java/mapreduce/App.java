package mapreduce;

import java.io.File;
import java.util.ArrayList;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class App {

	public static void main(String[] args) {
		
			String configFile = App.class.getClassLoader().
				  getResource("remote_application.conf").getFile();
			
			Config config = ConfigFactory.parseFile(new File(configFile));
		
			final ActorSystem system = ActorSystem.create("ServerSystem", config);
			        
			
			ActorRef reducerRef1 = system.actorOf(Props.create(Reducer.class));
			ActorRef reducerRef2 = system.actorOf(Props.create(Reducer.class));
			
			ArrayList<ActorRef> reducers = new ArrayList<ActorRef>();
			reducers.add(reducerRef1);
			reducers.add(reducerRef2);
			
			ActorRef mapperRef1 = system.actorOf(Props.create(Mapper.class,reducers),"mapper1");
			ActorRef mapperRef2 = system.actorOf(Props.create(Mapper.class,reducers),"mapper2");
			ActorRef mapperRef3 = system.actorOf(Props.create(Mapper.class,reducers),"mapper3");
			System.out.println("Serveur démarré :)");
		
		
		
		
	}
}
