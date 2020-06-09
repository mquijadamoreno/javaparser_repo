package visitors;

import java.util.List;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MethodCallExprVisitor extends VoidVisitorAdapter<List<MethodCallExpr>> {
	
	@Override
	public void visit(MethodCallExpr n, List<MethodCallExpr> list) {
		super.visit(n, list);
		list.add(n);
	}

}
