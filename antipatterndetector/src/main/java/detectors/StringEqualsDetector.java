package detectors;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BinaryExpr.Operator;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.resolution.types.ResolvedType;

import utils.JavaCompilationUnit;
import utils.VisitorEnum;

@SuppressWarnings("unchecked")
public class StringEqualsDetector extends AbstractDetector {

	List<BinaryExpr> binaryExpressions;

	public StringEqualsDetector(JavaCompilationUnit jcu) throws FileNotFoundException {
		super(StringEqualsDetector.class.getName(), jcu);
		this.binaryExpressions = new ArrayList<BinaryExpr>();
	}

	@Override
	public void detect() {
		retrieveEqualsExpressions();
		extractStringEqualExpr();
	}

	private void extractStringEqualExpr() {
		List<BinaryExpr> binaryEqualExpressions = new ArrayList<BinaryExpr>();	
		for (BinaryExpr binaryExpr : binaryExpressions) {
			if(binaryExpr.getOperator().equals(Operator.EQUALS)){
				binaryEqualExpressions.add(binaryExpr);
			}
		}
		
		for (BinaryExpr binaryExpr : binaryEqualExpressions) {
			ResolvedType left = binaryExpr.getLeft().calculateResolvedType();
			ResolvedType right = binaryExpr.getRight().calculateResolvedType();
			
			if(left.isReferenceType() && right.isReferenceType()) {
				if(left.asReferenceType().getQualifiedName().equals("java.lang.String") && right.asReferenceType().getQualifiedName().equals("java.lang.String")) {
					this.addOcurrence(new Ocurrence(binaryExpr.getRange().get().toString(), this.reason, "TestClass.java"));
				}
			}
		}
	}

	private void retrieveEqualsExpressions() {
		VoidVisitor<List<BinaryExpr>> visitor = (VoidVisitor<List<BinaryExpr>>) this.visitorFactory
				.getVisitor(VisitorEnum.BINARY_EXPR_VISITOR);
		visitor.visit(this.cu, binaryExpressions);
	}

}
