package mapreduce;

import java.io.Console;
import java.io.File;
import java.util.ArrayList;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import scala.io.StdIn;

public class App2 {
	
	public static void main(String[] args) {
		String configFile = App2.class.getClassLoader().
				  getResource("local-configuration.conf").getFile();
			
		Config config = ConfigFactory.parseFile(new File(configFile));
		
		final ActorSystem system = ActorSystem.create("ServerSystem", config);
		
		String url1 = "akka.tcp://ServerSystem@127.0.0.1:5150/user/mapper1";
		String url2 = "akka.tcp://ServerSystem@127.0.0.1:5150/user/mapper2";
		String url3 = "akka.tcp://ServerSystem@127.0.0.1:5150/user/mapper3";
		
		
		ActorSelection refMap1 = system.actorSelection(url1);
		ActorSelection refMap2 = system.actorSelection(url2);
		ActorSelection refMap3 = system.actorSelection(url3);

		
		ArrayList<ActorSelection> mappers = new ArrayList<>();
		mappers.add(refMap1);
		mappers.add(refMap2);
		mappers.add(refMap3);
		
		
		ActorRef masterRef = system.actorOf(Props.create(Master.class,mappers));
		masterRef.tell("à la pêche aux moules à pêche docteur cantonais à la pêche\n aux moules à pêche docteur cantonais\n à la pêche\n aux moules à pêche docteur cantonais à la pêche aux moules à pêche docteur cantonais",ActorRef.noSender());
	}

}
