package visitors;

import com.github.javaparser.ast.visitor.VoidVisitor;

import utils.VisitorEnum;

public class VisitorFactory {
	
	public VoidVisitor<?> getVisitor(VisitorEnum visitorKey) {
		VoidVisitor<?> visitor = null;
		switch(visitorKey) {
		case METHOD_NAME_VISITOR:
			visitor = new MethodDeclarationVisitor();
			break;
		case BINARY_EXPR_VISITOR:
			visitor = new BinaryExprVisitor();
			break;
		default:
			break;
			
		}
		return visitor;
	}

}
