package easyjob;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

public class SeekJobs {
	private int pageScope = 1;
	private List<Job> matches;
	private String location;
	private String jobTitle;
	private String[] exclusiveCriteria = new String[] { "mid", "senior",
			"support", "analyst" };
	private boolean exclusive;
	private CoverletterProcessor clProcessor;
	private String currentWeb;
	private String outputPath = "/Users/xingyuji/easyjob/progress/";
	private static Logger logger = Logger.getLogger(SeekJobs.class);
	private HashMap<String, List<String>> record;

	public SeekJobs() {
		this.matches = new ArrayList<Job>();
		this.clProcessor = new CoverletterProcessor();
	}

	public SeekJobs(int pageScope, String[] exclusiveCriteria) {
		this.matches = new ArrayList<Job>();
		this.pageScope = pageScope;
		this.exclusiveCriteria = exclusiveCriteria;
		this.clProcessor = new CoverletterProcessor();
	}

	public void onSeek() throws Exception {
		currentWeb = "SEEK";
		logger.info("Start seeking jobs on SEEK for [" + jobTitle + "] at ["
				+ location + "] within " + pageScope + " page(s)...");
		// TODO: add more location
		if ("melbourne".equalsIgnoreCase(location)) {
			location = "in-All-Melbourne-VIC";
		}
		for (int i = 0; i < pageScope; i++) {
			String url = "https://www.seek.com.au/jobs/" + location
					+ "?keywords=" + jobTitle + "&page=" + i;
			Document document = Jsoup.connect(url).timeout(6000).get();

			Elements elements = document.select("article");
			for (Element element : elements) {
				Elements articleContent = element.select("script");
				for (DataNode node : articleContent.get(0).dataNodes()) {
					Gson gson = new Gson();
					Job job = gson.fromJson(node.toString(), Job.class);

					if (determineMatch(job.getTitle(), job.getDescription(),
							jobTitle)) {
						matches.add(job);
					}
				}
			}
		}
		processEachMatchedJob();
	}

	public void onLinkedIn() throws IOException {
		String location = "in-All-Melbourne-VIC";
		// TODO: add more location
		if ("melbourne".equalsIgnoreCase(location)) {
			location = "in-All-Melbourne-VIC";
		}
		for (int i = 0; i < pageScope; i++) {
			String url = "https://www.seek.com.au/jobs/" + location
					+ "?keywords=" + jobTitle + "&page=" + i;
			Document document = Jsoup.connect(url).timeout(6000).get();

			Elements elements = document.select("article");
			for (Element element : elements) {
				Elements articleContent = element.select("script");
				for (DataNode node : articleContent.get(0).dataNodes()) {
					Gson gson = new Gson();
					Job job = gson.fromJson(node.toString(), Job.class);

					if (determineMatch(job.getTitle(), job.getDescription(),
							jobTitle)) {
						matches.add(job);
					}
				}
			}
		}
	}

	public void processEachMatchedJob() throws Exception {
		logger.info("processing matched jobs...");

		for (Job job : matches) {
			List<String> requiredSkills = new ArrayList<String>();
			Document document = Jsoup.connect(job.getUrl()).timeout(60000)
					.get();
			Elements elements = document.select("*.templatetext");
			for (Element element : elements) {
				Elements skills = element.select("li");
				if (skills.size() == 0) {
					// TODO: bug fix some should be matched may not be.
					String[] text = element.text().split("[•.,:]");
					for (int i = 0; i < text.length; i++) {
						requiredSkills.add(text[i]);
					}
				} else {
					for (Element skill : skills) {
						requiredSkills.add(skill.text());
					}
				}
			}
			String companyName = job.getHiringOrganization().getName();
			String jobTitle = job.getTitle();
			String url = job.getUrl();
			clProcessor.setCompanyAddress(job.getJobLocation().getAddress()
					.getAddressRegion());
			clProcessor.setCompanyName(job.getHiringOrganization().getName());
			clProcessor.setJobTitle(jobTitle);
			clProcessor.setJobURL(url);
			clProcessor.setPosition(jobTitle);
//			clProcessor.setRequiredType("java");
			// clProcessor.setRecruiterName("");
			clProcessor.setWebName(currentWeb);
			clProcessor.setRequiredSkills(requiredSkills);
			String positionAsPath = "/"
					+ jobTitle.replaceAll("[^a-zA-Z0-9.-]", "");
			String path = outputPath + companyName + positionAsPath;
			// Make dir
			new File(path).mkdirs();
			clProcessor.writeCoverletter(path + "/Xingyu-CL.docx");
			// leave job track info
			PrintWriter writer = new PrintWriter(path + "/info.txt", "UTF-8");
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			writer.printf(
					"Company: %s \n Position: %s \n URL: %s \n generated time: %s",
					companyName, jobTitle, url, dateFormat.format(date));
			writer.close();
			logger.info("preparing to apply position: [" + jobTitle
					+ "] of company [" + companyName + "]");
		}

	}

	private boolean determineMatch(String title, String description,
			String jobTitle) {
		String[] titleWords = jobTitle.split(" ");
		int count = 0;
		for (int i = 0; i < titleWords.length; i++) {
			if (title.toLowerCase().contains(titleWords[i])
					|| description.toLowerCase().contains(titleWords[i])) {
				count++;
			}
		}

		if (exclusive) {
			for (int j = 0; j < exclusiveCriteria.length; j++) {
				if (Pattern.matches(".*\\b" + exclusiveCriteria[j] + "\\b.*",
						title.toLowerCase())
						|| description.contains(exclusiveCriteria[j])) {
					return false;
				}
			}
		}
		return titleWords.length == count ? true : false;
	}

	public int getPageScope() {
		return pageScope;
	}

	public void setPageScope(int pageScope) {
		this.pageScope = pageScope;
	}

	public List<Job> getMatches() {
		return matches;
	}

	public void setMatches(List<Job> matches) {
		this.matches = matches;
	}

	public String[] getExclusiveCriteria() {
		return exclusiveCriteria;
	}

	public void setExclusiveCriteria(String[] exclusiveCriteria) {
		this.exclusiveCriteria = exclusiveCriteria;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public boolean isExclusive() {
		return exclusive;
	}

	public void setExclusive(boolean exclusive) {
		this.exclusive = exclusive;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public HashMap<String, List<String>> getRecord() {
		return record;
	}

	public void setRecord(HashMap<String, List<String>> record) {
		this.record = record;
	}

	public static void main(String[] args) throws Exception {
		String[] searchJobTitles = { "graduate software", "graduate java",
				"graduate ruby", "graduate web developer", "web developer" };
		String[] searchJobLocations = { "melbourne" };
		String[] searchJobOnWeb = { "seek", "linkedin", "indeed",
				"careeronline" };
		for (int i = 0; i < searchJobLocations.length; i++) {
			for (int j = 0; j < searchJobTitles.length; j++) {
				for (int k = 0; k < searchJobOnWeb.length; k++) {
					SeekJobs seekJobs = new SeekJobs();
					seekJobs.setPageScope(1); // how many pages of result to be
												// processed.
					seekJobs.setExclusive(true); // exclude jobs contain
													// specific key words.
					seekJobs.setLocation(searchJobLocations[i]);
					seekJobs.setJobTitle(searchJobTitles[j]);
					switch (searchJobOnWeb[k]) {
					case "seek":
						seekJobs.onSeek();
						break;
					case "linkedin":
						// seekJobs.onLinkedIn();
						break;
					case "indeed":

						break;
					case "careeronline":

						break;

					default:
						logger.error("no such web!");
						System.exit(1);
						break;
					}
				}
			}
		}
	}
}
