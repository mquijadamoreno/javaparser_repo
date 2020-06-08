package testpackage;

import java.io.FileNotFoundException;
import java.util.Collection;

import detectors.AbstractDetector;
import detectors.ConstructorOverloadDetector;
import detectors.FillCollectionDetector;
import detectors.MethodOverloadDetector;
import detectors.SetAndGetDetector;
import detectors.Ocurrence;
import detectors.OverrideContentMethodSuperCallDetector;
import detectors.PublicCloneDetector;
import detectors.StringEqualsDetector;
import detectors.SuperCallOverrideMethodDetector;

public class TestJavaParser {

	private static final String FILE_PATH = "src/main/java/testpackage/TestClass.java";
	private static final String FILE_PATH2 = "src/main/java/testpackage/";
	
	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("** TEST METHOD OVERLOAD DETECTOR **");
		AbstractDetector methodOverloadDetector = new MethodOverloadDetector(FILE_PATH);
		methodOverloadDetector.detect();
		printOcurrences(methodOverloadDetector.getOcurrences());
		printLineSeparator();
		
		System.out.println("** TEST STRING EQUALS DETECTOR **");
		AbstractDetector stringEqualsDetector = new StringEqualsDetector(FILE_PATH2);
		stringEqualsDetector.detect();
		printOcurrences(stringEqualsDetector.getOcurrences());
		printLineSeparator();
		
		System.out.println("** TEST FOR STATEMENTS **");
		AbstractDetector fillCollectionDetector = new FillCollectionDetector(FILE_PATH);
		fillCollectionDetector.detect();
		//TODO: build ocurrences
		printLineSeparator();
		
		System.out.println("** TEST CONSTRUCTOR OVERLOAD **");
		AbstractDetector constructorOverloadDetector = new ConstructorOverloadDetector(FILE_PATH);
		constructorOverloadDetector.detect();
		printOcurrences(constructorOverloadDetector.getOcurrences());
		printLineSeparator();
		
		System.out.println("** TEST ATTRIBUTES SETTERS AND GETTERS **");
		AbstractDetector setAndGetDetector = new SetAndGetDetector(FILE_PATH);
		setAndGetDetector.detect();
		printOcurrences(setAndGetDetector.getOcurrences());
		printLineSeparator();
		
		System.out.println("** TEST SUPER CALL METHOD OVERRIDE **");
		AbstractDetector superCallOverrideMethodDetector = new SuperCallOverrideMethodDetector(FILE_PATH);
		superCallOverrideMethodDetector.detect();
		printOcurrences(superCallOverrideMethodDetector.getOcurrences());
		printLineSeparator();
		
		System.out.println("** TEST SUPER CALL METHOD CONTENT OVERRIDE **");
		AbstractDetector overrideContentMethodSuperCallDetector = new OverrideContentMethodSuperCallDetector(FILE_PATH);
		overrideContentMethodSuperCallDetector.detect();
		printOcurrences(overrideContentMethodSuperCallDetector.getOcurrences());
		printLineSeparator();
		
		System.out.println("** TEST PUBLIC CLONE METHOD IMPLEMENTS CLONEABLE **");
		AbstractDetector cloneDetector = new PublicCloneDetector(FILE_PATH);
		cloneDetector.detect();
		printOcurrences(cloneDetector.getOcurrences());
		printLineSeparator();
		
	}
	
	private static void printOcurrences(Collection<Ocurrence> ocurrences) {
		for (Ocurrence ocurrence : ocurrences) {
			System.out.println(ocurrence.toString());
		}
	}
	
	private static void printLineSeparator() {
		System.out.println("\n\n__________________________________________________________________________________________________________________________________________ \n\n");
	}
}
