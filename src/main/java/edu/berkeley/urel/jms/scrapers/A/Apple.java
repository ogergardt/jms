package edu.berkeley.urel.jms.scrapers.A;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.URL;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import edu.berkeley.urel.jms.common.CheckForInternetConnection;
import edu.berkeley.urel.jms.common.HtmlCleaner;
import edu.berkeley.urel.jms.common.ScraperUtil;
import edu.berkeley.urel.jms.model.Job;
import edu.berkeley.urel.jms.producer.ProducerApp;
import lombok.extern.log4j.Log4j;
@Log4j
public class Apple {

	// Gerh
	// private static final String URL =
	// "https://jobs.apple.com/us/search?#location&t=0&so=&pN=0"; //URL - URL of the
	// career page
	// United States and California
	// https://jobs.apple.com/us/search?#location&t=0&so=&lo=1*953*USA&pN=0
	private static final String URL = "https://jobs.apple.com/us/search?#location&t=0&so=&lo=1*953*USA&pN=0";
	private static final String IMAGEURL = ""; // IMAGEURL - URL of the company LOGO
	private static final String JOBTYPE = "Scrapped"; // By default JOBTYPE is set as Scrapped
	private static final String COMPANY = "Apple"; // COMPANY - To store the company name

	/*
	 * scrapAndIndex() - To scrap the job details from the website and storing the
	 * data to XLSX file.
	 */

	public void scrapAndIndex(JmsTemplate jmsTemplate) throws IOException, InterruptedException, ParseException {

		// Gerh
		/*
		 * try { ds = DataSource.getInstance(); con = ds.getConnection(); } catch
		 * (Exception e2) { e2.printStackTrace(); }
		 */
/*		Capabilities chromeCapabilities = DesiredCapabilities.chrome();
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
			try {
				driver.get(URL);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.id("jobs_list")));
			} catch (Exception e) {
				while (!CheckForInternetConnection.isInternetConnected()) {
					log.error(
							"Internet is disconnected while scrapping Apple");
					Thread.sleep(3000);
				}
				driver.get(URL);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.id("jobs_list")));
			}

			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("jobs_list")));
			Thread.sleep(3000);
			Integer pageNo = Integer
					.parseInt(driver.findElement(By.className("job_count")).getText().replaceAll("\\D+", "").trim());
			if (pageNo % 10 == 0) {
				pageNo = pageNo / 10;
			} else {
				pageNo = (pageNo / 10) + 1;
			}
			for (int i = 0; i < pageNo; i++) {
				System.out.println("pageNo: " + pageNo + " i: " + i);
				if (i > 0) {
					// Gerh
					// String url = "https://jobs.apple.com/us/search?#location&t=0&so=&pN="+i+"";
					String url = "https://jobs.apple.com/us/search?#location&t=0&so=&lo=1*953*USA&pN=" + i + "";
					driver.get(url);

					Thread.sleep(7000);
				}
				Document doc = Jsoup.parse(driver.getPageSource());
				Elements elements = doc.select("#jobs_list").select("tbody").select("tr.searchresult");
				jobCount = jobCount + elements.size();
				for (Element element : elements) {
					try {
						if (element.select("a").isEmpty()) {
							--jobCount;
							continue;
						}
						String val = element.attr("onclick");
						val = val.substring(val.indexOf("(") + 2, val.indexOf(")") - 1).trim();
						String id = val.split("-")[1];
						String forwardUrl = "https://jobs.apple.com/us/search?job=" + id + "&openJobId=" + id + "#&openJobId="
								+ id;
						if (forwardUrl.contains("javascript:void(0)")) {
							--jobCount;
							continue;
						}

						String jobTitle = element.select(".title").text().trim();
						String dept = element.select(".detail").first().text().trim();
						String location = element.select(".detail").get(1).text().trim();
						String pDate = element.select(".detail").get(2).text().trim();
						pDate = new SimpleDateFormat("dd/MM/yyyy")
								.format(new SimpleDateFormat("MMM. dd, yyyy").parse(pDate));

						// Gerh
						++matched_URLS_Count;
						
						 * if(URLUploader.isURLAlreadyScrap_CheckWithTitle_N(con,forwardUrl, jobTitle,
						 * checkdup)){ ++matched_URLS_Count; continue; }
						 

						try {
							driver.get(forwardUrl);
							wait.until(ExpectedConditions.presenceOfElementLocated(By.id("job")));
						} catch (Exception e) {
							while (!CheckForInternetConnection.isInternetConnected()) {
								log.error(
										"Internet is disconnected while scrapping Apple.");
								Thread.sleep(3000);
							}
							driver.get(forwardUrl);
							wait.until(ExpectedConditions.presenceOfElementLocated(By.id("job")));
						}
						wait.until(ExpectedConditions.presenceOfElementLocated(By.id("job")));
						Document docum = Jsoup.parse(driver.getPageSource());
						String jobDesc = docum.select("#job").html();
						String emailId = "ogergardt@gmail.com";
						String skills = "";
						String education = "";
						String experience = "";
						String eDate = "";
						String industry = "";
						String typeOfJob ="";
						String jobid ="";
						String countryCode = "";

						log.info("Job Title::" + jobTitle + "\nDepartment::" + dept + "\nSkills::" + skills
								+ "\nQualification::" + education + "\nExperience::" + experience + "\nLocation::"
								+ location + "\nPosted date::" + pDate + "\nExpiry date::" + eDate + "\nDescription::"
								+ jobDesc + "\nIndustry::" + industry + "\nType of Job::" + typeOfJob + "\nJob Type::"
								+ JOBTYPE + "\nNo of Positions::" + noofpos + "\nJob Id::" + jobid + "\nforwardurl::"
								+ forwardUrl);

						*//**
						 * Rows of XLSX file is updated here
						 *//*

						if (location.isEmpty()) {
							ScraperUtil scobj = new ScraperUtil();
							location = scobj.locationFieldScrapWithBigList(jobTitle);
							if (location.isEmpty()) {
								location = scobj.locationFieldScrapWithBigList(jobDesc);
							}
						} else if (location.contains("~;~;~")) {
							String strLoc = location;
							ScraperUtil scobj = new ScraperUtil();
							location = scobj.locationFieldScrapWithBigList(jobTitle);
							if (location.isEmpty()) {
								location = scobj.locationFieldScrapWithBigList(jobDesc);
							}
							if (location.isEmpty()) {
								location = strLoc;
							}
						}
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

						jmsTemplate.convertAndSend("APPLE.QUEUE", job);

						jobInfo.put("" + (++arrIdx),
								new Object[] { "" + (++sno), COMPANY, forwardUrl, IMAGEURL, jobTitle, pDate, eDate,
										location, skills, experience, education, dept, industry, typeOfJob, JOBTYPE,
										jobDesc, checkdup, jobid, noofpos, emailId, countryCode });
					} catch (Exception e) {
						Boolean exception_Flag = true;
						String exceptionString = e.getMessage();
					}
				}
			}
			//driver.close();
		} finally {
			driver.quit();

		}*/
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, ParseException {
		ConfigurableApplicationContext context = SpringApplication.run(ProducerApp.class, args);
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
		String browser = "CRM";
		//DriverFactory.start(browser);
		Apple obj = new Apple();
		    obj.scrapAndIndex(jmsTemplate);
	}

}
