package net.miwashi.receiver;

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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import net.miwashi.teststatus.TestStatusApplication;
import net.miwashi.teststatus.model.Browser;
import net.miwashi.teststatus.model.Fail;
import net.miwashi.teststatus.model.Group;
import net.miwashi.teststatus.model.Job;
import net.miwashi.teststatus.model.Platform;
import net.miwashi.teststatus.model.Requirement;
import net.miwashi.teststatus.model.Result;
import net.miwashi.teststatus.model.Stats;
import net.miwashi.teststatus.model.Subject;
import net.miwashi.teststatus.repositories.BrowserRepository;
import net.miwashi.teststatus.repositories.FailNotificationRepository;
import net.miwashi.teststatus.repositories.FailRepository;
import net.miwashi.teststatus.repositories.GroupRepository;
import net.miwashi.teststatus.repositories.JobRepository;
import net.miwashi.teststatus.repositories.NotificationRepository;
import net.miwashi.teststatus.repositories.PlatformRepository;
import net.miwashi.teststatus.repositories.RequirementRepository;
import net.miwashi.teststatus.repositories.ResultRepository;
import net.miwashi.teststatus.repositories.SubjectRepository;
import reactor.bus.Event;
import reactor.fn.Consumer;

@Service
public class Receiver implements Consumer<Event<String>> {

	@Autowired
	CountDownLatch latch;

	@Value("${configuration.log.receivedresult:false}")
    private boolean do_log_recievied_results = false;

	private Log log = LogFactory.getLog(TestStatusApplication.class);

    @Autowired
    NotificationHelper notificationHelper;
    
    @Autowired
	NotificationRepository notificationRepository;
    
    @Autowired
	FailNotificationRepository failNotificationRepository;
    
    @Autowired
    ResultRepository resultRepository;

    @Autowired
    FailRepository failRepository;
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    
    private static ConcurrentHashMap<String, Notification> reports = new ConcurrentHashMap<String, Notification>();
    private static List<FailNotification> exceptions = Collections.synchronizedList(new ArrayList<FailNotification>());
    
	public void accept(Event<String> ev) {
		if(do_log_recievied_results){
			System.out.println("Event: " + ev.getData());
		}
		handleNotification(ev.getData());
		latch.countDown();
	}
	
	private void handleNotification(String request){
		if(FailNotification.isException(request)){
        	FailNotification ex = new FailNotification(request);
        	handleFail(ex);
        }else{	
            Notification result = new Notification(request);
            TestStatusApplication.getStats().add(result);
            if (!result.isCompleted()) {
            	reports.put(result.getKey(), result);
            } else {
                Notification oldReport = reports.get(result.getKey());
                reports.remove(result.getKey());
                if (oldReport != null) {
                    oldReport.setStatus(result.getStatus());
                    oldReport.setCompleteTime(result.getStartTime());
                }else{
                	log.warn("Completion received without start!");
                }
                handleCompletionNotification(oldReport);
            }
        }
	}
	
    private void handleFail(FailNotification failNotification) {
    	failNotificationRepository.save(failNotification);
    	Iterable<Result> resultIterator = resultRepository.findByOldKey(failNotification.getKey());
    	if(resultIterator.iterator().hasNext()){
    		Result result = resultIterator.iterator().next();
    		Fail fail = new Fail(failNotification);
    		fail.setResultId(result.getId());
    		failRepository.save(fail);
    		if (do_log_recievied_results) {
                log.info("handled: " + "Connect to " + result.getId() + ": " + failNotification);
            }
    		System.out.println("handled: " + "Connect to " + result.getId() + ": " + failNotification);
    	}else{
    		if (do_log_recievied_results) {
                log.info("FAILED to connect: " + failNotification);
            }
    		System.out.println("FAILED to connect: " + failNotification);
    	}
    }
	
	private void handleCompletionNotification(Notification notification){
		if(notification==null){
			log.warn("Null report received!");
			return;
		}
		
		notificationRepository.save(notification);
		
		Job aJob = notificationHelper.loadJob(notification.getJobName() + "-" + notification.getBuildId(), notification.getJobName(), notification);
        Browser aBrowser = notificationHelper.loadBrowser(notification.getBrowser());
        Platform aPlatform = notificationHelper.loadPlatform(notification.getPlatform());
        Group aGroup = notificationHelper.loadGroup(notification.getTestGroup());
        Group aSubGroup = notificationHelper.loadGroup(notification.getTestSubGroup());
        Subject aSubject = notificationHelper.loadSubject(notification.getTestSubject(), notification.getTestSubjectKey());

        notificationHelper.addRequirement(notification, aGroup, aSubGroup, aSubject, aBrowser, aPlatform, aJob);
	}
    
    public static Map<String, Notification> getIncompleteReports(){
    	return reports;
    }
}