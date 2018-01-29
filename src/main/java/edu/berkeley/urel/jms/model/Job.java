package edu.berkeley.urel.jms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Component
@Entity(name = "jobs")
@Data
@NoArgsConstructor
@ToString(exclude = "id")
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, 
fieldVisibility = JsonAutoDetect.Visibility.NONE, 
getterVisibility = JsonAutoDetect.Visibility.NONE, 
isGetterVisibility = JsonAutoDetect.Visibility.NONE, 
setterVisibility = JsonAutoDetect.Visibility.NONE) 
public class Job implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "pooled")
	@GenericGenerator(name = "pooled", strategy = "enhanced-table", parameters = {
			@Parameter(name = "value_column_name", value = "sequence_next_hi_value"),
			@Parameter(name = "prefer_entity_table_as_segment_value", value = "true"),
			@Parameter(name = "optimizer", value = "pooled-lo"), @Parameter(name = "increment_size", value = "100") })
    private Long id;
	@JsonProperty
    @Column
	private String company; //company
	@JsonProperty("forwardLink")
    @Column
	private String forward_url; //forward_page_link
	@JsonProperty("title")	
    @Column
	private String job_title;//title
	@JsonProperty("jobPosted")
    @Column
	private String posted_date;//posted_date
	@JsonProperty
    @Column
	private String location;//location
	@JsonProperty
    @Column
	private String skills;//skills
	@JsonProperty("exp")
    @Column
	private String experience;//experience
	@JsonProperty
    @Column
	private String education;//qualification
	@JsonProperty
    @Column
	private String department;//department
	@JsonProperty("jobDuration")
    @Column
	private String job_duration;//job_duration
	@JsonProperty("jobType")
    @Column
	private String job_type;//job_type
	@JsonProperty("description")
	@Column(columnDefinition="text")
	private String full_description;//content
	@JsonProperty("jobCode")
    @Column
	private String scrap_job_id;//Job id from XLSX (scrapped)
	@JsonProperty("noOfPosition")
    @Column
	private String no_of_pos;//No of position 
	@JsonProperty("checkDup")
    @Column
	private String check_dup;//No of position 
    @Column
	private String eDate;//Expired date
	@JsonProperty
    @Column
	private String emailId;//Email ID
	@JsonProperty
    @Column
	private String countryCode;//Country Code
}
