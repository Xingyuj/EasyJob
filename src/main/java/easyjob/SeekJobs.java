package easyjob;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	private List<String> jobTitles;
	private String[] exclusiveCriteria = new String[] { "mid", "senior",
			"support", "analyst" };
	private boolean exclusive;
	private CoverletterProcessor clProcessor;
	private String currentWeb;
	private String outputPath = "/Users/xingyuji/easyjob/progress/";
	
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

	public void onSeek() throws IOException {
		currentWeb = "SEEK";
		String location = "in-All-Melbourne-VIC";
		// TODO: add more location
		if ("melbourne".equalsIgnoreCase(location)) {
			location = "in-All-Melbourne-VIC";
		}
		for (String jobTitle : jobTitles) {
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

						if (determineMatch(job.getTitle(),
								job.getDescription(), jobTitle)) {
							matches.add(job);
						}
					}
				}
			}
		}
	}
	
	public void onLinkedIn() throws IOException {
		String location = "in-All-Melbourne-VIC";
		// TODO: add more location
		if ("melbourne".equalsIgnoreCase(location)) {
			location = "in-All-Melbourne-VIC";
		}
		for (String jobTitle : jobTitles) {
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

						if (determineMatch(job.getTitle(),
								job.getDescription(), jobTitle)) {
							matches.add(job);
						}
					}
				}
			}
		}
	}

	public void processEachMatchedJob() throws Exception {
		for (Job job : matches) {
			List<String> requiredSkills = new ArrayList<String>();
			Document document = Jsoup.connect(job.getUrl()).timeout(60000).get();
			Elements elements = document.select("div.templatetext");
			for (Element element : elements) {
				Elements skills = element.select("li");
				for (Element skill : skills) {
//					System.out.println(skill.text()+"&*&*&*&*&*&*&*");
					requiredSkills.add(skill.text());
				}
			}
			String companyName = job.getHiringOrganization().getName();
			clProcessor.setCompanyAddress(job.getJobLocation().getAddress().getAddressRegion());
			clProcessor.setCompanyName(job.getHiringOrganization().getName());
			clProcessor.setJobTitle(job.getTitle());
			clProcessor.setPosition(job.getTitle());
//					clProcessor.setRecruiterName("");
			clProcessor.setWebName(currentWeb);
			clProcessor.setRequiredSkills(requiredSkills);
			new File(outputPath+companyName).mkdir();
			clProcessor.writeCoverletter(outputPath+companyName+"/Xingyu-CL.docx");
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
				if (title.toLowerCase().contains(exclusiveCriteria[j])
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

	public List<String> getJobTitles() {
		return jobTitles;
	}

	public void setJobTitles(List<String> jobTitles) {
		this.jobTitles = jobTitles;
	}

	public static void main(String[] args) throws Exception {
		SeekJobs seekJobs = new SeekJobs();
		ArrayList<String> titles = new ArrayList<String>();
		titles.add("graduate software");
		seekJobs.setJobTitles(titles);
		seekJobs.onSeek();
		seekJobs.processEachMatchedJob();
//		Document document = Jsoup.connect("https://www.seek.com.au/job/32345698").timeout(6000).get();

	}
}
