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
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.visitor.VoidVisitor;

import utils.JavaCompilationUnit;
import utils.VisitorEnum;

@SuppressWarnings("unchecked")
public class OverrideContentMethodSuperCallDetector extends AbstractDetector {

	private List<MethodDeclaration> methodDeclarations;

	public OverrideContentMethodSuperCallDetector(JavaCompilationUnit jcu) throws FileNotFoundException {
		super(OverrideContentMethodSuperCallDetector.class.getName(), jcu);
		methodDeclarations = new ArrayList<MethodDeclaration>();
	}

	@Override
	public void detect() {
		retrieveMethodDeclarations();
		extractOverrideMethods();
		for (MethodDeclaration method : methodDeclarations) {
			for (MethodCallExpr methodCallExpr : method.findAll(MethodCallExpr.class)) {
				if (methodCallExpr.findAll(SuperExpr.class).size() > 0
						&& methodCallExpr.getName().equals(method.getName())) {

					if (checkIfDetected(method.findAll(SuperExpr.class).size(),
							method.findAll(ExpressionStmt.class).size(), method.findAll(ReturnStmt.class).size())) {
						this.addOcurrence(new Ocurrence(method.getRange().get().toString(), this.reason,
								((ClassOrInterfaceDeclaration) method.getParentNode().get()).getNameAsString()+ ".java, in method : " + method.getDeclarationAsString(false, false)));
						break;
					}
				}
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

	private void retrieveMethodDeclarations() {
		VoidVisitor<List<MethodDeclaration>> visitor = (VoidVisitor<List<MethodDeclaration>>) this.visitorFactory
				.getVisitor(VisitorEnum.METHOD_NAME_VISITOR);
		visitor.visit(this.cu, methodDeclarations);
	}

	private boolean checkIfDetected(Integer superCalls, Integer expressions, Integer returnCalls) {
		boolean superCallsEqualsToExpressions = superCalls == expressions;
		boolean superCallsEqualsToReturnCalls = (expressions == 0) && (superCalls == returnCalls);
		boolean superCallsEqualsToReturnCallsAndExpressions = superCalls == (returnCalls + expressions);
		return superCallsEqualsToExpressions || superCallsEqualsToReturnCalls
				|| superCallsEqualsToReturnCallsAndExpressions;
	}

}
