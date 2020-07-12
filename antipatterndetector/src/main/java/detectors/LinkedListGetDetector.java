package detectors;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.resolution.types.ResolvedType;

import utils.JavaCompilationUnit;
import utils.VisitorEnum;

@SuppressWarnings("unchecked")
public class LinkedListGetDetector extends AbstractDetector {

	List<MethodCallExpr> methodCallExprs;
	
	public LinkedListGetDetector(JavaCompilationUnit jcu) throws FileNotFoundException {
		super(LinkedListGetDetector.class.getName(), jcu);
		this.methodCallExprs = new ArrayList<MethodCallExpr>();
	}

	@Override
	public void detect() {
		retrieveMethodCalls();
		methodCallExprs = methodCallExprs.stream().filter(mce -> mce.getNameAsString().equals("get")).collect(Collectors.toList());
		for (MethodCallExpr methodCall : methodCallExprs) {
			if(methodCall.getScope().isPresent()) {
				ResolvedType exprType = methodCall.getScope().get().calculateResolvedType();
				if(exprType.isReferenceType()) {
						if(exprType.asReferenceType().getTypeDeclaration().getQualifiedName().toString().equals("java.util.LinkedList")) {
							this.addOcurrence(new Ocurrence(methodCall.getRange().get().toString(),this.reason, this.file_path));
						}
				}
			}
		}
	}

	private void retrieveMethodCalls() {
		VoidVisitor<List<MethodCallExpr>> visitor = (VoidVisitor<List<MethodCallExpr>>) this.visitorFactory
				.getVisitor(VisitorEnum.METHOD_CALL_EXPR_VISITOR);
		visitor.visit(this.cu, methodCallExprs);
	}

}
