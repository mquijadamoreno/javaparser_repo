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
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.visitor.VoidVisitor;

import utils.VisitorEnum;

@SuppressWarnings("unchecked")
public class OverrideContentMethodSuperCallDetector extends AbstractDetector {

	private List<MethodDeclaration> methodDeclarations;

	public OverrideContentMethodSuperCallDetector(String file_path) throws FileNotFoundException {
		super(OverrideContentMethodSuperCallDetector.class.getName(), file_path);
		methodDeclarations = new ArrayList<MethodDeclaration>();
	}

	@Override
	public void detect() {
		parse();
		retrieveMethodDeclarations();
		extractOverrideMethods();
		for (MethodDeclaration method : methodDeclarations) {
			for (MethodCallExpr methodCallExpr : method.findAll(MethodCallExpr.class)) {
				if (methodCallExpr.findAll(SuperExpr.class).size() > 0
						&& methodCallExpr.getName().equals(method.getName())) {

					if (checkIfDetected(method.findAll(SuperExpr.class).size(),
							method.findAll(ExpressionStmt.class).size(), method.findAll(ReturnStmt.class).size())) {

						System.out.println("The method only contains calls to super!");
						break;
					}
					;

				}
			}
		}

	}

	@Override
	protected void parse() {
		try {
			this.cu = JavaParser.parse(new FileInputStream(file_path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
