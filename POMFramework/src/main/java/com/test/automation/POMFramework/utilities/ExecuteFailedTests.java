
	package com.test.automation.POMFramework.utilities;

	import java.util.ArrayList;
	import java.util.List;

	import org.testng.TestNG;

	public class ExecuteFailedTests {

		public static void main(String[] args) {
		// Check the TestNG test-output file to locate the testng-failed.xml file, and add it to list of test using list.add();
			
			TestNG runner = new TestNG();
			List<String> list = new ArrayList<>();
			list.add("failed TEST(s) .xml location/path");	// Add the failed test that needs to be tested again in the list.add parameterized method
			runner.setTestSuites(list);
			runner.run();

		}

	}

