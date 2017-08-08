package com.rh.test.common;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class PollsExceptionHandler {
	private Log logger = LogFactory.getLog(PollsExceptionHandler.class);

	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Object ExceptionHandler(HttpServletRequest req, Exception e) throws Exception {
		logger.error("PollsExceptionHandler---Host {" + req.getRemoteHost() + "} invokes url {" + req.getRequestURL()
				+ "} ERROR: {" + e.getMessage() + "}");
		e.printStackTrace();	
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

}