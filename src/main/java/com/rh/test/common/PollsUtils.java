package com.rh.test.common;

import org.springframework.util.Assert;

import com.rh.test.model.Choice;
import com.rh.test.model.Question;

public class PollsUtils {
	/**
	 * generate url for question
	 * @param question
	 * @return
	 */
	public static String getQuestionUrl(Question question) {
		Assert.notNull(question, "question is null");

		return PollsConstant.QustUrlPrefix + question.getId();
	}

	/**
	 * generate url for choice
	 * @param choice
	 * @return
	 */
	public static String getChoiceUrl(Choice choice) {
		Assert.notNull(choice, "choice is null");
		Assert.notNull(choice.getQuestion(), "choice's question is null");

		return PollsConstant.QustUrlPrefix + choice.getQuestion().getId() + PollsConstant.ChcUrlPrefix + choice.getId();
	}

	/**
	 * populate url for question and choice
	 * @param question
	 */
	public static void enhanceUrl(Question question) {
		if(question==null) {
			return;
		}
		question.setUrl(PollsUtils.getQuestionUrl(question));
		for (Choice choice : question.getChoices()) {
			enhanceUrl4Choice(choice);
		}
	}
	
	/**
	 * populate url for choice
	 * @param choice
	 */
	public static void enhanceUrl4Choice(Choice choice) {
		if(choice==null) {
			return;
		}
		choice.setUrl(PollsUtils.getChoiceUrl(choice));
	}
}
