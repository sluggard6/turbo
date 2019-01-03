//package xyz.sluggard.transmatch;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.context.event.ApplicationContextEvent;
//import org.springframework.scheduling.annotation.EnableAsync;
//
//@SpringBootApplication
//@EnableAsync
//public class MatchApplication {
//	
//	public static void main(String... args) {
//		System.setProperty("spring.devtools.restart.enabled", "true");
//		ConfigurableApplicationContext cac = SpringApplication.run(MatchApplication.class, args);
//		cac.addApplicationListener(new ApplicationListener<ApplicationContextEvent>() {
//
//			@Override
//			public void onApplicationEvent(ApplicationContextEvent event) {
//				
//			}
//		});
//	}
//
//}
