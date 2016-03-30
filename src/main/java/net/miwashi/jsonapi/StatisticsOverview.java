package net.miwashi.jsonapi;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.miwashi.jsonapi.statistics.AbstractStatistics;
import net.miwashi.jsonapi.statistics.JobStat;
import net.miwashi.jsonapi.statistics.KeyStat;
import net.miwashi.jsonapi.summaries.AbstractOverview;
import net.miwashi.teststatus.model.Requirement;
import net.miwashi.teststatus.model.Result;

public class StatisticsOverview extends AbstractOverview{
	
	private static final String KEY_URL = "urls";
	private static final String KEY_HOST = "hosts";
	private static final String KEY_PATH = "paths";
	private static final String KEY_TEAM = "teams";
	private static final String KEY_JOB = "jobs";
	
	private String[] statKeys = new String[]{
			KEY_URL
			,KEY_HOST
			,KEY_PATH
			,KEY_TEAM
			,KEY_JOB
	};
	
	private Map<Long, SimpleRequirement> requirements = new HashMap<Long, SimpleRequirement>();
	
	public StatisticsOverview(){
		super("statistics");
		for(String key : statKeys){
			getStatistics().put(key, new KeyStat(key));
		}
	}
	
	/**
	 * Adds requirements to overview.
	 * @param requirement
	 */
	public void add(Requirement requirement){
		Long id = requirement.getId();
		URLRequirement urlRequirement = new URLRequirement(requirement);
		if(id==null || urlRequirement ==null){
			return;
		}
		urlRequirement.add(requirement.getResults());
		requirements.put(id, urlRequirement);
		try {
			URL url = new URL(requirement.getParameter());
			getStat(KEY_URL).add(url.toString(), requirement);
			getStat(KEY_HOST).add(url.getHost(), requirement);
			
			String path = url.getPath();
			path = path.startsWith("/")?path.replaceFirst("/", ""):path;
			path = (path.indexOf("/")>0)?path.substring(0, path.indexOf("/")):path;
			getStat(KEY_PATH).add(path, requirement);
			
		} catch (MalformedURLException ignore) {}
		
		String team = requirement.getGroup().getName();
		getStat(KEY_TEAM).add(team, requirement);
		
		for(Result result : requirement.getResults()){
			String job = result.getJob().getName() + "-" + result.getJob().getBuildNumber();
			AbstractStatistics stat = getStat(KEY_JOB);
			if(stat == null){
				
			} else if(stat instanceof JobStat){
				((JobStat) stat).add(job, result);
			}
		}
	}
	
	private KeyStat getStat(String key){
		KeyStat stat = (KeyStat) getStatistics().get(key);
		if(stat==null){
			stat = new KeyStat(key);
		}
		return stat;
	}
	
	public Collection<SimpleRequirement> getRequirements(){
		if(requirements==null){
			return new ArrayList<SimpleRequirement>();
		}
		return requirements.values();
	}
}
