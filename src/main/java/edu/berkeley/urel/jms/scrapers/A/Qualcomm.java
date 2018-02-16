package edu.berkeley.urel.jms.scrapers.A;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.remote.server.DriverFactory;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import edu.berkeley.urel.jms.common.CheckForInternetConnection;
import edu.berkeley.urel.jms.common.HtmlCleaner;
import edu.berkeley.urel.jms.model.Job;
import edu.berkeley.urel.jms.producer.ProducerApp;
import lombok.extern.log4j.Log4j;

@Log4j
public class Qualcomm {
	//https://jobs.qualcomm.com/public/search.xhtml?action=keywords&fq=WORLD:%22US%22

	// Gerh
	// private static final String URL =
	// "https://jobs.apple.com/us/search?#location&t=0&so=&pN=0"; //URL - URL of the
	// career page
	// United States and California
	// https://jobs.apple.com/us/search?#location&t=0&so=&lo=1*953*USA&pN=0
	private static final String BASEURL = "https://jobs.qualcomm.com";
	private static final String URL = "https://jobs.qualcomm.com/public/search.xhtml?action=keywords&fq=WORLD:%22US%22";
	private static final String IMAGEURL = ""; // IMAGEURL - URL of the company LOGO
	private static final String JOBTYPE = "Scrapped"; // By default JOBTYPE is set as Scrapped
	private static final String COMPANY = "Qualcomm"; // COMPANY - To store the company name

	/*
	 * scrapAndIndex() - To scrap the job details from the website and storing the
	 * data to XLSX file.
	 */

