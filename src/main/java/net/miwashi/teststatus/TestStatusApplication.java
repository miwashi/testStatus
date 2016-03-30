package net.miwashi.teststatus;

import static reactor.bus.selector.Selectors.$;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;

import net.miwashi.receiver.Receiver;
import net.miwashi.teststatus.model.Stats;
import net.miwashi.teststatus.model.User;
import net.miwashi.teststatus.repositories.UserRepository;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.io.encoding.StandardCodecs;
import reactor.net.netty.udp.NettyDatagramServer;
import reactor.net.udp.DatagramServer;
import reactor.net.udp.spec.DatagramServerSpec;
import reactor.spring.context.config.EnableReactor;

@SpringBootApplication
@ConfigurationProperties
@EnableReactor
@ComponentScan({"net.miwashi.teststatus", "net.miwashi.receiver"})
public class TestStatusApplication {

    @Value("${configuration.log.receivedresult:false}")
    private boolean do_log_recievied_results = false;

    @Value("${configuration.udp.port:6500}")
    private int udpPort = 6500;

    private Log log = LogFactory.getLog(TestStatusApplication.class);
    private static Stats stats = new Stats();

    public static Stats getStats(){
        return stats;
    }

    @Bean
    InitializingBean seedDatabase(final UserRepository repository){
        return () -> {
          repository.save(new User("miwa","miwa"));
            repository.save(new User("diwa","diwa"));
            repository.save(new User("fiwa","fiwa"));
        };
    }

    @Bean
    CommandLineRunner exampleQuery(UserRepository repository){
        return args ->
                repository.findByName("miwa").forEach(System.out::println);
    }

    @Value("${configuration.projectName}")
    void setProjectName(String projectName){
        System.out.println("Setting name to " + projectName);
    }

    @Autowired
    void setEnvrironment(Environment env){
        System.out.println("Setting environment: " + env.getProperty("configuration.projectName"));
    }

    public TestStatusApplication(){
        super();
    }

    /**
     * "0 0 * * * *" = the top of every hour of every day.
     * "* /10 * * * * *" = every ten seconds.
     * "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
     * "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.
     * "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
     * "0 0 0 25 12 ?" = every Christmas Day at midnight
     * second, minute, hour, day of month, month, day(s) of week
     */
    @Scheduled(cron = "${configuration.schedule.hourly.cron}")
    public void hourlyManagement(){
        stats.resetHour();
    }

    @Scheduled(cron = "${configuration.schedule.daily.cron}")
    public void daylyManagement(){
        stats.resetDay();
    }


    @Bean
    reactor.Environment env() {
        return reactor.Environment.initializeIfEmpty().assignErrorJournal();
    }
    
    @Bean
    EventBus createEventBus(reactor.Environment env) {
	    return EventBus.create(env, reactor.Environment.THREAD_POOL);
    }
    
    @Autowired
	private EventBus eventBus;
    
    @Autowired
	CountDownLatch latch;
    
    @Bean
    public DatagramServer<byte[], byte[]> datagramServer(reactor.core.Environment env) throws InterruptedException {
        final DatagramServer<byte[], byte[]> server = new DatagramServerSpec<byte[], byte[]>(NettyDatagramServer.class)
                .env(env)
                //.listen(SocketUtils.findAvailableTcpPort())
                .listen(udpPort)
                .codec(StandardCodecs.BYTE_ARRAY_CODEC)
                .consumeInput(bytes -> {
                    String request = new String(bytes);
                    long start = System.currentTimeMillis();
            		eventBus.notify("results", Event.wrap(request));
            		try {
						latch.await();
					} catch (Exception ignore) {}
            		long elapsed = System.currentTimeMillis() - start;
                })
                .get();
        server.start().await();
        return server;
    }

    @Bean
    public CountDownLatch latch() {
        return new CountDownLatch(1);
    }
    
    public static void main(String[] args) throws InterruptedException  {
        ApplicationContext ctx = SpringApplication.run(TestStatusApplication.class, args);
        
        CountDownLatch latch = ctx.getBean(CountDownLatch.class);
        latch.await(1, TimeUnit.SECONDS);
        
        EventBus bus = ctx.getBean(EventBus.class);
		Receiver receiver = ctx.getBean(Receiver.class);
		bus.on($("results"), receiver);
    }
}
