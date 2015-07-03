package com.miwashi;

import com.miwashi.model.User;
import com.miwashi.repositories.UserRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@ConfigurationProperties
public class TestStatusApplication extends WebMvcConfigurerAdapter{

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

    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/").setViewName("index");
    }

    @Value("${configuration.projectName}")
    void setProjectName(String projectName){
        System.out.println("Setting name to " + projectName);
    }

    @Autowired
    void setEnvrironment(Environment env){
        System.out.println("Setting environment: " + env.getProperty("configuration.projectName"));
    }


    public static void main(String[] args) {
        SpringApplication.run(TestStatusApplication.class, args);
    }
}
