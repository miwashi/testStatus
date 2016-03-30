package net.miwashi.receiver;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.miwashi.teststatus.model.Browser;
import net.miwashi.teststatus.model.Group;
import net.miwashi.teststatus.model.Job;
import net.miwashi.teststatus.model.Platform;
import net.miwashi.teststatus.model.Requirement;
import net.miwashi.teststatus.model.Result;
import net.miwashi.teststatus.model.Subject;
import net.miwashi.teststatus.repositories.BrowserRepository;
import net.miwashi.teststatus.repositories.FailRepository;
import net.miwashi.teststatus.repositories.GroupRepository;
import net.miwashi.teststatus.repositories.JobRepository;
import net.miwashi.teststatus.repositories.PlatformRepository;
import net.miwashi.teststatus.repositories.RequirementRepository;
import net.miwashi.teststatus.repositories.ResultRepository;
import net.miwashi.teststatus.repositories.SubjectRepository;

@Component
public class NotificationHelper {

	@Autowired
    RequirementRepository requirementRepository;

    @Autowired
    ResultRepository resultRepository;

    @Autowired
    FailRepository failRepository;
    
    @Autowired
    BrowserRepository browserRepository;

    @Autowired
    PlatformRepository platformRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    SubjectRepository subjectRepository;
    
    @Autowired
    JobRepository jobRepository;
    
    
    public Requirement addRequirement(Notification notification, Group aGroup, Group aSubGroup, Subject aSubject, Browser aBrowser, Platform aPlatform, Job aJob){
    	Requirement requirement = new Requirement(notification.getNameAsKey());
        Iterable<Requirement> requirements = requirementRepository.findByKey(requirement.getKey());
        requirement.setStatusChangeDate(notification.getCompleteTime().toDate());
        
        if(requirements.iterator().hasNext()){
            requirement = requirements.iterator().next();            
            //Set date for statuschanges
            if(notification.isSucceeded() != requirement.isStatusPass()){
            	requirement.setStatusChangeDate(notification.getCompleteTime().toDate());
            }
        }
        requirement.setStatusPass(notification.isSucceeded());
        requirement.setGroup(aGroup);
        requirement.setSubGroup(aSubGroup);
        requirement.setSubject(aSubject);
        requirementRepository.save(requirement);

        
        Result result = new Result(notification.getStatus());
        result.setOldKey(notification.getKey());
        result.setBrowser(aBrowser);
        result.setPlatform(aPlatform);
        result.setJob(aJob);
        result.setCompletionTime(notification.getCompleteTime());
        result.setStartTime(notification.getStartTime());
        requirement.add(result);
        
        requirementRepository.save(requirement);
        return requirement;
    }
    
	public Browser loadBrowser(String name){
        if(name==null || name.isEmpty() || "any".equalsIgnoreCase(name) || "default".equalsIgnoreCase(name)){
            name = Browser.DEFAULT_BROWSER;
        }
        name = name.toLowerCase();

        Iterable<Browser> browsers = browserRepository.findByName(name);
        Browser aBrowser = new Browser(name);
        if(browsers.iterator().hasNext()){
            aBrowser = browsers.iterator().next();
        }else{
            browserRepository.save(aBrowser);
        }
        return aBrowser;
    }
	
	public Platform loadPlatform(String name){
        if(name==null || name.isEmpty() || "any".equalsIgnoreCase(name) || "default".equalsIgnoreCase(name)){
            name = Platform.DEFAULT_PLATFORM;
        }
        name = name.toLowerCase();

        Iterable<Platform> platforms = platformRepository.findByName(name);
        Platform aPlatform = new Platform(name);
        if(platforms.iterator().hasNext()){
            aPlatform = platforms.iterator().next();
        }else{
            platformRepository.save(aPlatform);
        }
        return aPlatform;
    }

	public Group loadGroup(String name){
        name = name.toLowerCase();

        Iterable<Group> groups = groupRepository.findByName(name);
        Group aGroup = new Group(name);
        if(groups.iterator().hasNext()){
            aGroup = groups.iterator().next();
        }else{
        }
        aGroup.setLastTested(new Date());
        
        groupRepository.save(aGroup);
        return aGroup;
    }

	public Subject loadSubject(String name, String key){
        name = name.toLowerCase();

        Iterable<Subject> groups = subjectRepository.findByKey(key);
        Subject aSubject = new Subject(name, key);
        if(groups.iterator().hasNext()){
            aSubject = groups.iterator().next();
        }else{
        }
        aSubject.setLastTested(new Date());
        subjectRepository.save(aSubject);
        return aSubject;
    }
    
	public Job loadJob(String key, String name, Notification resultReport){
    	name = name.toLowerCase();
        if(name == null || name.isEmpty()){
        	name = "local";
        	key = "local-" + resultReport.getUuid();
        }
        Iterable<Job> jobs = jobRepository.findByKey(key);
        Job aJob = new Job(key, name);
        
        if(jobs.iterator().hasNext()){
        	aJob = jobs.iterator().next();
        }else{
        	aJob.setBuildNumber(resultReport.getBuildNumber());
            aJob.setGitBranch(resultReport.getGitBranch());
            aJob.setGitCommit(resultReport.getGitCommit());
            aJob.setGitURL(resultReport.getGitURL());
            aJob.setGrid(resultReport.getGrid());
            aJob.setHost(resultReport.getHost());
            aJob.setBrowser(resultReport.getBrowser());
            aJob.setBuildTag(resultReport.getBuildTag());
            aJob.setBuildUrl(resultReport.getBuildUrl());
            aJob.setJenkinsUrl(resultReport.getJenkinsUrl());
            aJob.setPlatform(resultReport.getPlatform());
            aJob.setSize(resultReport.getSize());
            aJob.setUser(resultReport.getUser());
            aJob.setTimeStamp(new Date(resultReport.getStartTime().getMillis()));
        }
        jobRepository.save(aJob);
        return aJob;
    }
}
