package detectors;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;

import utils.JavaCompilationUnit;
import utils.VisitorEnum;

@SuppressWarnings("unchecked")
public class SuperCallOverrideMethodDetector extends AbstractDetector {

	private List<MethodDeclaration> methodDeclarations;

	public SuperCallOverrideMethodDetector(JavaCompilationUnit jcu) throws FileNotFoundException {
		super(SuperCallOverrideMethodDetector.class.getName(), jcu);
		methodDeclarations = new ArrayList<MethodDeclaration>();
	}

	@Override
	public void detect() {
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
				this.addOcurrence(new Ocurrence(method.getRange().get().toString(), this.reason, 
								((ClassOrInterfaceDeclaration) method.getParentNode().get()).getNameAsString()+ ".java, in method : " + method.getDeclarationAsString(false, false)));
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

}
