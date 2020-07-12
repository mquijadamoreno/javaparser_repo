package detectors;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.visitor.VoidVisitor;

import utils.JavaCompilationUnit;
import utils.VisitorEnum;

@SuppressWarnings("unchecked")
public class FillCollectionDetector extends AbstractDetector {

	private List<ForStmt> forStatements;
	private List<ForEachStmt> foreachStatements;

	public FillCollectionDetector(JavaCompilationUnit jcu) throws FileNotFoundException {
		super(FillCollectionDetector.class.getName(), jcu);
		this.foreachStatements = new ArrayList<ForEachStmt>();
		this.forStatements = new ArrayList<ForStmt>();
	}

	@Override
	public void detect() {
		retrieveForStatements();
		for (ForStmt forstmt : forStatements) {
			if (forstmt.getBody().findAll(ExpressionStmt.class).size() == 1
					&& forstmt.findAll(MethodCallExpr.class).toString().contains("add")) {
				this.addOcurrence(new Ocurrence(forstmt.getRange().toString().replace("Optional[", "").replace("]", ""), this.reason, this.file_path));
				break;
			}
		}
		
		for (ForEachStmt foreachstmt : foreachStatements) {
			if (foreachstmt.getBody().findAll(ExpressionStmt.class).size() == 1
					&& foreachstmt.findAll(MethodCallExpr.class).toString().contains("add")) {
				this.addOcurrence(new Ocurrence(foreachstmt.getRange().toString().replace("Optional[", "").replace("]", ""), this.reason, this.file_path));
				break;
			}
		}
	}


	private void retrieveForStatements() {
		VoidVisitor<List<ForStmt>> forVisitor = (VoidVisitor<List<ForStmt>>) this.visitorFactory
				.getVisitor(VisitorEnum.FOR_VISITOR);
		VoidVisitor<List<ForEachStmt>> foreachVisitor = (VoidVisitor<List<ForEachStmt>>) this.visitorFactory
				.getVisitor(VisitorEnum.FOREACH_VISITOR);
		forVisitor.visit(this.cu, forStatements);
		foreachVisitor.visit(this.cu, foreachStatements);
	}


}
