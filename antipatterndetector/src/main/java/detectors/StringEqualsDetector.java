package detectors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BinaryExpr.Operator;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Operators;

import utils.VisitorEnum;

@SuppressWarnings("unchecked")
public class StringEqualsDetector extends AbstractDetector {

	List<BinaryExpr> binaryExpressions;

	public StringEqualsDetector(String file_path) throws FileNotFoundException {
		super(StringEqualsDetector.class.getName(), file_path);
		this.binaryExpressions = new ArrayList<BinaryExpr>();
	}

	@Override
	public void detect() {
		parse();
		retrieveEqualsExpressions();
		extractStringEqualExpr();
	}

	@Override
	protected void parse() {

		try {
			CombinedTypeSolver typeSolver = new CombinedTypeSolver();
			typeSolver.add(new JavaParserTypeSolver(new File(this.file_path)));
			typeSolver.add(new ReflectionTypeSolver());
			JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
			JavaParser.getStaticConfiguration().setSymbolResolver(symbolSolver);
			this.cu = JavaParser.parse(new FileInputStream(this.file_path + "TestClass.java"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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
				System.out.println(binaryExpr);
			}
		}
	}

	private void retrieveEqualsExpressions() {
		VoidVisitor<List<BinaryExpr>> visitor = (VoidVisitor<List<BinaryExpr>>) this.visitorFactory
				.getVisitor(VisitorEnum.BINARY_EXPR_VISITOR);
		visitor.visit(this.cu, binaryExpressions);
	}

}
