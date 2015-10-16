import java.io.IOException;
import java.util.List;


public class Compiler {

	public static void main(String args[]) throws IOException  
	{
		GrammarController gc = new GrammarController();
		ComputeStates cs = new ComputeStates();
		List<Grammar> grammarList = gc.readGrammer();
		List<String> nonTerminals = gc.readNonTerminals();
		//System.out.println(nonTerminals);
		gc.readTerminals();
	//	gc.printGrammar();
		//System.out.println(gc.computeFirst("A"));
		//System.out.println("Computed First");
		gc.getFirstFollow();
		gc.writeFirstFollow();
		//gc.printTerminals();
		//gc.printGrammar();
		
		States s = cs.computeStateZero();
	//	cs.printState();
		//cs.printBase();
		//cs.computeBaseState();
		//cs.computeStateBase();
		//cs.printBase();
		//cs.printState();
		//cs.computeAllOthers();
		//cs.printState();
		//cs.getUnique(s);
		//cs.printState();
		//cs.computeAllOthers(s);
		cs.stateController();
		//cs.printState();
		cs.itemsListWrite();
		cs.buildSLRTable();
		//cs.printSLR();
		cs.slrWrite();
		//cs.printMainStates();
		//cs.printGrammar();
		//cs.printGrammar2();
		
	}
}
