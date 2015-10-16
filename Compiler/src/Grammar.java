import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Grammar {

	String nonTerminal;
	List<String> productions;
	int id;
	Stack<String> st;
	int count;
	boolean flag;
	boolean closeFlag;

	public Grammar() {
		productions = new ArrayList<String>();
		count = 0;
		st = new Stack<String>();
		flag = false;
		closeFlag = false;
	}

	public String getNonTerminal() {
		return nonTerminal;
	}

	public void setNonTerminal(String nonTerminal) {
		this.nonTerminal = nonTerminal;
	}

	public List<String> getProductions() {
		return productions;
	}

	public void setProductions(List<String> productions) {
		this.productions = productions;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Stack getSt() {
		return st;
	}

	public void setSt(Stack st) {
		this.st = st;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public Grammar clone(){
		//System.out.println("cloning");
		Grammar g = new Grammar();
		List<String> xxxx = new ArrayList<String>();
		Stack<String> stack = new Stack<String>();
		List<String> stk = new ArrayList<String>();
		List<String> kk = new ArrayList<String>();
		for(String s: productions) {
			xxxx.add(new String(s));
		}
		//System.out.println("________________________"+st);
		for(String s : st)
		{
			stack.push(new String(s));
		}
		g.setId(new Integer(id));
		g.setSt(stack);
		g.setNonTerminal(nonTerminal);
		g.setProductions(xxxx);
		g.setSt(stack);
		g.setCount(count);
		g.setFlag(flag);
		g.setCloseFlag(closeFlag);
		return g;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public boolean isCloseFlag() {
		return closeFlag;
	}

	public void setCloseFlag(boolean closeFlag) {
		this.closeFlag = closeFlag;
	}

	

}
