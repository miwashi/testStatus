package net.miwashi.teststatus.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import net.miwashi.receiver.Notification;

public interface NotificationRepository extends MongoRepository<Notification, String> {
	
	public Notification findByKey(String key);
    public List<Notification> findByStatus(String status);
    
}
