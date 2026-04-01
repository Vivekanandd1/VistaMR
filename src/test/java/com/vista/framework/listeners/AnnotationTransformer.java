package com.vista.framework.listeners;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Annotation Transformer for setting retry analyzer on all test methods.
 */
public class AnnotationTransformer implements IAnnotationTransformer {
    
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, 
                         Constructor testConstructor, Method testMethod) {
        // Set retry analyzer for all test methods
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}
