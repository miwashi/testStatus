package com.miwashi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.miwashi.model.Browser;
import com.miwashi.model.ExceptionReport;
import com.miwashi.model.Fail;
import com.miwashi.model.Group;
import com.miwashi.model.Job;
import com.miwashi.model.Platform;
import com.miwashi.model.Requirement;
import com.miwashi.model.Result;
import com.miwashi.model.ResultReport;
import com.miwashi.model.Stats;
import com.miwashi.model.Subject;
import com.miwashi.repositories.BrowserRepository;
import com.miwashi.repositories.FailRepository;
import com.miwashi.repositories.GroupRepository;
import com.miwashi.repositories.JobRepository;
import com.miwashi.repositories.PlatformRepository;
import com.miwashi.repositories.RequirementRepository;
import com.miwashi.repositories.ResultRepository;
import com.miwashi.repositories.SubjectRepository;

import reactor.bus.Event;
import reactor.fn.Consumer;

@Service
public class Receiver implements Consumer<Event<String>> {

	@Autowired
	CountDownLatch latch;

	@Value("${configuration.log.receivedresult:false}")
    private boolean do_log_recievied_results = false;

	private Log log = LogFactory.getLog(TestStatusApplication.class);

    private static final String DEFAULT_BROWSER = "firefox";
    private static final String DEFAULT_PLATFORM = "linux";
    
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
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    
    private static ConcurrentHashMap<String, ResultReport> reports = new ConcurrentHashMap<String, ResultReport>();
    private static List<ExceptionReport> exceptions = Collections.synchronizedList(new ArrayList<ExceptionReport>());
    
	public void accept(Event<String> ev) {
		if(do_log_recievied_results){
			System.out.println("Event: " + ev.getData());
		}
		handleReport(ev.getData());
		latch.countDown();
	}
	
	private void handleReport(String request){
		if(ExceptionReport.isException(request)){
        	ExceptionReport ex = new ExceptionReport(request);
        	handleFail(ex);
        }else{	
            ResultReport result = new ResultReport(request);
            TestStatusApplication.getStats().add(result);
            
            if (!result.isCompleted()) {
            	reports.put(result.getKey(), result);
            } else {
                ResultReport oldReport = reports.get(result.getKey());
                reports.remove(result.getKey());
                if (oldReport != null) {
                    oldReport.setStatus(result.getStatus());
                    oldReport.setCompleteTime(result.getStartTime());
                }else{
                	log.warn("Completion received without start!");
                }
                handleCompleteReport(oldReport);
            }
        }
	}
	
    private void handleFail(ExceptionReport exceptionReport) {
    	Iterable<Result> resultIterator = resultRepository.findByOldKey(exceptionReport.getKey());
    	if(resultIterator.iterator().hasNext()){
    		Result result = resultIterator.iterator().next();
    		Fail fail = new Fail(exceptionReport);
    		fail.setResultId(result.getId());
    		failRepository.save(fail);
    		if (do_log_recievied_results) {
                log.info("handled: " + "Connect to " + result.getId() + ": " + exceptionReport);
            }
    		System.out.println("handled: " + "Connect to " + result.getId() + ": " + exceptionReport);
    	}else{
    		if (do_log_recievied_results) {
                log.info("FAILED to connect: " + exceptionReport);
            }
    		System.out.println("FAILED to connect: " + exceptionReport);
    	}
    }
	
	private void handleCompleteReport(ResultReport resultReport){
		Job aJob = loadJob(resultReport.getJobName() + "-" + resultReport.getBuildId(), resultReport.getJobName(), resultReport);
        Browser aBrowser = loadBrowser(resultReport.getBrowser());
        Platform aPlatform = loadPlatform(resultReport.getPlatform());
        Group aGroup = loadGroup(resultReport.getTestGroup());
        Group aSubGroup = loadGroup(resultReport.getTestSubGroup());
        Subject aSubject = loadSubject(resultReport.getTestSubject(), resultReport.getTestSubjectKey());

        Requirement requirement = new Requirement(resultReport.getNameAsKey());
        Iterable<Requirement> requirements = requirementRepository.findByKey(requirement.getKey());
        requirement.setStatusChangeDate(resultReport.getCompleteTime().toDate());
        
        if(requirements.iterator().hasNext()){
            requirement = requirements.iterator().next();            
            //Set date for statuschanges
            if(resultReport.isSucceeded() != requirement.isStatusPass()){
            	requirement.setStatusChangeDate(resultReport.getCompleteTime().toDate());
            }
        }
        requirement.setStatusPass(resultReport.isSucceeded());
        requirement.setGroup(aGroup);
        requirement.setSubGroup(aSubGroup);
        requirement.setSubject(aSubject);
        requirementRepository.save(requirement);

        
        Result result = new Result(resultReport.getStatus());
        result.setOldKey(resultReport.getKey());
        result.setBrowser(aBrowser);
        result.setPlatform(aPlatform);
        result.setJob(aJob);
        result.setCompletionTime(resultReport.getCompleteTime());
        result.setStartTime(resultReport.getStartTime());
        requirement.add(result);
        
        requirementRepository.save(requirement);
	}

	private Browser loadBrowser(String name){
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

    private Platform loadPlatform(String name){
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

    private Group loadGroup(String name){
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

    private Subject loadSubject(String name, String key){
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
    
    private Job loadJob(String key, String name, ResultReport resultReport){
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
    
    public static Map<String, ResultReport> getIncompleteReports(){
    	return reports;
    }
}