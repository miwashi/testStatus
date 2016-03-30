package net.miwashi.jsonapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.miwashi.jsonapi.statistics.AbstractMeta;
import net.miwashi.jsonapi.statistics.AbstractStatistics;

public abstract class AbstractSummary {
	
	private String name = "";
	private Map<String, AbstractStatistics> statistics = new HashMap<String, AbstractStatistics>();
	private Map<String, AbstractMeta> meta = new HashMap<String, AbstractMeta>();
	SimpleLink links = new SimpleLink();
	
	public AbstractSummary(String name){
		this.name = name;
	}
	
	public Map<String, AbstractStatistics> getStatistics() {
		return statistics;
	}
	
	public Map<String, AbstractMeta> getMeta() {
		return meta;
	}
	
	public List<SimpleLink.Ref> getLinks(){
		return links.getRefs();
	}
	
	public String getName(){
		return name;
	}
}
