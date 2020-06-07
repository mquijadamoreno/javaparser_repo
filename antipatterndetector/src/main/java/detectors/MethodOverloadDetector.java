package detectors;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;

import utils.VisitorEnum;

@SuppressWarnings("unchecked")
public class MethodOverloadDetector extends AbstractDetector {
	
	private List<MethodDeclaration> methodDeclarations;

	public MethodOverloadDetector(String file_path) throws FileNotFoundException {
		super(MethodOverloadDetector.class.getName(), file_path);
		this.methodDeclarations = new ArrayList<MethodDeclaration>();
	}

	@Override
	public void detect() {
		parse();
		retrieveMethodNames();
		List<String> duplicates = retrieveDuplicates();
		Boolean encounteredOne = false;
		if(duplicates.size() > 0) {
			for (String methodName : duplicates) {
				for (MethodDeclaration methodDeclaration : methodDeclarations) {
					if(methodName.equals(methodDeclaration.getNameAsString()) && !methodDeclaration.getBody().toString().contains(methodName)) {
						if(!encounteredOne) {
							encounteredOne = true;
						} else {
							this.ocurrences.add(new Ocurrence(0,"When overloading more than 2 times a method, the method should contain a call of a common method", "class"));
							break;
						}										
					}
				}
			}
			
		} else {
			System.out.println("NO Method Overload");
		}
		
	}
	
	private void retrieveMethodNames(){
		VoidVisitor<List<MethodDeclaration>> visitor = (VoidVisitor<List<MethodDeclaration>>) this.visitorFactory.getVisitor(VisitorEnum.METHOD_NAME_VISITOR);
		visitor.visit(this.cu, methodDeclarations);
	}
	
	private List<String> retrieveDuplicates() {
		Map<String,Integer> methodsMap = new HashMap<>();
		List<String> duplicates = new ArrayList<String>();
		List<String> methodNames = methodDeclarations.stream().map(MethodDeclaration::getNameAsString).collect(Collectors.toList());
		
		Integer count = 0;
		for (String method : methodNames) {
			count = methodsMap.get(method);
			if(count == null) {
				methodsMap.put(method, 1);
			} else {
				methodsMap.put(method, ++count);
			}
		}
		
		for (Entry<String,Integer> entry : methodsMap.entrySet()) {
			if(entry.getValue() > 2) {
				duplicates.add(entry.getKey());
			}
		}
		
		return duplicates;
	}

	public List<String> getMethodNames() {
		return Collections.unmodifiableList(methodDeclarations.stream().map(MethodDeclaration::getNameAsString).collect(Collectors.toList()));
	}

	@Override
	protected void parse() {
		try {
			this.cu = JavaParser.parse(new FileInputStream(file_path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	
	

}
