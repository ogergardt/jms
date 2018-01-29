package edu.berkeley.urel.jms.boot;

import edu.berkeley.urel.jms.consumer.ConsumerApp;
import edu.berkeley.urel.jms.producer.ProducerApp;

public class AppStarter {

	public static void main(String args[]) throws Exception {
		if (args.length == 0) {
			System.out.println("Usage : ");
			System.out.println("java -jar target/jms-0.0.1.jar [consumer|producer]");
			return;
		}

		if (args[0].equalsIgnoreCase("consumer")) {
			ConsumerApp.main(args);
			return;
		}

		if (args[0].equalsIgnoreCase("producer")) {
			ProducerApp.main(args);
			return;
		}
	}
}
