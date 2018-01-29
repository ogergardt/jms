package edu.berkeley.urel.jms.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScraperUtil {

	public String pDateWithoutYear(String pDate, String dateFormat)
			throws ParseException {

		Calendar cal = Calendar.getInstance();

		String sampleDate = new SimpleDateFormat("dd/MM")
				.format(new SimpleDateFormat(dateFormat).parse(pDate))
				+ "/"
				+ cal.get(Calendar.YEAR);

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		Date date1 = format.parse(sampleDate);

		Date nowdate = format.parse(format.format(new Date()));

		if (!date1.before(nowdate)) {

			cal.add(Calendar.YEAR, -1);

			pDate = new SimpleDateFormat("dd/MM").format(new SimpleDateFormat(
					"dd MMM").parse(pDate)) + "/" + cal.get(Calendar.YEAR);

		}

		return pDate;

	}

	public String pDateWithoutdate(String pDate, String dateFormat)
			throws ParseException {

		String sampleDate = "01"
				+ "/"
				+ new SimpleDateFormat("MM/yyyy").format(new SimpleDateFormat(
						dateFormat).parse(pDate));

		return sampleDate;

	}

	public static String pdateWithNow(String pDate) {

		pDate = pDate.trim().toUpperCase();

		switch (pDate) {

		case "NOW":

			pDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

			break;

		case "CURRENT DATE":

			pDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

			break;

		case "TODAY":

			pDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

			break;

		default:

			pDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

		}

		return pDate;

	}

	public static String pdateWithWords(String pDate) {

		pDate = pDate.trim().toUpperCase();

		String[] dateArr = null;

		String pdate = "";

		try {

			dateArr = pDate.split(" ");

			for (int i = 0; i < dateArr.length; i++) {

				Pattern p = Pattern.compile("(([A-Z].*[0-9])|([0-9].*[A-Z]))");

				Matcher m = p.matcher(dateArr[i]);

				if (m.find()) {

					pDate = m.group();

					// pDate = pDate.replaceAll("RD", "").replaceAll("TH",
					// "").replaceAll("ST", "").replaceAll("ND", "");

					dateArr[i] = m.group().replaceAll("RD", "")
							.replaceAll("TH", "").replaceAll("ST", "")
							.replaceAll("ND", "");

				}

				pdate = pdate + " " + dateArr[i];

			}
		} catch (Exception e) {

			pDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

		}

		return pdate;

	}

	public String fieldFinder(Elements elements, String[] fieldDataArr) {

		Elements resultElements = null;

		Element descElement = null;

		Boolean first = true;

		for (String fieldData : fieldDataArr) {

			resultElements = elements
					.select("*:containsOwn(" + fieldData + ")");

			if (resultElements.size() == 0) {
				continue;
			}

			Element parentElement = resultElements.first().parent();

			if (first) {

				descElement = parentElement;

				first = false;

			} else {

				// Skipping if elements already in description..

				if (!(descElement.html().contains(parentElement.html()) ||

				descElement.text().contains(parentElement.text()))) {

					descElement = descElement.append(parentElement.html());

				}

			}

		}

		return descElement.text();

	}

	public String emailidScrapper(String jobDesc) {

		ArrayList<String> arrayList = new ArrayList<String>();

		Pattern p = Pattern
				.compile("([a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-z]{2,6})");

		Matcher m = null;

		m = p.matcher(jobDesc);

		while (m.find()) {

			int start = m.start();

			int end = m.end();

			String jobDescStr = jobDesc.substring(start, end);

			arrayList.add(jobDescStr);

		}

		for (String arrayElement : arrayList) {

			jobDesc = jobDesc.replace(arrayElement, "***************");

		}

		return jobDesc;

	}

	public static String noofposition(String noofpos) {

		noofpos = noofpos.trim().replaceAll("[^\\w\\s0-9]", " ").toUpperCase();

		if (!noofpos.matches("[0-9][0-9][0-9]")) {

			String noofposArr[] = null;

			try {

				noofposArr = noofpos.split(" ");

			} catch (Exception e) {

				noofpos = "";

			}

			for (String nps : noofposArr)

				switch (nps) {

				case "ONE":

					noofpos = "1";

					break;

				case "TWO":

					noofpos = "2";

					break;

				case "THREE":

					noofpos = "3";

					break;

				case "FOUR":

					noofpos = "4";

					break;

				case "FIVE":

					noofpos = "5";

					break;

				case "SIX":

					noofpos = "6";

					break;

				case "SEVEN":

					noofpos = "7";

					break;

				case "EIGHT":

					noofpos = "8";

					break;

				case "NINE":

					noofpos = "9";

					break;

				case "TEN":

					noofpos = "10";

					break;

				case "multiple":

					noofpos = "MULTIPLE";

					break;

				default:

					noofpos = "1";

				}

		}

		return noofpos;

	}

	public static String expScrap(String experience) { // Used to convert the
														// experience given in
														// Words to its
														// corresponding digits.

		experience = experience.trim().toUpperCase();

		if (experience.contains("ONE")) {

			experience = "1 years";

		}

		else if (experience.contains("TWO")) {

			experience = "2 years";

		}

		else if (experience.contains("THREE")) {

			experience = "3 years";

		}

		else if (experience.contains("FOUR")) {

			experience = "4 years";

		}

		else if (experience.contains("FIVE")) {

			experience = "5 years";

		}

		else if (experience.contains("SIX")) {

			experience = "6 years";

		}

		else if (experience.contains("SEVEN")) {

			experience = "7 years";

		}

		else if (experience.contains("EIGHT")) {

			experience = "8 years";

		}

		else if (experience.contains("NINE")) {

			experience = "9 years";

		}

		else if (experience.contains("TEN")) {

			experience = "10 years";

		}

		return experience;

	}
	
	public String emailidFieldScrapper(String jobDesc) {

		String email = "";

		try {

			if (!jobDesc.isEmpty()) {

				ArrayList<String> arrayList = new ArrayList<String>();

				Boolean first = true;

				Pattern p = Pattern
						.compile("([a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-z]{2,6})");

				Matcher m = null;

				m = p.matcher(jobDesc);

				while (m.find()) {

					int start = m.start();

					int end = m.end();

					String jobDescStr = jobDesc.substring(start, end);

					arrayList.add(jobDescStr);

				}

				for (String arrayElement : arrayList) {

					if (first) {

						first = false;

						email = arrayElement;

						continue;

					}

					if (!email.contains(arrayElement)) {

						email = email + "~:~" + arrayElement;

					}

				}

			}

		} catch (Exception e) {
			email = "";
		}

		return email;

	}

	public String locationFieldScrapWithBigList(String description)
			throws IOException {
		description = description.toUpperCase();
		description = description.replaceAll("\u00A0", " ").replaceAll(
				"\u00a0", " ");
		StringBuffer locationBuffer = new StringBuffer();
		String locationFrmText = "";
		String pattern = "";
		InputStream inputStream = getClass().getResourceAsStream(
				"/locationsBigList.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));
		/**
		 * location matcher
		 */
		while ((locationFrmText = reader.readLine()) != null) {
			pattern = locationFrmText.toUpperCase();
			Pattern r = Pattern.compile("\\b" + pattern + "\\b");
			Matcher m = r.matcher(description);
			while (m.find()) {
				locationBuffer.append(pattern + "/ ");
			}
		}
		String resultMatch = locationBuffer.toString();
		return resultMatch;

	}

}
