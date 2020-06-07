package visitors;

import java.util.List;

import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ForVisitor extends VoidVisitorAdapter<List<ForStmt>> {
	
	@Override
	public void visit(ForStmt n, List<ForStmt> forList) {
		super.visit(n, forList);
		forList.add(n);
	}

}
