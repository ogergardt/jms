package edu.berkeley.urel.jms.producer;

import java.lang.reflect.Method;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;

import edu.berkeley.urel.jms.common.DriverFactory;
import edu.berkeley.urel.jms.common.MessageConverters;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class ProducerApp implements CommandLineRunner {

	ConnectionFactory connectionFactory() {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.99.101:61616");
		return connectionFactory;
	}

	@Bean
	public JmsTemplate jmsTemplate() {
		JmsTemplate template = new JmsTemplate(connectionFactory());
		template.setMessageConverter(MessageConverters.defaultMessageConverter());
		return template;
	}

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext context = SpringApplication.run(ProducerApp.class, args);
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
		String browser = "CRM";
		DriverFactory.start(browser);

		String arr = "A";
		for (String fname : new String[] {"Apple"}) {
		String classFullName = "edu.berkeley.urel.jms.scrapers." + arr + "." + fname.replace(".class", "");
		try {
			Class<?> cl = Class.forName(classFullName);
			Class<?>[] argTypes = new Class[] { JmsTemplate.class };
			Object obj = cl.newInstance();
			Method method = cl.getMethod("scrapAndIndex", argTypes);
			method.invoke(obj, (Object) jmsTemplate);
		} catch (Exception e) {

		}
		}
	}

	public void run(String... args) throws Exception {
	}
}
