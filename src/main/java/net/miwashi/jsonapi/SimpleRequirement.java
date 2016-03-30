package net.miwashi.jsonapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.miwashi.jsonapi.statistics.DurationStat;
import net.miwashi.jsonapi.statistics.LastResult;
import net.miwashi.jsonapi.statistics.RequirementResultSummary;
import net.miwashi.teststatus.model.Requirement;
import net.miwashi.teststatus.model.Result;

public class SimpleRequirement {
	private long id;
	private String name;
	private String readableName;
	private String team;
	private String parameter;
	private SimpleTimeStamp whenStatusChanged;
	private boolean status;
	
	private Map<String, Object> statistics = new HashMap<String, Object>();
	private List<SimpleResult> results = new ArrayList<SimpleResult>();
	
	public SimpleRequirement(long id, String name){
		this.id = id;
		this.name = name;
		
		String readable1 = subjectToReadable(name);
		String readable2 = requirementToReadeable(name);
		readableName = readable1 + " " + readable2;
		parameter = extractParameter(name);
		
		team = requirementToTeam(name);
	}
	
	public SimpleRequirement(Requirement persistentRequirement) {
		id = persistentRequirement.getId();
		name = persistentRequirement.getKey();
		
		String readable1 = subjectToReadable(name);
		String readable2 = requirementToReadeable(name);
		readableName = readable1 + " " + readable2;
		parameter = extractParameter(name);	
		
		whenStatusChanged = new SimpleTimeStamp(persistentRequirement.getStatusChangeDate());
		status = persistentRequirement.isStatusPass();
		
		team = requirementToTeam(persistentRequirement.getGroup()!=null?persistentRequirement.getGroup().getName():"");
	}

	public long getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public String getReadableName() {
		return readableName;
	}

	public String getTeam(){
		return team;
	}
	
	public String getParameter() {
		return parameter;
	}
	
	public SimpleTimeStamp getWhenStatusChanged() {
		return whenStatusChanged;
	}

	public boolean isStatusPass() {
		return status;
	}

	public Map<String, Object> getStatistics(){
		return statistics;
	}
	
	public List<SimpleResult> getResults(){
		return results;
	}
	
	public void add(List<Result> persistentResults){
		for(Result persistentResult: persistentResults){
			add(persistentResult);
		}
		statistics.put("duration",new DurationStat(persistentResults));
		statistics.put("summary", new RequirementResultSummary(persistentResults));
		//statistics.put("lastresult", new LastResult(persistentResults));		
	}
	
	public void add(Result persistentResult){
		SimpleResult result = new SimpleResult(persistentResult);
		results.add(result);
	}
	
	public int getNumberOfResults(){
		return results!=null?results.size():0;
	}
	
	private String requirementToReadeable(String methodName){
		String requirement = methodName;
		
		//Remove parameter if any
		if((requirement.indexOf("[")>=0)&&(requirement.indexOf("]")>=0)){
			requirement = requirement.substring(0, requirement.indexOf("["));
		}
		
		//If no packages, then return
		if(requirement.indexOf(".")<0){
            return requirement;
        }
		requirement = requirement.replaceAll("TMA", "Tma");
		requirement = requirement.replaceAll("VMA", "Vma");
		requirement = requirement.replaceAll("NSS", "Nss");
		requirement = requirement.replaceAll("SVT", "Svt");
		requirement = requirement.replaceAll("ATV", "Atv");
		requirement = requirement.replaceAll("TV", "Tv");
		requirement = requirement.replaceAll("CSS", "Css");
		requirement = requirement.replaceAll("RSS", "Rss");
		requirement = requirement.replaceAll("URL", "Url");
		requirement = requirement.replaceAll("PopUp", "Popup");
		requirement = requirement.replaceAll("SvtHeader", "Svtheader");
		
		//Clean
		requirement = requirement.substring(requirement.lastIndexOf("."));
		requirement = requirement.replaceAll("\\.", "");
		requirement = requirement.replaceAll("(.)([A-Z])", "$1 $2");
		requirement = requirement.replaceAll("/(\\d+)([a-z]+)/g","$1 $2");
		requirement = requirement.replaceAll("([a-z]+)(\\d+)","$1 $2");
		requirement = requirement.toLowerCase();
		
		requirement = requirement.replaceAll(" tma", " TMA");
		requirement = requirement.replaceAll(" vma", " VMA");
		
		return requirement;
	}
	
	private String requirementToTeam(String methodName){
		String team = methodName;
		
		return team;
	}
	
	private String subjectToReadable(String methodName){
		String subject = "";
		//Remove parameter if any
		if((methodName.indexOf("[")>=0)&&(methodName.indexOf("]")>=0)){
			methodName = methodName.substring(0, methodName.indexOf("["));
		}
		
		//Remove methodname
		if(methodName.indexOf(".")>=0){
			methodName = methodName.substring(0, methodName.lastIndexOf("."));
		}
		
		//Find subject
		if(methodName.indexOf(".")>=0){
	        subject = methodName.substring(methodName.lastIndexOf("."));
        }
		subject = subject.replaceAll("Test", "");
		subject = subject.replaceAll("VTma", "Vtma");
		subject = subject.replaceAll("TMA", "Tma");
		subject = subject.replaceAll("VMA", "Vma");
		subject = subject.replaceAll("NSS", "Nss");
		subject = subject.replaceAll("SVT", "Svt");
		subject = subject.replaceAll("ATV", "Atv");
		subject = subject.replaceAll("TV", "Tv");
		subject = subject.replaceAll("CSS", "Css");
		subject = subject.replaceAll("RSS", "Rss");
		subject = subject.replaceAll("URL", "Url");
		subject = subject.replaceAll("PopUp", "Popup");
		subject = subject.replaceAll("SvtHeader", "Svtheader");

		subject = subject.replaceAll("\\.", "");
		subject = subject.replaceAll("(.)([A-Z])", "$1 $2");
		subject = subject.replaceAll("(\\d+)([a-z]+)","$1 $2");
		subject = subject.replaceAll("([a-z]+)(\\d+)","$1 $2");
		subject = subject.toLowerCase();
		
		subject = subject.replaceAll("svt world", "Svt-World");
		return subject;
	}
	
	private String extractParameter(String methodName){
		String parameter = "";
		if((methodName.indexOf("[")>=0)&&(methodName.indexOf("]")>=0)){
        	parameter = methodName.substring(methodName.indexOf("[") + 1, methodName.lastIndexOf("]"));
        }
		return parameter;
	}
}
