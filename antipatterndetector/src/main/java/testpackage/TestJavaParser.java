package testpackage;

import java.io.FileNotFoundException;

import detectors.AbstractDetector;
import detectors.MethodOverloadDetector;
import detectors.Ocurrence;
import detectors.StringEqualsDetector;

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
		
	}
}
