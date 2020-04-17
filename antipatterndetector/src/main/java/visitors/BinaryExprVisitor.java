package visitors;

import java.util.List;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class BinaryExprVisitor extends VoidVisitorAdapter<List<BinaryExpr>> {
	
	@Override
	public void visit(BinaryExpr n, List<BinaryExpr> collector) {
		super.visit(n, collector);
		collector.add(n);
	}
}
