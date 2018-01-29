package edu.berkeley.urel.jms.consumer;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import edu.berkeley.urel.jms.common.MessageConverters;

@EnableJpaRepositories(basePackages = {"edu.berkeley.urel.jms.repository"})
@EntityScan(value = "edu.berkeley.urel.jms.model")
@SpringBootApplication(scanBasePackages = {"edu.berkeley.urel.jms.consumer"})
@EnableJms
public class ConsumerApp {
	
    @Autowired
    private MessageErrorHandler messageErrorHandler;

	ConnectionFactory connectionFactory() {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.99.101:61616");
		return connectionFactory;
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setErrorHandler(messageErrorHandler);
		factory.setConnectionFactory(connectionFactory());
		factory.setDestinationResolver(new DynamicDestinationResolver());
		factory.setMessageConverter(MessageConverters.defaultMessageConverter());
		factory.setConcurrency("3-10");
		return factory;
	}
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ConsumerApp.class, args);
	}
}
