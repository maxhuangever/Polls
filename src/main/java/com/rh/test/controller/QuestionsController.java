package com.rh.test.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rh.test.common.PollsConstant;
import com.rh.test.model.Choice;
import com.rh.test.model.Question;
import com.rh.test.service.QuestionsService;

@RestController
public class QuestionsController {
	@Autowired
	private QuestionsService questionsService;

	@RequestMapping("/")
	public Map<String, String> retrieveEntryPoint() {
		Map<String, String> entry = new HashMap<String, String>();
		entry.put(PollsConstant.EntryKey, PollsConstant.EntryUrl);
		return entry;
	}

	@PostMapping(path = "/questions")
	public Object createNewQuestion(@RequestParam(value = "page") int page, @RequestBody Question question) {
		questionsService.createNewQuestion(question);
		return this.listAllQuestions(page);
	}
	
	@GetMapping(path = "/questions/{question_id}")
	public Object getQuestionDetail(@PathVariable("question_id") int questionId) {
		Question question = questionsService.getQuestionById(questionId);
		if (question == null) {
			return PollsConstant.Response_NoContent;
		}
		return question;
	}

	@GetMapping(path = "/questions")
	public Object listAllQuestions(@RequestParam(value = "page") int page) {
		List<Question> result = questionsService.listAllQuestions(page);
		if (result.isEmpty()) {
			return PollsConstant.Response_NoContent;
		}

		return result;
	}

	@PostMapping(path = "/questions/{question_id}/choices/{choice_id}")
	public Choice vote(@PathVariable("question_id") int questionId, @PathVariable("choice_id") int choiceId) {
		return questionsService.vote(questionId, choiceId);
	}
}
