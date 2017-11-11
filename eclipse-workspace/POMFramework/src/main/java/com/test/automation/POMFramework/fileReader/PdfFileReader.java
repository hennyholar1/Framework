package com.test.automation.POMFramework.fileReader;

import java.io.IOException;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class PdfFileReader {

	String pathToFile;
	PdfReader pdfReader;
	
	public PdfFileReader(String pathToFile) {
		this.pathToFile = pathToFile;
		try {
			this.pdfReader = new PdfReader(("C:\\Users\\Da Novenos\\eclipse-workspace\\POMFramework\\src\\main\\java\\com\\test\\automation\\POMFramework\\dataFile\\Geico.pdf"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	public String retrivePdfDataStored (int pageNumber) {
		try {
			String data = PdfTextExtractor.getTextFromPage(pdfReader, pageNumber);
			pdfReader.close();
			return data;
		} 
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return null;	
	}
}
