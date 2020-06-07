package visitors;

import java.util.List;

import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ForEachVisitor extends VoidVisitorAdapter<List<ForEachStmt>> {

	@Override
	public void visit(ForEachStmt n, List<ForEachStmt> listForEachStmt) {
		super.visit(n, listForEachStmt);
		listForEachStmt.add(n);
	}
	
}
