package xyz.sluggard.transmatch;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MatchApplication {
	
	public static void main(String... args) {
		ConfigurableApplicationContext cac = SpringApplication.run(MatchApplication.class, args);
		cac.addApplicationListener(new ApplicationListener<ApplicationContextEvent>() {

			@Override
			public void onApplicationEvent(ApplicationContextEvent event) {
			}
		});
	}
	
	@Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		ctx.publishEvent(new ApplicationEvent(ctx) {
		});
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
//            for (String beanName : beanNames) {
//                System.out.println(beanName);
//            }

        };
    }

}
