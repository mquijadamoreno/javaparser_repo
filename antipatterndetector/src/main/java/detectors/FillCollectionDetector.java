package detectors;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitor;

import utils.VisitorEnum;

@SuppressWarnings("unchecked")
public class FillCollectionDetector extends AbstractDetector {
	
	private List<ForStmt> forStatements;
	private List<ForEachStmt> foreachStatements;
	private static final String STRING_PATTERN = "[a-zA-Z0-9_\\-()\\.\\s{}<>]+.add\\([a-zA-Z0-9_\\-()\\.\\s{}<>]+\\);";

	public FillCollectionDetector(String name, String file_path) throws FileNotFoundException {
		super(name, file_path);
		// TODO Auto-generated constructor stub
	}
	
	public FillCollectionDetector(String file_path) throws FileNotFoundException {
		super(FillCollectionDetector.class.getName(),file_path);
		this.foreachStatements = new ArrayList<ForEachStmt>();
		this.forStatements = new ArrayList<ForStmt>();
	}

	@Override
	public void detect() {
		parse();
		retrieveForStatements();
		analyzeForStatements();
	}
	
	private void analyzeForStatements() {
		String parsedBodyFor;
		for (ForStmt ocurrence : forStatements) {
			parsedBodyFor = trimComments(ocurrence.getBody());
			if(parsedBodyFor.matches(STRING_PATTERN)) {
				System.out.println("Ocurrence of for statement found");
			}
		}
		
		String parsedBodyForEach;
		for (ForEachStmt ocurrence : foreachStatements) {
			parsedBodyForEach = trimComments(ocurrence.getBody());
			if(parsedBodyForEach.matches(STRING_PATTERN)) {
				System.out.println("Ocurrence of foreach statement found");
			}
		}
	}

	
	private String trimComments (Statement body) {
		String bodyParsed = "";
		for (Node node :body.getChildNodes()) {
			if(!(node instanceof LineComment)) {
				bodyParsed = bodyParsed.concat(node.toString());
			}
		}
		return bodyParsed;
	}
	
	private void retrieveForStatements() {
		VoidVisitor<List<ForStmt>> forVisitor = (VoidVisitor<List<ForStmt>>) this.visitorFactory.getVisitor(VisitorEnum.FOR_VISITOR);
		VoidVisitor<List<ForEachStmt>> foreachVisitor = (VoidVisitor<List<ForEachStmt>>) this.visitorFactory.getVisitor(VisitorEnum.FOREACH_VISITOR);
		forVisitor.visit(this.cu, forStatements);
		foreachVisitor.visit(this.cu, foreachStatements);
	}
	

	@Override
	protected void parse() {
		try {
			this.cu = JavaParser.parse(new FileInputStream(file_path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