	public void scrapAndIndex(JmsTemplate jmsTemplate) throws IOException, InterruptedException, ParseException {

		// Gerh

		Capabilities chromeCapabilities = DesiredCapabilities.chrome();
		WebDriver driver = new RemoteWebDriver(new URL("http://hub:4444/wd/hub"), chromeCapabilities);

		try {
			int jobCount = 0;
			int matched_URLS_Count = 0;
			String checkdup = "N";
			String noofpos = "1";
			Integer sno = 0; // Serial Number
			Integer arrIdx = 0; // Array Index
			Map<String, Object[]> jobInfo = new LinkedHashMap<String, Object[]>(); // Map object created here

			jobInfo.put("" + (++arrIdx),
					new Object[] { "S.No", "Company", "Link of Scrapped Page", "Imageurllogo", "Title", "Posted date",
							"Expired Date", "Location", "Skills", "Experience", "Education", "Department", "Industry",
							"Type of Job", "Job Type", "Full Description", "checkdup", "Job ID", "No of positions",
							"Email ID", "Country Code" });

			log.info("Started scrapping");
			noofpos = "1";
			WebDriverWait wait = new WebDriverWait(driver, 30);
			 try{
					driver.get(URL);
					wait.until(ExpectedConditions.presenceOfElementLocated(By.className("data-table")));
				    }catch(Exception e) {
				    	while (!CheckForInternetConnection.isInternetConnected()) {
		  					Thread.sleep(3000);
		  				}
				    	driver.get(URL);
						wait.until(ExpectedConditions.presenceOfElementLocated(By.className("data-table")));
					    }
			 
				wait.until(ExpectedConditions.presenceOfElementLocated(By.className("data-table")));
				Thread.sleep(3000);
				String  pageNo = driver.findElement(By.className("pagination_info_div")).getText().trim();
				pageNo = pageNo.substring(pageNo.indexOf("of")+2,pageNo.indexOf("results")).trim();
				Integer page = Integer.parseInt(pageNo);
				if(page % 10 == 0){
					page = page / 10;
		  		}else{
		  			page = (page / 10) + 1;
		  		}
				for(int i = 0; i < page; i++) {
					System.out.println("pageNo: " + pageNo + " i: " + i);
					if(i>0){
						try{
						//driver.findElements(By.className("next")).get(0).click();
							//wait.until(ExpectedConditions.presenceOfElementLocated(By.className("data-table")));
							driver.findElements(By.className("next")).get(0).click();
							Thread.sleep(2000);
						}
						catch(Exception e){e.printStackTrace();}
						
					}
				Document document = Jsoup.parse(driver.getPageSource());
				Elements elements = document.select(".data-table").select("tbody").select("tr");
				jobCount = jobCount+elements.size();
				for (Element element : elements) {
					try{
						if(element.select("a").attr("href").isEmpty()){
							--jobCount ;
							continue ;
						}
						String jobTitle = element.select(".job-header > a").first().text().trim();
						if(jobTitle.equals(" more...")){
							--jobCount ;
							continue;
						}
						String fwd = element.select("a").attr("href").replace("../..", "");
						String forwardUrl = BASEURL + fwd ;
			
						 try{
	             			driver.get(forwardUrl);
	              			wait.until(ExpectedConditions.presenceOfElementLocated(By.className("baseTableNoBorder")));
	             			 }catch(Exception e) {
	             			while (!CheckForInternetConnection.isInternetConnected()) {
	             			 log.error("Internet is disconnected while scrapping Qualcomm.");
	             			  Thread.sleep(3000);
	             			  }
	             		     driver.get(forwardUrl);
	               			wait.until(ExpectedConditions.presenceOfElementLocated(By.className("baseTableNoBorder")));
	             		 	  }
	           				wait.until(ExpectedConditions.presenceOfElementLocated(By.className("baseTableNoBorder")));
	             		    Document doc = Jsoup.parse(driver.getPageSource());
	             		    String jobDesc = "";
							String emailId = "ogergardt@gmail.com";
							String skills = "";
							String education = "";
							String experience = "";
							String eDate = "";
							String pDate = "";
							String industry = "";
							String typeOfJob ="";
							String jobid ="";
							String countryCode = "";
							String location = "";
							String dept = "";
						
	             			jobid = doc.select(".baseTableNoBorder").select("td:matchesOwn(Job Id)").first().nextElementSibling().nextElementSibling().text().trim();
	             			try{
	             			pDate = doc.select(".baseTableNoBorder").select("td:matchesOwn(Post Date)").first().nextElementSibling().nextElementSibling().text().trim();
	             			}
	             			catch(Exception e){pDate = "";}
	             			if(pDate.equals("")){
	             				pDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
	             			}else{
	             			pDate= new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("MM/dd/yyyy").parse(pDate));	
	             			}
	             			location = doc.select(".baseTableNoBorder").select("td:matchesOwn(Location)").first().nextElementSibling().nextElementSibling().text().trim();
	             			dept = doc.select(".baseTableNoBorder").select("td:matchesOwn(Job Area)").first().nextElementSibling().nextElementSibling().text().trim();
							jobDesc = doc.select(".baseTableNoBorder").get(2).outerHtml();
							

						/*log.info("Job Title::" + jobTitle + "\nDepartment::" + dept + "\nSkills::" + skills
								+ "\nQualification::" + education + "\nExperience::" + experience + "\nLocation::"
								+ location + "\nPosted date::" + pDate + "\nExpiry date::" + eDate + "\nDescription::"
								+ jobDesc + "\nIndustry::" + industry + "\nType of Job::" + typeOfJob + "\nJob Type::"
								+ JOBTYPE + "\nNo of Positions::" + noofpos + "\nJob Id::" + jobid + "\nforwardurl::"
								+ forwardUrl);*/

						// Gerh
						
						Job job = new Job();
						job.setCompany(COMPANY);
						job.setForward_url(forwardUrl);
						job.setJob_title(HtmlCleaner.cleanHtml(jobTitle.trim()));
						job.setPosted_date(HtmlCleaner.cleanHtml(pDate.trim()));
						job.setE_date(HtmlCleaner.cleanHtml(eDate.trim()));
						job.setLocation(HtmlCleaner.cleanHtml(location.trim()));
						job.setSkills(HtmlCleaner.cleanHtml(skills.trim()));
						job.setExperience(HtmlCleaner.cleanHtml(experience.trim()));
						job.setEducation(HtmlCleaner.cleanHtml(education.trim()));
						job.setDepartment(HtmlCleaner.cleanHtml(dept.trim()));
						job.setJob_duration(HtmlCleaner.cleanHtml(typeOfJob.trim()));
						job.setJob_type(JOBTYPE);
						job.setFull_description(HtmlCleaner.cleanHtml(jobDesc.trim()));
						job.setScrap_job_id(HtmlCleaner.cleanHtml(jobid.trim()));
						job.setNo_of_pos(HtmlCleaner.cleanHtml(noofpos.trim()));
						job.setCheck_dup(HtmlCleaner.cleanHtml(checkdup.trim()));
						job.setEmailId(HtmlCleaner.cleanHtml(emailId.trim()));
						job.setCountryCode(HtmlCleaner.cleanHtml(countryCode.trim()));
						
						System.out.println(job.getJob_title());
						jmsTemplate.convertAndSend("APPLE.QUEUE", job);

					} catch (Exception e) {
						Boolean exception_Flag = true;
						String exceptionString = e.getMessage();
					}
				}
				driver.findElement(By.className("prev")).click();
			}
			
		} finally {
			driver.quit();

		}
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, ParseException {
		ConfigurableApplicationContext context = SpringApplication.run(ProducerApp.class, args);
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
		String browser = "CRM";
		Qualcomm obj = new Qualcomm();
		    obj.scrapAndIndex(jmsTemplate);
	}
}
