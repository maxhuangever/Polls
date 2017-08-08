package com.rh.test.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.rh.test.model.Question;

public interface QuestionRepository extends PagingAndSortingRepository<Question, Integer> {

}
