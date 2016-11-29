package easyjob;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFNumbering;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class CoverletterProcessor {

	private String name = "Ethan (Xingyu Ji)";
	private String email = "Email: xingyuj@student.unimelb.edu.au";
	private String mobile = "Mobile: (+61) 452340717";
	private String companyName = "<com name>";
	private String companyAddress = "com address";
	private String jobURL;
	private String jobTitle = "<jobtitle>";
	private String recruiterName = "To Whom it May Concern,";
	private String webName = "<web name>";
	private String position = "<position name>";
	private String fontFamily = "Times New Roman";
	private String requiredType = "";
	private int size = 14;
	private int personalInfoSize = 12;
	private int companyInfoSize = 12;
	private List<String> requiredSkills = new ArrayList<String>();
	private static Logger logger = Logger.getLogger(CoverletterProcessor.class);

	public void writeCoverletter(String filename) throws Exception {
		logger.info("generating cover letter for company [" + companyName
				+ "] ...");

		XWPFDocument document = new XWPFDocument();
		XWPFNumbering numbering = document.createNumbering();
		// BigInteger abstractNumId = numbering.addAbstractNum(null);
		BigInteger numId = numbering.addNum(BigInteger.ZERO);

		processPersonalInfo(document);
		processCompanyInfo(document);
		processTitle(document);
		processBody(document);
		processBodySkills(document, numId);
		processConclusion(document);

		XWPFParagraph inscribe = document.createParagraph();
		inscribe.setAlignment(ParagraphAlignment.LEFT);
		XWPFRun _inscribe = inscribe.createRun();
		_inscribe.setText("Yours sincerely");
		_inscribe.addBreak();
		_inscribe.setText("Ethan Ji");
		_inscribe.setFontFamily(fontFamily);
		_inscribe.setFontSize(size);

		FileOutputStream fos = new FileOutputStream(new File(filename));
		document.write(fos);
		fos.close();
		logger.info("Company [" + companyName + "]'s cover letter generated");
	}

	public void processConclusion(XWPFDocument document) {
		XWPFParagraph conclusion = document.createParagraph();
		conclusion.setAlignment(ParagraphAlignment.BOTH);
		XWPFRun body = conclusion.createRun();
		// TODO: add name of the recruiter
		body.setText("I look forward to hearing from you and would appreciate that you could provide me with an interview to discuss my application further. I have also attached my resume with this cover letter for your consideration. Thank you very much in advance for your time.");
		body.addCarriageReturn();
		body.setFontFamily(fontFamily);
		body.setFontSize(size);
	}

	private void processBodySkills(XWPFDocument document, BigInteger numId)
			throws IOException {

		HashMap<String, String> results = retrieveResources();
		Iterator<Entry<String, String>> it = results.entrySet().iterator();
		while (it.hasNext()) {
			XWPFParagraph skillParagraph = document.createParagraph();
			skillParagraph.setIndentationFirstLine(0);
			skillParagraph.setIndentationHanging(0);
			skillParagraph.setIndentationLeft(0);
			skillParagraph.setIndentationRight(0);
			skillParagraph.setAlignment(ParagraphAlignment.BOTH);
			@SuppressWarnings("rawtypes")
			Map.Entry pair = (Map.Entry) it.next();
			XWPFRun stressed = skillParagraph.createRun();
			stressed.setText(pair.getKey().toString());
			stressed.setBold(true);
			stressed.setFontFamily(fontFamily);
			stressed.setFontSize(size);
			// System.out.println(pair.getValue());
			XWPFRun content = skillParagraph.createRun();
			content.setText(pair.getValue().toString());
			content.addCarriageReturn();
			content.setFontFamily(fontFamily);
			content.setFontSize(size);
			// numbering
			// skillParagraph.setNumID(numId);
		}
	}

	private void processPersonalInfo(XWPFDocument document) {
		XWPFParagraph personalInfoParagraph = document.createParagraph();
		personalInfoParagraph.setAlignment(ParagraphAlignment.RIGHT);
		XWPFRun personalInfo = personalInfoParagraph.createRun();
		personalInfo.setText(name);
		personalInfo.addCarriageReturn();
		personalInfo.setText(email);
		personalInfo.addCarriageReturn();
		personalInfo.setText(mobile);
		personalInfo.setFontFamily(fontFamily);
		personalInfo.setFontSize(personalInfoSize);
	}

	private void processCompanyInfo(XWPFDocument document) {
		XWPFParagraph companyInfoParagraph = document.createParagraph();
		companyInfoParagraph.setAlignment(ParagraphAlignment.LEFT);
		XWPFRun companyInfo = companyInfoParagraph.createRun();
		// TODO: add company
		companyInfo.setText(companyName);
		companyInfo.addCarriageReturn();
		companyInfo.setText(companyAddress);
		companyInfo.setFontFamily(fontFamily);
		companyInfo.setFontSize(companyInfoSize);
	}

	private void processTitle(XWPFDocument document) {
		XWPFParagraph titleParagraph = document.createParagraph();
		titleParagraph.setAlignment(ParagraphAlignment.LEFT);
		XWPFRun title = titleParagraph.createRun();
		title.addCarriageReturn();
		title.setText("RE: Application ");
		// TODO: add job title
		title.setText(jobTitle);
		title.addCarriageReturn();
		title.setFontSize(14);
		title.setBold(true);
		title.setFontFamily(fontFamily);
		title.setFontSize(size);
	}

	private void processBody(XWPFDocument document) {
		XWPFParagraph bodyParagraph = document.createParagraph();
		bodyParagraph.setAlignment(ParagraphAlignment.BOTH);
		XWPFRun body = bodyParagraph.createRun();
		// TODO: add name of the recruiter
		body.setText(recruiterName);
		body.addCarriageReturn();
		body.addCarriageReturn();
		// TODO: add position name and website name
		body.setText("I am writing to apply for "
				+ jobTitle
				+ ", as advertised on "
				+ webName
				+ ". I recently graduated from the University of Melbourne with a degree of Master of Engineering (Software) in 2016, possessing one-year Java development work experience from 2012 to 2013. ");
		body.addCarriageReturn();
		body.addCarriageReturn();
		body.setText("I am keen to apply my knowledge and essential skills gained through both projects experience and university study. This position appeals to me a lot as I believe to work at "
				+ companyName
				+ " is an ideal opportunity for me to contribute to real commercial software projects and collaborate with experienced developers, and to have access to new technologies which is always my favourite. ");
		body.addCarriageReturn();
		body.addCarriageReturn();
		body.setText("Concerning my skills to meet the essential requirements of this position:");
		body.setFontFamily(fontFamily);
		body.setFontSize(size);
	}

	/**
	 * 
	 * @param requiredType
	 * @return
	 * @throws IOException
	 */
	public HashMap<String, String> retrieveResources() throws IOException {
		File myFile = new File(
				"/Users/xingyuji/easyjob/coverletterresources.xlsx");
		HashMap<String, String> results = new HashMap<String, String>();
		HashMap<String, String> bakupResults = new HashMap<String, String>();
		FileInputStream fis = new FileInputStream(myFile);
		// Finds the workbook instance for XLSX file
		XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
		// Return first sheet from the XLSX workbook
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = mySheet.iterator();
		// Traversing over each row of XLSX file
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			// For each row, iterate through each columns
			Iterator<Cell> cellIterator = row.cellIterator();
			Cell cell = null;
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
			} else {
				continue;
			}
			String resourceType = cell.getStringCellValue();
			// requiredType: java ruby all
			if ("".equals(requiredType)
					|| requiredType.equalsIgnoreCase(resourceType)
					|| "all".equalsIgnoreCase(resourceType)) {

				if (cellIterator.hasNext()) {
					// skill key words
					cell = cellIterator.next();
				} else {
					continue;
				}
				// TODO: fuzzy matching
				String[] keywords = cell.getStringCellValue().split(",");
				for (String skill : requiredSkills) {
					for (int i = 0; i < keywords.length; i++) {
						// if (skill.contains(keywords[i])) {
						if (Pattern.matches(".*\\b" + keywords[i] + "\\b.*",
								skill.toLowerCase())) {
							// cell = cellIterator.next();
							if (cellIterator.hasNext()) {
								cell = cellIterator.next();
							} else {
								continue;
							}
							String stressedWords = cell.getStringCellValue();
							// cell = cellIterator.next();
							if (cellIterator.hasNext()) {
								cell = cellIterator.next();
							} else {
								continue;
							}
							// logger.error("")
							String content = cell.getStringCellValue();
							results.put(stressedWords, content);
							break;
						} else if (bakupResults.size() < 4) {
							// does not match, but store it up to 4 skills to
							// fill out cover letter if there is less than 2
							// matched skills
							if (cellIterator.hasNext()) {
								cell = cellIterator.next();
							} else {
								continue;
							}
							String stressedWords = cell.getStringCellValue();
							// cell = cellIterator.next();
							if (cellIterator.hasNext()) {
								cell = cellIterator.next();
							} else {
								continue;
							}
							// logger.error("")
							String content = cell.getStringCellValue();
							bakupResults.put(stressedWords, content);
							break;
						}
					}
				}

			}
		}
		if (results.size() == 0) {
			logger.error("no skills match position: [" + jobTitle
					+ "] at company: [" + companyName
					+ "], URL: ["+jobURL+"] do check manually, or are you just a dead dog?!");
			results = bakupResults;
		} else if (results.size() <= 2){
			logger.info("your skills match position: [" + jobTitle
					+ "] at company: [" + companyName
					+ "], URL: ["+jobURL+"] less than 2, automaticaly added another two");
			results.putAll(bakupResults);
		}
		return results;
	}

	public List<String> getRequiredSkills() {
		return requiredSkills;
	}

	public void setRequiredSkills(List<String> requiredSkills) {
		this.requiredSkills = requiredSkills;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getRecruiterName() {
		return recruiterName;
	}

	public void setRecruiterName(String recruiterName) {
		this.recruiterName = recruiterName;
	}

	public String getWebName() {
		return webName;
	}

	public void setWebName(String webName) {
		this.webName = webName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getFontFamily() {
		return fontFamily;
	}

	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getPersonalInfoSize() {
		return personalInfoSize;
	}

	public void setPersonalInfoSize(int personalInfoSize) {
		this.personalInfoSize = personalInfoSize;
	}

	public int getCompanyInfoSize() {
		return companyInfoSize;
	}

	public void setCompanyInfoSize(int companyInfoSize) {
		this.companyInfoSize = companyInfoSize;
	}

	public String getJobURL() {
		return jobURL;
	}

	public void setJobURL(String jobURL) {
		this.jobURL = jobURL;
	}

	public String getRequiredType() {
		return requiredType;
	}

	public void setRequiredType(String requiredType) {
		this.requiredType = requiredType;
	}

	public static void main(String[] args) throws Exception {
		CoverletterProcessor app = new CoverletterProcessor();
		ArrayList<String> skills = new ArrayList<String>();
		skills.add("qualification");
		skills.add("commercial environment");
		app.setRequiredSkills(skills);
		app.writeCoverletter("/Users/xingyuji/Desktop/cl/testfile.docx");
	}
}
