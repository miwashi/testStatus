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

    private Log log = LogFactory.getLog(TestStatusApplication.class);

    private static long numberOfTests = 0;
    private static long numberOfCompletedTests = 0;
    private static long numberOfFailedTests = 0;

    private static long[] numberOfTestsPerHour = new long[25];
    private static long[] numberOfCompletedTestsPerHour = new long[25];
    private static long[] numberOfFailedTestsPerHour = new long[25];

    private static long[] numberOfTestsPerDay = new long[32];
    private static long[] numberOfCompletedTestsPerDay = new long[32];
    private static long[] numberOfFailedTestsPerDay = new long[32];

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

    public static long getNumberOfTests() {
        return numberOfTests;
    }

    public static long getNumberOfCompletedTests() {
        return numberOfCompletedTests;
    }

    public static long getNumberOfFailedTests() {
        return numberOfFailedTests;
    }

    public static long getNumberOfTestsToday() {
        DateTime now = DateTime.now();
        int dayOfMonth = now.getDayOfMonth();
        return numberOfTestsPerDay[dayOfMonth];
    }

    public static long getNumberOfCompletedTestsToday() {
        DateTime now = DateTime.now();
        int dayOfMonth = now.getDayOfMonth();
        return numberOfCompletedTestsPerDay[dayOfMonth];
    }

    public static long getNumberOfFailedTestsToday() {
        DateTime now = DateTime.now();
        int dayOfMonth = now.getDayOfMonth();
        return numberOfFailedTestsPerDay[dayOfMonth];
    }

    public static long getNumberOfTestsLastHour() {
        DateTime now = DateTime.now();
        int hourOfDay = now.getHourOfDay();
        return numberOfTestsPerHour[hourOfDay];
    }

    public static long getNumberOfCompletedTestsLastHour() {
        DateTime now = DateTime.now();
        int hourOfDay = now.getHourOfDay();
        return numberOfCompletedTestsPerHour[hourOfDay];
    }

    public static long getNumberOfFailedTestsLastHour() {
        DateTime now = DateTime.now();
        int hourOfDay = now.getHourOfDay();
        return numberOfFailedTestsPerHour[hourOfDay];
    }

    public static long getNumberOfTestsLast24Hours() {
        long result = 0;
        for(int i =1; i < 24; i++){
            result+= numberOfTestsPerHour[i];
        }
        return result;
    }

    public static long getNumberOfCompletedTestsLast24Hours() {
        long result = 0;
        for(int i =0; i < 24; i++){
            result+= numberOfCompletedTestsPerHour[i];
        }
        return result;
    }

    public static long getNumberOfFailedTestsLast24Hours() {
        long result = 0;
        for(int i =0; i < 24; i++){
            result+= numberOfFailedTestsPerHour[i];
        }
        return result;
    }

    public static long getNumberOfTestsLastWeek() {
        long result = 0;
        DateTime now = DateTime.now();
        int dayOfMonth = now.getDayOfMonth();
        result+= numberOfTestsPerDay[dayOfMonth];
        now = now.minusDays(1);
        dayOfMonth = now.getDayOfMonth();
        result+= numberOfTestsPerDay[dayOfMonth];
        dayOfMonth = now.getDayOfMonth();
        result+= numberOfTestsPerDay[dayOfMonth];
        dayOfMonth = now.getDayOfMonth();
        result+= numberOfTestsPerDay[dayOfMonth];
        dayOfMonth = now.getDayOfMonth();
        result+= numberOfTestsPerDay[dayOfMonth];
        dayOfMonth = now.getDayOfMonth();
        result+= numberOfTestsPerDay[dayOfMonth];
        return result;
    }

    public static long getNumberOfCompletedTestsLastWeek() {
        long result = 0;
        DateTime now = DateTime.now();
        int dayOfMonth = now.getDayOfMonth();
        result+= numberOfCompletedTestsPerDay[dayOfMonth];
        now = now.minusDays(1);
        dayOfMonth = now.getDayOfMonth();
        result+= numberOfCompletedTestsPerDay[dayOfMonth];
        dayOfMonth = now.getDayOfMonth();
        result+= numberOfCompletedTestsPerDay[dayOfMonth];
        dayOfMonth = now.getDayOfMonth();
        result+= numberOfCompletedTestsPerDay[dayOfMonth];
        dayOfMonth = now.getDayOfMonth();
        result+= numberOfCompletedTestsPerDay[dayOfMonth];
        dayOfMonth = now.getDayOfMonth();
        result+= numberOfCompletedTestsPerDay[dayOfMonth];
        return result;
    }

    public static long getNumberOfFailedTestsLastWeek() {
        long result = 0;
        DateTime now = DateTime.now();
        int dayOfMonth = now.getDayOfMonth();
        result+= numberOfFailedTestsPerDay[dayOfMonth];
        now = now.minusDays(1);
        dayOfMonth = now.getDayOfMonth();
        result+= numberOfFailedTestsPerDay[dayOfMonth];
        dayOfMonth = now.getDayOfMonth();
        result+= numberOfFailedTestsPerDay[dayOfMonth];
        dayOfMonth = now.getDayOfMonth();
        result+= numberOfFailedTestsPerDay[dayOfMonth];
        dayOfMonth = now.getDayOfMonth();
        result+= numberOfFailedTestsPerDay[dayOfMonth];
        dayOfMonth = now.getDayOfMonth();
        result+= numberOfFailedTestsPerDay[dayOfMonth];
        return result;
    }

    public static void incNumberOfTests(ResultReport result) {
        TestStatusApplication.numberOfTests++;

        DateTime now = DateTime.now();
        int hourOfDay = now.getHourOfDay();
        numberOfTestsPerHour[hourOfDay]++;

        int dayOfMonth = now.getDayOfMonth();
        numberOfTestsPerDay[dayOfMonth]++;
    }

    private void incNumberOfCompletedTests(ResultReport result) {
        numberOfCompletedTests++;

        DateTime now = DateTime.now();
        int hourOfDay = now.getHourOfDay();
        numberOfCompletedTestsPerHour[hourOfDay]++;


        int dayOfMonth = now.getDayOfMonth();
        numberOfCompletedTestsPerDay[dayOfMonth]++;

        if(!"success".equalsIgnoreCase(result.getStatus())) {
            numberOfFailedTests++;
            numberOfFailedTestsPerHour[hourOfDay]++;
            numberOfFailedTestsPerDay[dayOfMonth]++;
        }
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
    @Scheduled(cron = "1 0 * * * *")
    public void hourlyManagement(){
        DateTime now = DateTime.now();
        int hourOfDay = now.getHourOfDay();
        numberOfTestsPerHour[hourOfDay]=0;
        numberOfCompletedTestsPerHour[hourOfDay]=0;
        numberOfFailedTestsPerHour[hourOfDay]=0;
    }

    @Scheduled(cron = "1 0 0 * * *")
    public void daylyManagement(){
        DateTime now = DateTime.now();
        int dayOfMonth = now.getDayOfMonth();
        numberOfTestsPerDay[dayOfMonth]=0;
        numberOfCompletedTestsPerDay[dayOfMonth]=0;
        numberOfFailedTestsPerDay[dayOfMonth]=0;
    }

    @Scheduled(fixedRate = 500)
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
        Browser aBrowser = loadBrowser(resultReport.getBrowser());
        Platform aPlatform = loadPlatform(resultReport.getPlatform());

        Requirement requirement = new Requirement(resultReport.getName());
        Iterable<Requirement> requirements = requirementRepository.findByName(requirement.getName());
        if(requirements.iterator().hasNext()){
            requirement = requirements.iterator().next();
        }

        requirementRepository.save(requirement);

        Result result = new Result(resultReport.getStatus());
        result.setBrowser(aBrowser);
        result.setPlatform(aPlatform);
        result.setCompletionTime(resultReport.getCompleteTime());
        result.setStartTime(resultReport.getStartTime());
        requirement.add(result);
        requirementRepository.save(requirement);
    }

    private Browser loadBrowser(String browserName){
        if(browserName==null || browserName.isEmpty()){
            browserName = Browser.DEFAULT_BROWSER;
        }
        browserName = browserName.toLowerCase();

        Iterable<Browser> browsers = browserRepository.findByName(browserName);
        Browser aBrowser = new Browser(browserName);
        if(browsers.iterator().hasNext()){
            aBrowser = browsers.iterator().next();
        }else{
            browserRepository.save(aBrowser);
        }
        return aBrowser;
    }

    private Platform loadPlatform(String platformName){
        if(platformName==null || platformName.isEmpty()){
            platformName = Platform.DEFAULT_PLATFORM;
        }
        platformName = platformName.toLowerCase();

        Iterable<Platform> platforms = platformRepository.findByName(platformName);
        Platform aPlatform = new Platform(platformName);
        if(platforms.iterator().hasNext()){
            aPlatform = platforms.iterator().next();
        }else{
            platformRepository.save(aPlatform);
        }
        return aPlatform;
    }

    @Bean
    public DatagramServer<byte[], byte[]> datagramServer(reactor.core.Environment env) throws InterruptedException {

        final DatagramServer<byte[], byte[]> server = new DatagramServerSpec<byte[], byte[]>(NettyDatagramServer.class)
                .env(env)
                //.listen(SocketUtils.findAvailableTcpPort())
                .listen(6500)
                .codec(StandardCodecs.BYTE_ARRAY_CODEC)
                .consumeInput(bytes -> {
                    String request = new String(bytes);
                    log.info("received: " + request);

                    ResultReport result = new ResultReport(request);
                    if (result.isCompleted()) {
                        ResultReport oldReport = reports.get(result.getKey());
                        incNumberOfCompletedTests(result);
                        if (oldReport != null) {
                            oldReport.setStatus(result.getStatus());
                            oldReport.setCompleteTime(result.getStartTime());
                        }
                    } else {
                        incNumberOfTests(result);
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
