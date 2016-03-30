package net.miwashi.teststatus.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import net.miwashi.receiver.FailNotification;
import net.miwashi.receiver.Notification;

public interface FailNotificationRepository extends MongoRepository<FailNotification, String> {
	
	public Notification findByKey(String key);
    public List<FailNotification> findByUuid(String uuid);
    
}
