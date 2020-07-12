package detectors;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;

import utils.JavaCompilationUnit;
import utils.VisitorEnum;

@SuppressWarnings("unchecked")
public class MethodOverloadDetector extends AbstractDetector {

	private List<MethodDeclaration> methodDeclarations;

	public MethodOverloadDetector(JavaCompilationUnit jcu) throws FileNotFoundException {
		super(MethodOverloadDetector.class.getName(), jcu);
		this.methodDeclarations = new ArrayList<MethodDeclaration>();
	}

	@Override
	public void detect() {
		retrieveMethodNames();
		retrieveDuplicates();
		if(methodDeclarations.size() > 2) {
			Integer numberOfThisCalls = 0;
			for (MethodDeclaration method : methodDeclarations) {
				for (MethodCallExpr methodCall : method.findAll(MethodCallExpr.class)) {
					if (methodCall.getNameAsString().equals(method.getNameAsString())
							&& methodCall.getScope().get() instanceof ThisExpr) {
						numberOfThisCalls++;
						break;
					}
				}
			}

			if (numberOfThisCalls != (methodDeclarations.size() - 1)) {
				this.addOcurrence(new Ocurrence("undefined", this.reason,
						((ClassOrInterfaceDeclaration) methodDeclarations.get(0).getParentNode().get()).getNameAsString()+".java"));
			}
		}
		

	}

	private void retrieveMethodNames() {
		VoidVisitor<List<MethodDeclaration>> visitor = (VoidVisitor<List<MethodDeclaration>>) this.visitorFactory
				.getVisitor(VisitorEnum.METHOD_NAME_VISITOR);
		visitor.visit(this.cu, methodDeclarations);
	}

	private void retrieveDuplicates() {
		Map<String, Integer> methodsMap = new HashMap<>();
		List<String> duplicates;
		List<String> methodNames = methodDeclarations.stream().map(MethodDeclaration::getNameAsString)
				.collect(Collectors.toList());

		Integer count = 0;
		for (String method : methodNames) {
			count = methodsMap.get(method);
			if (count == null) {
				methodsMap.put(method, 1);
			} else {
				methodsMap.put(method, ++count);
			}
		}

		duplicates = methodsMap.entrySet().stream().filter(md -> md.getValue() > 2).map(Entry<String, Integer>::getKey)
				.collect(Collectors.toList());
		methodDeclarations = methodDeclarations.stream().filter(md -> duplicates.contains(md.getNameAsString()))
				.collect(Collectors.toList());
	}

	public List<String> getMethodNames() {
		return Collections.unmodifiableList(
				methodDeclarations.stream().map(MethodDeclaration::getNameAsString).collect(Collectors.toList()));
	}

}
