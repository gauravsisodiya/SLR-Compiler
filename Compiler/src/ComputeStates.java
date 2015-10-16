import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class ComputeStates extends GrammarController {

	List<Grammar> grammarList = readGrammer();
	List<String> nonTerminals = readNonTerminals();
	List<String> terminals = readTerminals();
	List<Grammar> grammarList2 = readGrammer();
	GrammarController basegc = new GrammarController();
	List<Grammar> operatingGrammar = cloneGrammar(grammarList);
	List<States> resultStates = new ArrayList<States>();
	States baseState = new States();
	HashMap<String, String> mainStates = new HashMap<String, String>();
	// / List<FirstFollow> localFF = new ArrayList<FirstFollow>();
	List<FirstFollow> firstFollow = getFirstFollow();

	int stateCount = 1;

	public States computeStateZero() {
		States stateZero = new States();

		List<Grammar> grammars = new ArrayList<Grammar>();
		List<String> prods = new ArrayList<String>();
		for (Grammar g : grammarList) {
			if (g.getId() == 0) {
				stateZero.setStateName("I" + 0);
				prods = g.getProductions();
				g.setSt(push2Stack(g));
				prods.add(0, ".");
				g.setProductions(prods);
				grammars.add(g);
				g.setSt(g.getSt());
				// System.out.println(g.getSt());
				grammars.addAll(compZeroSt(g, cloneGrammar(grammarList),
						grammars));
				stateZero.setGrammars(grammars);

			}

		}
		for (Grammar g : stateZero.getGrammars()) {
			g.setFlag(true);
		}
		resultStates.add(stateZero);

		baseState = stateZero.clone();
		return stateZero;
	}

	public String getNextElement(Grammar g) {
		int index = g.getProductions().indexOf(".");
		String element = "";
		if (index == g.getProductions().size() - 1)
			element = null;
		else if (index < g.getProductions().size() - 1)
			element = g.getProductions().get(index + 1);

		return element;

	}

	public Stack<String> push2Stack(Grammar g) {
		Stack<String> st = new Stack<String>();
		for (int i = g.getProductions().size() - 1; i >= 0; i--) {
			st.push(g.getProductions().get(i));
		}
		return st;
	}

	public List<Grammar> compZeroSt(Grammar inputGrammar,
			List<Grammar> localTempList, List<Grammar> grammars) {

		List<Grammar> result = new ArrayList<Grammar>();
		Grammar grammar = new Grammar();
		String nextElement = getNextElement(inputGrammar);
		if (nextElement.equals(null)) {
			return grammars;
		}
		for (Grammar g : localTempList) {

			if (g.getNonTerminal().equals(nextElement)
					&& !isGrammarExist(g, grammars)) {
				grammar = new Grammar();
				grammar = g.clone();
				String furtherElement = grammar.getProductions().get(0);
				grammar.getProductions().add(0, ".");
				grammar.setSt(push2Stack(g));
				grammars.add(grammar);
				// System.out.println(grammar.getNonTerminal()+"**"+grammar.getProductions());
				if (nonTerminalList.contains(furtherElement))
					grammars.addAll(compZeroSt(grammar,
							cloneGrammar(localTempList), grammars));
			}
		}
		/*
		 * for(Grammar g : localTempList)
		 * System.out.println(g.getNonTerminal()+" -- "+g.getProductions());
		 * for(Grammar g : grammars)
		 * System.out.println(g.getNonTerminal()+" ++ "+g.getProductions());
		 */

		// result = cloneGrammar(grammars);

		return result;
	}

	/*
	 * public List<Grammar> compZeroSt(Grammar inputGrammar, List<Grammar>
	 * localTempList) {
	 * 
	 * List<Grammar> grammars = new ArrayList<Grammar>(); List<Grammar> result =
	 * new ArrayList<Grammar>();
	 * 
	 * int index = inputGrammar.getProductions().indexOf("."); String
	 * nonTerminal = inputGrammar.getProductions().get(index + 1); if
	 * (!nonTerminalList.contains(nonTerminal)) return grammars;
	 * 
	 * Grammar grammar = new Grammar(); List<String> prods = new
	 * ArrayList<String>();
	 * 
	 * for (Grammar g : localTempList) {
	 * 
	 * if (g.getNonTerminal().equals(nonTerminal)) { grammar = new Grammar();
	 * grammar = g; g.setSt(push2Stack(g)); prods = g.getProductions();
	 * prods.add(0, ".");
	 * 
	 * grammar.setProductions(prods);
	 * 
	 * grammars.add(grammar); String nextElement = g.getProductions().get(
	 * g.getProductions().indexOf(".") + 1); if
	 * (!nextElement.equals(nonTerminal)) { grammars.addAll(compZeroSt(g,
	 * localTempList)); } } }
	 * 
	 * result = cloneGrammar(grammars);
	 * 
	 * return result; }
	 */

	private boolean isGrammarExist(Grammar g, List<Grammar> grammars) {
		// TODO Auto-generated method stub
		for (Grammar grammar : grammars) {
			if (grammar.getId() == g.getId())
				return true;
		}
		return false;
	}

	public int getMax(States state) {
		int max = 0;
		for (Grammar g : state.getGrammars()) {
			if (g.getProductions().size() > max) {
				max = g.getProductions().size();
			}
		}
		return max;

	}

	public void printBase(States state) {
		// System.out.println();
		// System.out.println("*************** Base State ******************");
		for (Grammar g : state.getGrammars()) {
			System.out.println(g.getNonTerminal() + "::" + g.getProductions()
					+ " >> " + g.getCount());

			if (!g.getSt().isEmpty())
				System.out.println("****" + g.getSt().peek());

		}

	}

	public void printState() {
		for (States s : resultStates) {
			System.out.println();
			System.out.println("*************** State " + s.getStateName()
					+ "*******************");
			for (Grammar g : s.getGrammars()) {
				System.out.println(g.getNonTerminal() + "::"
						+ g.getProductions() /* +" ++ "+g.getSt() */);

			}
			for (String str : s.getCloseState().keySet()) {
				System.out.println(str + " :: " + s.closeState.get(str));
			}
		}
	}

	public List<Grammar> cloneGrammar(List<Grammar> grammars) {
		List<Grammar> result = new ArrayList<Grammar>();
		Grammar gr = new Grammar();
		for (Grammar g : grammars) {
			gr = new Grammar();
			gr = g.clone();
			result.add(gr);
		}
		return result;
	}

	public void printGrammar() {
		for (Grammar g : grammarList)
			System.out.println(g.getNonTerminal() + ">>" + g.getProductions()
					+ ">>");
	}

	public void printGrammar2() {
		System.out.println(" ");
		for (Grammar g : grammarList2)
			System.out.println(g.getNonTerminal() + ">>" + g.getProductions());
	}

	public Set<String> getUnique(States state) {
		int count = 0;
		Grammar grammar1 = new Grammar();
		Grammar grammar2 = new Grammar();
		HashSet<String> set = new HashSet<String>();
		for (int i = 0; i < state.getGrammars().size(); i++) {
			grammar1 = state.getGrammars().get(i);
			int firstDot = grammar1.getProductions().indexOf(".");
			if (firstDot < state.getGrammars().get(i).getProductions().size() - 1) {
				String element = grammar1.getProductions().get(firstDot + 1);
				for (int j = i; j < state.getGrammars().size(); j++) {
					grammar2 = state.getGrammars().get(j);
					int secondDot = grammar2.getProductions().indexOf(".");
					if (secondDot < state.getGrammars().get(j).getProductions()
							.size() - 1) {
						String element2 = grammar2.getProductions().get(
								secondDot + 1);
						if (element.equals(element2)) {
							set.add(element);
							// System.out.println("Matched ::" +element);
						}
					}
				}
			}
		}
		// System.out.println(set.size());
		return set;
	}

	public List<FirstFollow> cloneFirstFollow() {
		List<FirstFollow> temp = new ArrayList<FirstFollow>();
		for (FirstFollow f : firstFollow) {
			temp.add(f.clone());
		}
		return temp;
	}

	public void stateController() {

		Set<String> temp = new HashSet<String>();
		List<FirstFollow> localFF = cloneFirstFollow();
		// computeStateZero();
		// getFirst();
		// getFollow();
		/*
		 * for (FirstFollow f : getFirst()) { localFF.add(f); }
		 */
		/*
		 * for (FirstFollow f : localFF) {
		 * System.out.println("Non Terminal :: "+f.getNonTerminal());
		 * System.out.println("First ::"+f.getFirst());
		 * System.out.println("Follow :: "+f.getFollow()); }
		 */// System.out.println("*****************************************************************");
			// System.out.println(getFirstFollow());

		for (int i = 0; i < resultStates.size(); i++) {
			resultStates.addAll(computeState(resultStates.get(i))); //
		//	System.out.println("controller size :: " + resultStates.size());
		}

		// resultStates.addAll(computeState(resultStates.get(0)));
		/*
		 * for (States s : resultStates) {
		 * 
		 * temp = new HashSet<String>(); temp = getUnique(s); for (String str :
		 * temp) { if (!s.getCloseState().containsKey(str))
		 * s.getCloseState().put(str, mainStates.get(str)); }
		 * 
		 * }
		 */}

	public List<States> computeState(States parameterState) {
		List<States> results = new ArrayList<States>();
		States localStates = parameterState.clone();
		HashMap<String, String> closeStates = new HashMap<String, String>();
		//System.out.println("**************************");
		// printBase(parameterState);
		int index = localStates.getGrammars().get(0).getCount();
		List<Grammar> grammars = new ArrayList<Grammar>();
		States newState = new States();
		Grammar grammar1 = new Grammar();
		Grammar grammar2 = new Grammar();
		List<String> prods1 = new ArrayList<String>();
		List<String> prods2 = new ArrayList<String>();
		for (int i = 0; i < localStates.getGrammars().size(); i++) {
			grammar1 = new Grammar();
			grammar1 = localStates.getGrammars().get(i);
			// System.out.println("**"+grammar1.getNonTerminal()
			// +" :: "+grammar1.getProductions());
			prods1 = new ArrayList<String>();
			prods1 = grammar1.getProductions();
			int firstDot = prods1.indexOf(".");
			if ((!grammar1.getSt().isEmpty()) && (grammar1.getCount() == index)
					&& (grammar1.flag)) {
				newState = new States();
				newState.setStateName("I" + stateCount);
				grammars = new ArrayList<Grammar>();
				String element1 = (String) grammar1.getSt().pop();
				newState.setCloseItem(element1);

				if (!closeStates.containsKey(element1))
					closeStates.put(element1, newState.getStateName());

				if (!mainStates.containsKey(element1))
					mainStates.put(element1, newState.getStateName());
				grammar1.getProductions().set(firstDot, element1);
				grammar1.getProductions().set(firstDot + 1, ".");
				grammar1.setCount(grammar1.getCount() + 1);
				if (grammar1.getSt().isEmpty()) {
					grammar1.setCloseFlag(true);
				}
				/*System.out.println(grammar1.getNonTerminal() + " :: "
						+ grammar1.getProductions() + "::"
						+ grammar1.getCount() + "::" + grammar1.getSt() + "::"
						+ grammar1.getId() + "::");*/
				grammars.add(grammar1);
				if ((!grammar1.getSt().isEmpty())
						&& (nonTerminalList.contains(grammar1.getSt().peek()))) {

					grammars.addAll(compZeroSt(grammar1,
							cloneGrammar(grammarList), grammars));
				}

				for (int j = i; j < localStates.getGrammars().size(); j++) {
					grammar2 = new Grammar();
					grammar2 = localStates.getGrammars().get(j);
					prods2 = new ArrayList<String>();
					prods2 = grammar2.getProductions();
					int secondDot = prods2.indexOf(".");
					// String element2 = grammar2.getSt().peek();
					if ((!grammar2.getSt().isEmpty())
							&& (grammar2.getSt().peek().equals(element1))) {
						// String element2 = (String) grammar2.getSt().pop();
						grammar2.getProductions().set(secondDot,
								(String) grammar2.getSt().pop());
						grammar2.getProductions().set(secondDot + 1, ".");
						grammar2.setCount(grammar2.getCount() + 1);
						grammars.add(grammar2);
						if ((!grammar2.getSt().isEmpty())
								&& nonTerminalList.contains(grammar2.getSt()
										.peek())) {
							grammars.addAll(compZeroSt(grammar2,
									cloneGrammar(grammarList), grammars));

						}
					}
				}

				newState.setGrammars(grammars);
				stateCount++;
				results.add(newState);
				// System.out.println("*****State Ends****");
			}
		}
		parameterState.setCloseState(closeStates); 
		return results;
	}

	/*
	 * public List<States> computeState(States parameterState) {
	 * 
	 * // System.out.println("********** COmpute***********"); List<States>
	 * result = new ArrayList<States>(); HashMap<String, String> closeStates =
	 * new HashMap<String, String>(); States localState = new States();
	 * localState = parameterState.clone(); int index =
	 * localState.getGrammars().get(0).getCount(); List<Grammar> grammars = new
	 * ArrayList<Grammar>(); // / List<Grammar> localGrammar = new
	 * ArrayList<Grammar>(); States newState = new States(); Grammar grammar1 =
	 * new Grammar(); Grammar grammar2 = new Grammar(); List<String> prods1 =
	 * new ArrayList<String>(); List<String> prods2 = new ArrayList<String>();
	 * for (int i = 0; i < localState.getGrammars().size(); i++) { grammar1 =
	 * new Grammar(); grammar1 = localState.getGrammars().get(i); prods1 = new
	 * ArrayList<String>(); prods1 = grammar1.getProductions(); int firstDot =
	 * prods1.indexOf(".");
	 * 
	 * if ((!grammar1.getSt().isEmpty()) && (grammar1.getCount() == index) &&
	 * (grammar1.flag)) {
	 * 
	 * System.out .println("******New State " + stateCount + "********");
	 * newState = new States(); newState.setStateName("I" + stateCount);
	 * grammars = new ArrayList<Grammar>(); String element1 = (String)
	 * grammar1.getSt().pop(); newState.setCloseItem(element1); if
	 * (!closeStates.containsKey(element1)) closeStates.put(element1,
	 * newState.getStateName());
	 * 
	 * if (!mainStates.containsKey(element1)) mainStates.put(element1,
	 * newState.getStateName()); grammar1.getProductions().set(firstDot,
	 * element1); grammar1.getProductions().set(firstDot + 1, ".");
	 * grammar1.setCount(grammar1.getCount() + 1); if
	 * (grammar1.getSt().isEmpty()) { grammar1.setCloseFlag(true); }
	 * grammars.add(grammar1); if ((!grammar1.getSt().isEmpty()) &&
	 * nonTerminalList.contains(grammar1.getSt().peek())) {
	 * 
	 * grammars.addAll(compZeroSt(grammar1,
	 * cloneGrammar(grammarList),grammars)); }
	 * 
	 * System.out .println(grammar1.getNonTerminal() + " :: " +
	 * grammar1.getProductions() + " :: " + grammar1.getSt()); for (int j = i +
	 * 1; j < localState.getGrammars().size(); j++) {
	 * 
	 * grammar2 = new Grammar(); grammar2 = localState.getGrammars().get(j);
	 * prods2 = new ArrayList<String>(); prods2 = grammar2.getProductions(); int
	 * secondDot = prods2.indexOf(".");
	 * 
	 * if ((!grammar2.getSt().isEmpty()) &&
	 * (element1.equals(grammar2.getSt().peek()))) { // String element2 =
	 * (String)grammar2.getSt().pop(); grammar2.getProductions().set(secondDot,
	 * (String) grammar2.getSt().pop()); grammar2.getProductions().set(secondDot
	 * + 1, "."); grammar2.setCount(grammar2.getCount() + 1);
	 * grammars.add(grammar2); if ((!grammar2.getSt().isEmpty()) &&
	 * nonTerminalList.contains(grammar2.getSt() .peek())) {
	 * grammars.addAll(compZeroSt(grammar2,
	 * cloneGrammar(grammarList),grammars));
	 * 
	 * } } }
	 * 
	 * newState.setGrammars(grammars); stateCount++; result.add(newState);
	 * //System.out.println("*****State Ends****"); }
	 * 
	 * } // System.out.println("size :: "+resultStates.size());
	 * parameterState.setCloseState(closeStates); return result; }
	 */

	public void printMainStates() {
		for (String s : mainStates.keySet()) {
			System.out.println(s + " : : " + mainStates.get(s));
		}
	}

	public void printSLR() {
		for (States s : resultStates) {
			System.out.println("*****************  " + s.getStateName()
					+ " *******");
			for (String nt : s.getNonTerminals().keySet()) {
				System.out.println(nt + " " + s.getNonTerminals().get(nt));
			}
			for (String t : s.getTerminals().keySet()) {
				System.out.println(t + " " + s.getTerminals().get(t));
			}
		}

	}

	public void buildSLRTable() {
		HashMap<String, String> nt = new HashMap<String, String>();
		HashMap<String, String> t = new HashMap<String, String>();

		for (States state : resultStates) {
			nt = new HashMap<String, String>();
			t = new HashMap<String, String>();
			if(state.getStateName().equals("I1"))
				t.put("$", "accept");
	//		System.out.println("**********");
			for (String str : state.getCloseState().keySet()) {
			//	System.out.println(str);
								if (nonTerminalList.contains(str))
					nt.put(str,(String) state.getCloseState().get(str).subSequence(	1,state.getCloseState().get(str).length()));

				else if (terminals.contains(str))
					t.put(str,"s"+ (String) state.getCloseState().get(str).subSequence(1,state.getCloseState().get(str).length()));
			}

			for (Grammar gr : baseState.grammars) {
				if ((state.getGrammars().get(0).closeFlag)
						&& (state.getCloseItem().equals(gr.getProductions()
								.get(gr.getProductions().size() - 1)))
						&& (gr.getId() == state.getGrammars().get(0).getId())) {
					 /* System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^ macthed");
					  System.out.println(gr.getNonTerminal());
					  System.out.println(state.getGrammars().get(0)
					  .getProductions());
*/
					  for (FirstFollow f : firstFollow)
						if (f.getNonTerminal().equals(gr.getNonTerminal()))
							for (String str : f.getFollow())
								t.put(str, "r" + gr.getId());
					break;
				}

			}

			state.setNonTerminals(nt);
			state.setTerminals(t);

		}

	}
	
	public void itemsListWrite() throws IOException
	{
		
		File items = new File("Item_List.txt");
		
		if(!items.exists())
			items.createNewFile();
		
		
		FileWriter fw = new FileWriter(items.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		for(States s : resultStates)
		{
			String string = "**************   "+s.getStateName()+"  *************"+"\n";
			bw.write(string);
			for(Grammar g : s.getGrammars())
			{
				string = g.getNonTerminal() + " :: " + g.getProductions()+"\n";
				bw.write(string);
			}
			string ="\n\n";
			bw.write(string);
			
		}
		bw.close();
	}
	
	public void slrWrite() throws IOException
	{
		
		File items = new File("SLR_Table.txt");
		
		if(!items.exists())
			items.createNewFile();
		
		
		FileWriter fw = new FileWriter(items.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		for(States s : resultStates)
		{
			String string = "**************   State "+s.getStateName().subSequence(1, s.getStateName().length())+"  *************"+"\n";
			bw.write(string);
			
				if(s.getTerminals().keySet()!= null)
				{
					string = "Action :: "+"\n";
					bw.write(string);
					for (String t : s.getTerminals().keySet()) {
						string = t + " :: "+s.getTerminals().get(t)+"\n";
							//System.out.println(t + " " + s.getTerminals().get(t));
						bw.write(string);
					}
				}
					
				
				if(s.getNonTerminals().keySet()!=null)
				{
					string = "Goto :: "+"\n";
					bw.write(string);
					for (String nt : s.getNonTerminals().keySet()) {
						string = nt +" :: "+s.getNonTerminals().get(nt)+"\n";
						//System.out.println(nt + " " + s.getNonTerminals().get(nt));
						bw.write(string);
					}
				}
				

			
			string ="\n\n";
			bw.write(string);
			
		}
		bw.close();
	}
}
