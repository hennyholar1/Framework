package com.test.automation.POMFramework.fileHandler;

import org.testng.annotations.Test;

import com.test.automation.POMFramework.fileReader.PdfFileReader;

public class PdfFileHandler {

	@Test
	public void testPdfFileReading() {
		PdfFileReader pdfReader = new PdfFileReader("C:\\Users\\Da Novenos\\eclipse-workspace\\POMFramework\\src\\main\\java\\com\\test\\automation\\POMFramework\\dataFile\\Geico.pdf");
		System.out.println("----------------------");
		String data = pdfReader.retrivePdfDataStored(13);
		System.out.println(data);
		System.out.println("----------------------------");
	}	
}
