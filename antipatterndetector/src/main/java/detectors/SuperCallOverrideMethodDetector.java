package detectors;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;

import utils.VisitorEnum;

@SuppressWarnings("unchecked")
public class SuperCallOverrideMethodDetector extends AbstractDetector {

	private List<MethodDeclaration> methodDeclarations;

	public SuperCallOverrideMethodDetector(String file_path) throws FileNotFoundException {
		super(SuperCallOverrideMethodDetector.class.getName(), file_path);
		methodDeclarations = new ArrayList<MethodDeclaration>();
	}

	@Override
	public void detect() {
		parse();
		retrieveMethodNames();
		extractOverrideMethods();
		boolean superCallFound;
		for (MethodDeclaration method : methodDeclarations) {
			superCallFound = false;
			for (MethodCallExpr methodCallExpr : method.findAll(MethodCallExpr.class)) {
				if (methodCallExpr.findAll(SuperExpr.class).size() > 0
						&& methodCallExpr.getName().equals(method.getName())) {
					superCallFound = true;
					break;
				}
			}
			if(!superCallFound) {
				System.out.println("Detected in method : " + method.getNameAsString());
				System.out.println("Overrided methods should contain a call to its super method!");
			}
			
		}
	}
	
	private void extractOverrideMethods() {
		List<MethodDeclaration> methodDeclarationsAux = methodDeclarations.stream()
				.filter(m -> m.findAll(MarkerAnnotationExpr.class).toString().contains("@Override"))
				.collect(Collectors.toList());
		methodDeclarations.clear();
		methodDeclarations.addAll(methodDeclarationsAux);
	}

	private void retrieveMethodNames() {
		VoidVisitor<List<MethodDeclaration>> visitor2 = (VoidVisitor<List<MethodDeclaration>>) this.visitorFactory
				.getVisitor(VisitorEnum.METHOD_NAME_VISITOR);
		visitor2.visit(this.cu, methodDeclarations);

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
