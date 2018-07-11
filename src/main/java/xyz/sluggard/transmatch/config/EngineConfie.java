package xyz.sluggard.transmatch.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.viewfin.commons.protocol.HttpResult;

@Configuration
public class EngineConfie {
	
	@ExceptionHandler
	public HttpResult<?> exp(HttpServletRequest request, Exception ex) {  
		return HttpResult.newResult(500, ex.getMessage());
	}

}
