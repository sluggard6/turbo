package xyz.sluggard.transmatch;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.scheduling.annotation.EnableAsync;

import com.viewfin.commons.system.Application;

import xyz.sluggard.transmatch.engine.MatchEngine;
import xyz.sluggard.transmatch.service.EventService;

@SpringBootApplication
@EnableAsync
public class MatchApplication {
	
	public static void main(String... args) {
		Application.setCurrentApplication(Application.ORDER);
		ConfigurableApplicationContext cac = SpringApplication.run(MatchApplication.class, args);
		cac.addApplicationListener(new ApplicationListener<ApplicationContextEvent>() {

			@Override
			public void onApplicationEvent(ApplicationContextEvent event) {
				
			}
		});
	}
	
	@Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
    		MatchEngine.ENGIN.setEventService(ctx.getBean(EventService.class));
        };
    }

}
