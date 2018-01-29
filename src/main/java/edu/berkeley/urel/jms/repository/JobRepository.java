package edu.berkeley.urel.jms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.berkeley.urel.jms.model.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
}
