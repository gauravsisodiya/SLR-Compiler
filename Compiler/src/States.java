import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class States {

	String stateName;
	List<Grammar> grammars;
	boolean flag;
	HashMap<String, String> closeState;
	HashMap<String, String> terminals;
	HashMap<String, String> nonTerminals;
	String closeItem;
	

	// Stack st;

	public States() {
		stateName = "";
		grammars = new ArrayList<Grammar>();
		closeState = new HashMap<String, String>();
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public List<Grammar> getGrammars() {
		return grammars;
	}

	public void setGrammars(List<Grammar> grammars) {
		this.grammars = grammars;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public HashMap<String, String> getCloseState() {
		return closeState;
	}

	public void setCloseState(HashMap<String, String> closeState) {
		this.closeState = closeState;
	}

	public States clone() {

		States s = new States();
		s.setStateName(stateName);
		List<Grammar> ssss = new ArrayList<Grammar>();
		HashMap<String, String> map = new HashMap<String, String>();
		for (Grammar x : grammars) {
			ssss.add(x.clone());
		}
		s.setGrammars(ssss);

		for (String str : closeState.keySet()) {
			map.put(new String(str), new String(closeState.get(str)));
		}
		s.setCloseState(map);
		s.setCloseItem(closeItem);

		return s;
	}

	public HashMap<String, String> getTerminals() {
		return terminals;
	}

	public void setTerminals(HashMap<String, String> terminals) {
		this.terminals = terminals;
	}

	public HashMap<String, String> getNonTerminals() {
		return nonTerminals;
	}

	public void setNonTerminals(HashMap<String, String> nonTerminals) {
		this.nonTerminals = nonTerminals;
	}

	public String getCloseItem() {
		return closeItem;
	}

	public void setCloseItem(String closeItem) {
		this.closeItem = closeItem;
	}

}
