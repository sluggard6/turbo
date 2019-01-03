//package xyz.sluggard.transmatch.config;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//import xyz.sluggard.transmatch.core.Engine;
//import xyz.sluggard.transmatch.engine.ExecutorEngine;
//import xyz.sluggard.transmatch.service.impl.EventServiceImpl;
//import xyz.sluggard.transmatch.service.impl.NoneInitServiceImpl;
//import xyz.sluggard.transmatch.vo.HttpResult;
//
//@Configuration
//public class EngineConfig {
//	
//	@Value("${currencyPair}")
//	private String currencyPair;
//	
//	@ExceptionHandler
//	public HttpResult<?> exp(HttpServletRequest request, Exception ex) {  
//		return HttpResult.newResult(500, ex.getMessage());
//	}
//	
//	@Bean
//	public Engine execuEngine() {
//		ExecutorEngine engine = new ExecutorEngine(currencyPair);
//		engine.setEventService(new EventServiceImpl());
//		engine.setInitService(new NoneInitServiceImpl());
//		return engine;
//	}
//
//}
