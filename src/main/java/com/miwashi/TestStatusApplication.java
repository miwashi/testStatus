package com.miwashi;

import com.miwashi.model.*;
import com.miwashi.repositories.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.context.annotation.ComponentScan;
import reactor.io.encoding.StandardCodecs;
import reactor.net.netty.udp.NettyDatagramServer;
import reactor.net.udp.DatagramServer;
import reactor.net.udp.spec.DatagramServerSpec;
import reactor.spring.context.config.EnableReactor;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@ConfigurationProperties
@EnableReactor
@ComponentScan
public class TestStatusApplication {

    @Value("${configuration.log.receivedresult:false}")
    private boolean do_log_recievied_results = false;

    @Value("${configuration.udp.port:6500}")
    private int udpPort = 6500;

    private Log log = LogFactory.getLog(TestStatusApplication.class);
    private static Stats stats = new Stats();

    private static final String DEFAULT_BROWSER = "firefox";
    private static final String DEFAULT_PLATFORM = "linux";

    @Autowired
    RequirementRepository requirementRepository;

    @Autowired
    ResultRepository resultRepository;

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
    private static ConcurrentHashMap<String, Group> groups = new ConcurrentHashMap<String, Group>();
    private static ConcurrentHashMap<String, Group> subgroups = new ConcurrentHashMap<String, Group>();
    private static ConcurrentHashMap<String, Subject> subjects = new ConcurrentHashMap<String, Subject>();

    public static Map<String, ResultReport> getIncompleteReports(){
        Map<String, ResultReport> result = new HashMap<String, ResultReport>();

        reports.values().forEach(item -> {
            if (!item.isCompleted()) {
                result.put(item.getKey(), item);
            }
        });
        return result;
    }

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


    @Scheduled(fixedRateString = "${configuration.schedule.handleresults.rate}", initialDelayString = "${configuration.schedule.daily.delay}")
    public void handleResultReports() {
        List<ResultReport> toBeHandled = new ArrayList<ResultReport>();
        List<String> toBeDeleted = new ArrayList<String>();

        reports.values().forEach(result -> {
            if(result.getCompleteTime()!=null){
                toBeHandled.add(result);
                toBeDeleted.add(result.getKey());
            }
        });
        toBeDeleted.forEach( key -> {
            reports.remove(key);
        });
        toBeHandled.forEach(result -> {
            handleResult(result);
        });
    }

    private void handleResult(ResultReport resultReport) {
    	Job aJob = loadJob(resultReport.getBuildId(), resultReport.getJobName(), resultReport);
        Browser aBrowser = loadBrowser(resultReport.getBrowser());
        Platform aPlatform = loadPlatform(resultReport.getPlatform());
        Group aGroup = loadGroup(resultReport.getTestGroup());
        Group aSubGroup = loadGroup(resultReport.getTestSubGroup());
        Subject aSubject = loadSubject(resultReport.getTestSubject(), resultReport.getTestSubjectKey());

        Requirement requirement = new Requirement(resultReport.getNameAsKey());
        Iterable<Requirement> requirements = requirementRepository.findByKey(requirement.getKey());
        if(requirements.iterator().hasNext()){
            requirement = requirements.iterator().next();
        }
        requirement.setLastTested(new Date());
        requirement.setGroup(aGroup);
        requirement.setSubGroup(aSubGroup);
        requirement.setSubject(aSubject);
        requirementRepository.save(requirement);

        Result result = new Result(resultReport.getStatus());
        result.setBrowser(aBrowser);
        result.setPlatform(aPlatform);
        result.setJob(aJob);
        result.setCompletionTime(resultReport.getCompleteTime());
        result.setStartTime(resultReport.getStartTime());
        requirement.add(result);
        requirementRepository.save(requirement);
    }

    private Browser loadBrowser(String name){
        if(name==null || name.isEmpty()){
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
        if(name==null || name.isEmpty()){
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
        }
        jobRepository.save(aJob);
        return aJob;
    }

    @Bean
    public DatagramServer<byte[], byte[]> datagramServer(reactor.core.Environment env) throws InterruptedException {

        final DatagramServer<byte[], byte[]> server = new DatagramServerSpec<byte[], byte[]>(NettyDatagramServer.class)
                .env(env)
                //.listen(SocketUtils.findAvailableTcpPort())
                .listen(udpPort)
                .codec(StandardCodecs.BYTE_ARRAY_CODEC)
                .consumeInput(bytes -> {
                    String request = new String(bytes);
                    if (do_log_recievied_results) {
                        log.info("received: " + request);
                    }
                    ResultReport result = new ResultReport(request);
                    stats.inc(result);
                    if (result.isCompleted()) {
                        ResultReport oldReport = reports.get(result.getKey());
                        if (oldReport != null) {
                            oldReport.setStatus(result.getStatus());
                            oldReport.setCompleteTime(result.getStartTime());
                        }
                    } else {
                        reports.put(result.getKey(), result);
                    }
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
        latch.await();
    }
}
