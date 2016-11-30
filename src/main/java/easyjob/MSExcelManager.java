package easyjob;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MSExcelManager {
	private String requiredType = "";
	private XSSFWorkbook workBook;
	private String filename = "/Users/xingyuji/easyjob/coverletterresources.xlsx";
	
	public XSSFWorkbook getWorkBook() {
		return workBook;
	}

	public void setWorkBook(XSSFWorkbook workBook) {
		this.workBook = workBook;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getRequiredType() {
		return requiredType;
	}

	public void setRequiredType(String requiredType) {
		this.requiredType = requiredType;
	}

	public List<List<String>> extractXLSX() throws IOException {
		List<List<String>> results = new ArrayList<List<String>>();

		FileInputStream fis = new FileInputStream(filename);
		workBook = new XSSFWorkbook(fis);
		// Return first sheet from the XLSX workbook
		XSSFSheet mySheet = workBook.getSheetAt(0);
		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = mySheet.iterator();
		// Traversing over each row of XLSX file
		while (rowIterator.hasNext()) {
			List<String> rowList = new ArrayList<String>();
			Row row = rowIterator.next();
			// For each row, iterate through each columns
			Iterator<Cell> cellIterator = row.cellIterator();
			Cell cell = null;
			if (cellIterator.hasNext()) {
				cell = cellIterator.next();
			} else {
				System.err.println("Empty row read, please check!");
				continue;
			}
			String resourceType = cell.getStringCellValue();
			// requiredType: java ruby all
			if ("".equals(requiredType)
					|| requiredType.equalsIgnoreCase(resourceType)
					|| "all".equalsIgnoreCase(resourceType)) {
				// add keywords
				rowList.add(cellIterator.next().toString());
				// add stressed words
				rowList.add(cellIterator.next().toString());
				// add description words
				rowList.add(cellIterator.next().toString());
			} else {
				continue;
			}
			results.add(rowList);
		}
		return results;
	}

	public static void main(String[] args) throws IOException {
		// /Users/xingyuji/easyjob/coverletterresources.xlsx
		 MSExcelManager readxlsx = new MSExcelManager();
		 readxlsx.setFilename("/Users/xingyuji/easyjob/coverletterresources.xlsx");
		 List<List<String>> results = readxlsx.extractXLSX();
		 System.out.println(results);
		// Hashtable<String, String> results =
		// readxlsx.retrieveResources("java");
		// System.out.println("keywords: " + results.keys().nextElement());
		// Read more:
		// http://www.java67.com/2014/09/how-to-read-write-xlsx-file-in-java-apache-poi-example.html#ixzz4RIWo3w45
	}
}
