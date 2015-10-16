import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FirstFollow {

	String nonTerminal;
	Set<String> first;
	Set<String> follow;
	
	public String getNonTerminal() {
		return nonTerminal;
	}
	public void setNonTerminal(String nonTerminal) {
		this.nonTerminal = nonTerminal;
	}
	public Set<String> getFirst() {
		return first;
	}
	public void setFirst(Set<String> first) {
		this.first = first;
	}
	public Set<String> getFollow() {
		return follow;
	}
	public void setFollow(Set<String> follow) {
		this.follow = follow;
	}
	
	public FirstFollow clone()
	{
	
		FirstFollow ff = new FirstFollow();
		ff.setNonTerminal(new String(nonTerminal));
		Set<String> f1 = new HashSet<String>();
		Set<String> f2 = new HashSet<String>();
		
		for(String s : first)
		{
			f1.add(new String(s));
		}
		ff.setFirst(f1);
		for(String s :follow)
		{
			f2.add(new String(s));
		}
		ff.setFollow(f2);
		
		return ff;
	}
	
	
}
