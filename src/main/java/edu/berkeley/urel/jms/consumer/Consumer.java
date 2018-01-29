package edu.berkeley.urel.jms.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import edu.berkeley.urel.jms.model.Job;
import edu.berkeley.urel.jms.repository.JobRepository;

@Component
class Consumer {

	private JobRepository jobRepository;

	@Autowired
	public void setJobRepository(JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}
	
	@JmsListener(destination = "APPLE.QUEUE")
	void receive(Job job) {
		jobRepository.save(job);
	}
}
