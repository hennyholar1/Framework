package com.test.automation.POMFramework.fileReader;

import java.io.IOException;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class PdfFileReader {

	private static final Logger log = LogFunction.getLogger(WjvPendingTransactionPO.class);
		String pathToPdfFile;
		String ignoreFile = "/cars-wjv-test/ignore.conf";
		PdfReader pdfReader;
	
	public PdfFileReader(String pathToFile) {
		pathToPdfFile = pathToFile;
		try {
			this.pdfReader = new PdfReader(("C:\\Users\\Da Novenos\\eclipse-workspace\\POMFramework\\src\\main\\java\\com\\test\\automation\\POMFramework\\dataFile\\Geico.pdf"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	public String retrivePdfDataStored (int pageNumber) {
		/*	https://www.youtube.com/watch?v=koMegSvZEVE
		 *  	https://github.com/red6/pdfcompare	
		 * 	http://www.verypdf.com/app/pdf-to-txt-converter/index.html
		 * */
			String pdfData = null;
			try {
				pdfData = PdfTextExtractor.getTextFromPage(pdfReader, pageNumber);
				pdfReader.close();
				return pdfData;
			} 
			catch (IOException e) {
				log.info(e.getMessage());
			}
			return pdfData;	
		}
	
	public void comparePdfFiles(String expectPdfFile, String actualPdfFile, String pdfOutPutFile) {
		//	All the files must be of .pdf format
			try {
			boolean result = new PdfComparator(expectPdfFile, actualPdfFile).withIgnore(ignoreFile).compare().writeTo(pdfOutPutFile);
			log.info(result);
			} catch (Exception e) 
			{
			log.info(e.getMessage());
			}
		}
	}
