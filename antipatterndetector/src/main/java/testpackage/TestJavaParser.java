package testpackage;

import java.io.FileNotFoundException;

import detectors.AbstractDetector;
import detectors.MethodOverloadDetector;
import detectors.Ocurrence;

public class TestJavaParser {

	private static final String FILE_PATH = "src/main/java/testpackage/TestClass.java";
	
	public static void main(String[] args) throws FileNotFoundException {
		AbstractDetector methodOverloadDetector = new MethodOverloadDetector(FILE_PATH);
		methodOverloadDetector.detect();
		for (Ocurrence ocurrence : methodOverloadDetector.getOcurrences()) {
			System.out.println(ocurrence.toString());
		}
	}
}
