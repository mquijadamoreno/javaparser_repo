package testpackage;

import java.io.FileNotFoundException;
import java.util.Collection;

import detectors.AbstractDetector;
import detectors.ConstructorOverloadDetector;
import detectors.FillCollectionDetector;
import detectors.GetCollectionDetector;
import detectors.LinkedListGetDetector;
import detectors.MethodOverloadDetector;
import detectors.Ocurrence;
import detectors.OverrideContentMethodSuperCallDetector;
import detectors.PublicCloneDetector;
import detectors.SetAndGetDetector;
import detectors.StringEqualsDetector;
import detectors.SuperCallOverrideMethodDetector;
import utils.JavaCompilationUnit;

public class TestJavaParser {

	private static final String FILE_PATH = "src/main/java/testpackage/TestClass.java";
	private static final String FILE_PATH2 = "src/main/java/testpackage/";
	
	public static void main(String[] args) throws FileNotFoundException {
		
		JavaCompilationUnit jcu = new JavaCompilationUnit(FILE_PATH2, TestClass.class.getSimpleName()+".java");
		jcu.buildCompilationUnit();
		
		System.out.println("** TEST METHOD OVERLOAD DETECTOR **");
		AbstractDetector methodOverloadDetector = new MethodOverloadDetector(jcu);
		methodOverloadDetector.detect();
		printOcurrences(methodOverloadDetector.getOcurrences());
		printLineSeparator();
			
		System.out.println("** TEST STRING EQUALS DETECTOR **");
		AbstractDetector stringEqualsDetector = new StringEqualsDetector(jcu);
		stringEqualsDetector.detect();
		printOcurrences(stringEqualsDetector.getOcurrences());
		printLineSeparator();
		
		System.out.println("** TEST FOR STATEMENTS **");
		AbstractDetector fillCollectionDetector = new FillCollectionDetector(jcu);
		fillCollectionDetector.detect();
		printOcurrences(fillCollectionDetector.getOcurrences());
		printLineSeparator();
		
		System.out.println("** TEST CONSTRUCTOR OVERLOAD **");
		AbstractDetector constructorOverloadDetector = new ConstructorOverloadDetector(jcu);
		constructorOverloadDetector.detect();
		printOcurrences(constructorOverloadDetector.getOcurrences());
		printLineSeparator();
		
		System.out.println("** TEST ATTRIBUTES SETTERS AND GETTERS **");
		AbstractDetector setAndGetDetector = new SetAndGetDetector(jcu);
		setAndGetDetector.detect();
		printOcurrences(setAndGetDetector.getOcurrences());
		printLineSeparator();
		
		System.out.println("** TEST SUPER CALL METHOD OVERRIDE **");
		AbstractDetector superCallOverrideMethodDetector = new SuperCallOverrideMethodDetector(jcu);
		superCallOverrideMethodDetector.detect();
		printOcurrences(superCallOverrideMethodDetector.getOcurrences());
		printLineSeparator();
		
		System.out.println("** TEST SUPER CALL METHOD CONTENT OVERRIDE **");
		AbstractDetector overrideContentMethodSuperCallDetector = new OverrideContentMethodSuperCallDetector(jcu);
		overrideContentMethodSuperCallDetector.detect();
		printOcurrences(overrideContentMethodSuperCallDetector.getOcurrences());
		printLineSeparator();
		
		System.out.println("** TEST PUBLIC CLONE METHOD IMPLEMENTS CLONEABLE **");
		AbstractDetector cloneDetector = new PublicCloneDetector(jcu);
		cloneDetector.detect();
		printOcurrences(cloneDetector.getOcurrences());
		printLineSeparator();
		
		System.out.println("** TEST USE OF GET TO ACCESS A LINKEDLIST **");
		AbstractDetector getLinkedListDetector = new LinkedListGetDetector(jcu);
		getLinkedListDetector.detect();
		printOcurrences(getLinkedListDetector.getOcurrences());
		printLineSeparator();
		
		System.out.println("** TEST GET COLLECTION **");
		AbstractDetector getCollectionDetector = new GetCollectionDetector(jcu);
		getCollectionDetector.detect();
		printOcurrences(getCollectionDetector.getOcurrences());
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
