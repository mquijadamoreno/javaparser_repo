package visitors;

import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class AnnotantionVisitor extends VoidVisitorAdapter<Object> {
	
	@Override
	public void visit(AnnotationDeclaration n, Object arg) {
		super.visit(n, arg);
		}
	
	@Override
	public void visit(MarkerAnnotationExpr n, Object arg) {
		super.visit(n, arg);
	}

}
