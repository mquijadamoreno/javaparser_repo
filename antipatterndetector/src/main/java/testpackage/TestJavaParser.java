package testpackage;

import java.io.FileNotFoundException;

import detectors.AbstractDetector;
import detectors.ConstructorOverloadDetector;
import detectors.FillCollectionDetector;
import detectors.MethodOverloadDetector;
import detectors.SetAndGetDetector;
import detectors.Ocurrence;
import detectors.OverrideContentMethodSuperCallDetector;
import detectors.StringEqualsDetector;
import detectors.SuperCallOverrideMethodDetector;

public class TestJavaParser {

	private static final String FILE_PATH = "src/main/java/testpackage/TestClass.java";
	private static final String FILE_PATH2 = "src/main/java/testpackage/";
	
	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("** TEST METHOD OVERLOAD DETECTOR **");
		AbstractDetector methodOverloadDetector = new MethodOverloadDetector(FILE_PATH);
		methodOverloadDetector.detect();
		for (Ocurrence ocurrence : methodOverloadDetector.getOcurrences()) {
			System.out.println(ocurrence.toString());
		}
		
		System.out.println("** TEST STRING EQUALS DETECTOR **");
		AbstractDetector stringEqualsDetector = new StringEqualsDetector(FILE_PATH2);
		stringEqualsDetector.detect();
		
		System.out.println("** TEST FOR STATEMENTS **");
		AbstractDetector fillCollectionDetector = new FillCollectionDetector(FILE_PATH);
		fillCollectionDetector.detect();
		
		System.out.println("** TEST CONSTRUCTOR OVERLOAD **");
		AbstractDetector constructorOverloadDetector = new ConstructorOverloadDetector(FILE_PATH);
		constructorOverloadDetector.detect();
		
		System.out.println("** TEST ATTRIBUTES MODIFIERS **");
		AbstractDetector setAndGetDetector = new SetAndGetDetector(FILE_PATH);
		setAndGetDetector.detect();
		
		System.out.println("** TEST SUPER CALL METHOD OVERRIDE **");
		AbstractDetector superCallOverrideMethodDetector = new SuperCallOverrideMethodDetector(FILE_PATH);
		superCallOverrideMethodDetector.detect();
		
		System.out.println("** TEST SUPER CALL METHOD CONTENT OVERRIDE **");
		AbstractDetector overrideContentMethodSuperCallDetector = new OverrideContentMethodSuperCallDetector(FILE_PATH);
		overrideContentMethodSuperCallDetector.detect();
	}
}
