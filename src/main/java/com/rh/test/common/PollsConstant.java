package com.rh.test.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface PollsConstant {
	public final static String EntryKey = "questions_url";
	public final static String EntryUrl = "/questions";
	public final static String QustUrlPrefix = "/questions/";
	public final static String ChcUrlPrefix = "/choices/";
	
	public final static int PageSize = 10;
	public final static String SortField = "id";//default sort field for Question

	public final static ResponseEntity<String> Response_NoContent = new ResponseEntity<String>("no entry found", HttpStatus.NO_CONTENT);
	public final static ResponseEntity<String> Response_ServerError = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	public final static ResponseEntity<String> Response_BadRequest = new  ResponseEntity<>(HttpStatus.BAD_REQUEST);
}
