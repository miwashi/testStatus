package net.miwashi.teststatus;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import net.miwashi.receiver.Notification;
import net.miwashi.receiver.NotificationHelper;
import net.miwashi.teststatus.model.Browser;
import net.miwashi.teststatus.model.Group;
import net.miwashi.teststatus.model.Job;
import net.miwashi.teststatus.model.Platform;
import net.miwashi.teststatus.model.Subject;
import net.miwashi.teststatus.repositories.NotificationRepository;

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent>{

	@Autowired
    NotificationHelper notificationHelper;
    
    @Autowired
	NotificationRepository notificationRepository;
	
	@Value("${configuration.initialdata:false}")
    private boolean do_init_data = false;
	
	/**
	 * This method is called during Spring's startup.
	 * 
	 * @param event Event raised when an ApplicationContext gets initialized or
	 * refreshed.
	 **/
	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		List<Notification> notifications = notificationRepository.findAll();
		for(Notification notification : notifications){
			try{
				handleNotification(notification);
			}catch(Throwable e){
				System.out.println(e); 
			}
		}
	}

	
	private void handleNotification(Notification notification){
		Job aJob = notificationHelper.loadJob(notification.getJobName() + "-" + notification.getBuildId(), notification.getJobName(), notification);
        Browser aBrowser = notificationHelper.loadBrowser(notification.getBrowser());
        Platform aPlatform = notificationHelper.loadPlatform(notification.getPlatform());
        Group aGroup = notificationHelper.loadGroup(notification.getTestGroup());
        Group aSubGroup = notificationHelper.loadGroup(notification.getTestSubGroup());
        Subject aSubject = notificationHelper.loadSubject(notification.getTestSubject(), notification.getTestSubjectKey());

        notificationHelper.addRequirement(notification, aGroup, aSubGroup, aSubject, aBrowser, aPlatform, aJob);
	}
}
