package net.miwashi.jsonapi;

import java.util.ArrayList;
import java.util.List;

public class SimpleLink {
	
	public class Ref{
		String to;
		String name;
	
		public Ref(String to, String name){
			this.to = to;
			this.name = name;
		}
		
		public String getTo() {
			return to;
		}
		public String getName() {
			return name;
		}
	}
	
	
	private List<Ref> refs = new ArrayList<Ref>();
	
	public SimpleLink(Ref ... newRefs){
	}
	
	public List<Ref> getRefs(){
		return refs;
	}
	
	public SimpleLink add(Ref ... newRefs){
		for(Ref ref : newRefs){
			refs.add(ref);
		}
		return this;
	}
}
