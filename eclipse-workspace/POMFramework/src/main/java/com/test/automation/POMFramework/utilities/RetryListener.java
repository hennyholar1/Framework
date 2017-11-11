package com.test.automation.POMFramework.utilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;

public class RetryListener implements IAnnotationTransformer{

	public void transform(ITestAnnotation testAnnotation, Class arg1, Constructor arg2, Method arg3) {
		
		IRetryAnalyzer retry = testAnnotation.getRetryAnalyzer();

		if (retry == null)	{
			testAnnotation.setRetryAnalyzer(Retry.class);
		}
	}

}