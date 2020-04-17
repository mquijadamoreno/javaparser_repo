package detectors;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BinaryExpr.Operator;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;

import utils.VisitorEnum;

@SuppressWarnings("unchecked")
public class StringEqualsDetector extends AbstractDetector {

	List<BinaryExpr> binaryExpressions;

	public StringEqualsDetector(String name, String file_path) throws FileNotFoundException {
		super(name, file_path);
		this.binaryExpressions = new ArrayList<BinaryExpr>();
	}

	@Override
	public void detect() {
		retrieveEqualsExpressions();
		extractStringEqualExpr();
	}

	
	@Override
	protected void parse() {
		TypeSolver typeSolver = new CombinedTypeSolver();
		JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
		JavaParser.getStaticConfiguration().setSymbolResolver(symbolSolver);
		this.cu = JavaParser.parse(this.file_path);
	}

	private void extractStringEqualExpr() {
		List<BinaryExpr> binaryStringEqualExpressions = new ArrayList<BinaryExpr>();
		CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
		for (BinaryExpr expr : this.binaryExpressions) {
			if (expr.getOperator() == Operator.EQUALS && (expr.getLeft().calculateResolvedType().isReferenceType()
					&& expr.getRight().calculateResolvedType().isReferenceType())) {
				if (JavaParserFacade.get(combinedTypeSolver).getType(expr.getRight()).asReferenceType()
						.getQualifiedName().equals("java.lang.String")
						&& JavaParserFacade.get(combinedTypeSolver).getType(expr.getLeft()).asReferenceType()
								.getQualifiedName().equals("java.lang.String")) {
					binaryStringEqualExpressions.add(expr);
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
