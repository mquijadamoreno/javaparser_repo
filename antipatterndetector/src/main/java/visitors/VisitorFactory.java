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
		case FOR_VISITOR:
			visitor = new ForVisitor();
			break;
		case FOREACH_VISITOR:
			visitor = new ForEachVisitor();
			break;
		case CONSTRUCTOR_VISITOR:
			visitor = new ConstructorDeclarationVisitor();
			break;
		case ATTRIBUTES_VISITOR:
			visitor = new AttributesVisitor();
			break;
		case METHOD_CALL_EXPR_VISITOR:
			visitor = new MethodCallExprVisitor();
			break;
		default:
			break;
		}
		return visitor;
	}

}
