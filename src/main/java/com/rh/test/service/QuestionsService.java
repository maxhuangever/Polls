package com.rh.test.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestParam;

import com.rh.test.common.PollsConstant;
import com.rh.test.common.PollsUtils;
import com.rh.test.dao.ChoiceRepository;
import com.rh.test.dao.QuestionRepository;
import com.rh.test.model.Choice;
import com.rh.test.model.Question;

@Service
public class QuestionsService {
	private Log logger = LogFactory.getLog(QuestionsService.class);

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private ChoiceRepository choiceRepository;

	public Question createNewQuestion(Question question) {
		// set up relationship and save
		question.setPublishAt(new Date());
		for (Choice choice : question.getChoices()) {
			choice.setQuestion(question);
		}
		Question newQuestion = questionRepository.save(question);

		PollsUtils.enhanceUrl(newQuestion);

		return newQuestion;
	}

	public Question getQuestionById(int questionId) {
		Question question = questionRepository.findOne(questionId);
		if(question!=null) {
			PollsUtils.enhanceUrl(question);
		}
		return question;
	}

	public List<Question> listAllQuestions(@RequestParam int currentPage) {
		Assert.isTrue(currentPage>=1, "currentPage should be greater than or equal to 1");
		currentPage = currentPage - 1;
		
		// order
		Order idOrder = new Order(Direction.DESC, PollsConstant.SortField);
		Sort sort = new Sort(idOrder);
		PageRequest pageRequest = new PageRequest(currentPage, PollsConstant.PageSize, sort);

		// get paged data
		Page<Question> page = questionRepository.findAll(pageRequest);
		logger.info(String.format("TotalCount=[%d],TotalPages=[%d],currentPage=[%d],Reords of CurPage=[%d]",
				page.getTotalElements(), page.getTotalPages(), page.getNumber(), page.getSize()));
		List<Question> questions = page.getContent();
		for (Question question : questions) {
			PollsUtils.enhanceUrl(question);
		}

		return questions;
	}

	public Choice vote(@RequestParam int questionId, @RequestParam int choiceId) {
		// direct get choice by id
		Choice choice = choiceRepository.findOne(choiceId);
		Assert.notNull(choice, "find no record in table by id[" + choiceId + "]");
		Assert.notNull(choice.getQuestion(), "choice has no related question");
		Assert.isTrue(questionId == choice.getQuestion().getId(), "parameter questionId[" + questionId
				+ "] not equals to [" + choice.getQuestion().getId() + "] in table");

		// vote
		int newVotes = choice.getVotes() + 1;
		choice.setVotes(newVotes);
		Choice newChoice =  choiceRepository.save(choice);
		PollsUtils.enhanceUrl4Choice(newChoice);
		return newChoice;
	}
}
