package com.rh.test.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rh.test.model.Choice;

public interface ChoiceRepository extends JpaRepository<Choice, Integer>  {

}
