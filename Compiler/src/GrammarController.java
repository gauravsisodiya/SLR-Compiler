import java.awt.color.CMMException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class GrammarController {

	List<Grammar> grammarList;
	public List<String> nonTerminalList;
	public List<String> terminalList;
	// List<FirstFollow> firstFollow ;
	String root;
	int count;

	public GrammarController() {
		super();
		this.grammarList = new ArrayList<Grammar>();
		this.nonTerminalList = new ArrayList<String>();
		this.terminalList = new ArrayList<String>();
		// this.firstFollow = new ArrayList<FirstFollow>();
		this.root = "";
		this.count = 1;
	}

	public List<Grammar> readGrammer() {
		try {
			grammarList = new ArrayList<Grammar>();
			String string;
			Grammar grammar;
			List<String> productions;
			BufferedReader bufferedReader = new BufferedReader(new FileReader(
					"GrammarList.txt"));
			int count = 1;
			StringTokenizer st;
			String secondPart;
			String firstPart;
			StringTokenizer stringTokenizer;
			boolean flag = true;
			while ((string = bufferedReader.readLine()) != null) {
				stringTokenizer = new StringTokenizer(string, "::");
				firstPart = stringTokenizer.nextToken();
				secondPart = stringTokenizer.nextToken();
				firstPart = firstPart.replaceAll("\\s+", "");
				if (flag)
					root = firstPart;
				flag = false;
				st = new StringTokenizer(secondPart);
				productions = new ArrayList<String>();
				while (st.hasMoreTokens()) {
					productions.add(st.nextToken());
				}
				grammar = new Grammar();
				grammar.setNonTerminal(firstPart);
				grammar.setProductions(productions);
				grammar.setId(count);
				//grammar.

				count++;
				grammarList.add(grammar);

			}

			grammar = new Grammar();
		//	System.out.println("***"+grammarList.get(0).getNonTerminal() + grammarList.get(0).getId());
			grammar.setNonTerminal(grammarList.get(0).getNonTerminal() + "'");
			productions = new ArrayList<String>();
			productions.add(grammarList.get(0).getNonTerminal());
			grammar.setProductions(productions);
			grammar.setId(0);
			grammarList.add(grammar);
			// System.out.println("************************************ "+root);
			// return grammarList;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return grammarList;

	}

	public List<String> readNonTerminals() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(
					"nonTerminalList.txt"));
			String string;
			while ((string = bufferedReader.readLine()) != null) {
				string = string.replaceAll("\\s+", "");
				nonTerminalList.add(string);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nonTerminalList;
	}

	public List<String> readTerminals() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(
					"terminalList.txt"));
			String string;
			while ((string = bufferedReader.readLine()) != null) {
				string = string.replaceAll("\\s+", "");
				terminalList.add(string.trim());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return terminalList;
	}

	public Set<String> computeFirst(String nonTerminal, int id) {
		Set<String> results = new HashSet<String>();
		Set<String> temp = new HashSet<String>();

		for (Grammar g : grammarList) {
			if (g.getNonTerminal().equals(nonTerminal) && g.getId() != 0) {
				// System.out.println("nont ::" + g.getNonTerminal());
				// System.out.println("prods :: " + g.getProductions());
				if (terminalList.contains(g.getProductions().get(0))) {
					// System.out.println("--" + g.getProductions());
					results.add(g.getProductions().get(0));
				} else if (g.getId() != id) {
					// System.out.println("" + g.getProductions());
					if (g.getProductions().get(0).equals("empty")) {
						results.add("empty");
					} else
						temp = computeFirst(g.getProductions().get(0),
								g.getId());
					results.addAll(temp);
					// System.out.println("()()()********************"+temp);
					if (temp.contains("empty")) {
						if (g.getProductions().size() > 1)
							results.addAll(computeFirst(
									g.getProductions().get(1), g.getId()));
						// System.out.println("*****************got it"+g.getProductions());}
					}

				}

			}
		}
		return results;
	}

	public List<FirstFollow> getFirst() {
		List<FirstFollow> firstList = new ArrayList<FirstFollow>();
		Set<String> first;
		FirstFollow f = new FirstFollow();
		for (String nT : nonTerminalList) {
			f = new FirstFollow();
			first = computeFirst(nT, 0);
			f.setNonTerminal(nT);
			f.setFirst(first);
			firstList.add(f);

		}
		return firstList;
	}

	public void printFirst(List<FirstFollow> first) {
		for (FirstFollow f : first) {
			//System.out.println("First :: ");
			//System.out.println(f.getNonTerminal() + "::" + f.getFirst());
			//System.out.println("Follow :: ");
			System.out.println(f.getNonTerminal() + ">>" + f.getFollow());
		}

	}

	public List<FirstFollow> getFirstFollow() {
		List<FirstFollow> firstList = getFirst();
		getFollow(firstList);
		
		return firstList;
	}

	public List<FirstFollow> getFollow(List<FirstFollow> firstList) {
		List<FirstFollow> followList = new ArrayList<FirstFollow>();
		Set<String> follow = new HashSet<String>();
		for (String nonT : nonTerminalList) {
			for (FirstFollow f : firstList) {
				if (f.getNonTerminal().equals(nonT)) {
					follow = new HashSet<String>();
					if (firstList.get(0).getNonTerminal().equals(nonT)) {
						follow.add("$");
					}
					follow.addAll(computeFollow(nonT, firstList));
					f.setFollow(follow);
				}
			}
		}
		return firstList;
	}

	public Set<String> computeFollow(String nT, List<FirstFollow> firstList) {
		Set<String> follow = new HashSet<String>();
		
		for (Grammar g : grammarList) {

			if (g.getProductions().contains(nT) && g.getId() != 0) {
				if (g.getProductions().indexOf(nT) < g.getProductions().size() - 1) {
					int index = g.getProductions().indexOf(nT) + 1;
					String element = g.getProductions().get(index);
					if (terminalList.contains(element)) {
						follow.add(element);
					} else if (nonTerminalList.contains(element)) {
						follow.addAll(getComputedFirst(element, firstList));
						follow.remove("empty");
					}
				} else if (g.getProductions().indexOf(nT) == g.getProductions()
						.size() - 1) {
					if (getComputedFollow(g.getNonTerminal(), firstList) != null)
					{	follow.addAll(getComputedFollow(g.getNonTerminal(),
								firstList));
						follow.remove("empty");
					}
					
				}
			}

		}

		return follow;

	}

	public Set<String> getComputedFirst(String nT, List<FirstFollow> firstList) {
		for (FirstFollow ff : firstList)
			if (ff.getNonTerminal().equals(nT))
				return ff.getFirst();

		return null;
	}

	public Set<String> getComputedFollow(String nT, List<FirstFollow> firstList) {
		for (FirstFollow ff : firstList)
			if (ff.getNonTerminal().equals(nT))
				return ff.getFollow();

		return null;
	}

	public void printTerminals() {
		// System.out.println();
		for (String s : terminalList)
			System.out.println("==" + s);
	}

	public void printGrammar() {
		for (Grammar g : grammarList)
			System.out.println(g.getNonTerminal() + ">>" + g.getProductions());
	}
	
	public void writeFirstFollow() throws IOException
	{
		List<FirstFollow> list = getFirstFollow();
		File first = new File("First.txt");
		File follow = new File("Follow.txt");
		if(!first.exists())
			first.createNewFile();
		
		if(!follow.exists())
			follow.createNewFile();
		
		
		FileWriter fw = new FileWriter(first.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		FileWriter fw1 = new FileWriter(follow.getAbsoluteFile());
		BufferedWriter bw1 = new BufferedWriter(fw1);
		
		for(FirstFollow f : list)
		{
			String string = f.getNonTerminal() + " :: " + f.getFirst() +"\n";
			bw.write(string);
			
			String str = f.getNonTerminal() + " :: "+f.getFollow() +"\n";
			bw1.write(str);
		}
		bw.close();
		bw1.close();
	}

}