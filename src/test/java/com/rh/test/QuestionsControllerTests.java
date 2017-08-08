package com.rh.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rh.test.common.PollsConstant;
import com.rh.test.model.Choice;

/**
 * These test methods are supposed to be executed by order, since test urls
 * that later methods use are retrieved from earlier method.
 * @author 23885_000
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QuestionsControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	private static Map<String, Object> testQuestionData;
	private static String questionUrl;// get from response data, will be used in test40_getQuestionDetail
	private static String choiceUrl;// get from response data, will be used in test50_voteTest
	private static String nonExistParam="10000";// no data should be found with this param
	
	@BeforeClass
	public static void beforeClass() {     
		testQuestionData = initialTestData();    
    } 

	@Test
	public void test10_retrieveEntryPoint() throws Exception {
		System.out.println("\n===================test10_retrieveEntryPoint");
		this.mockMvc.perform(get("/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$." + PollsConstant.EntryKey).value(PollsConstant.EntryUrl));
	}

	@Test
	public void test20_createNewQuestion() throws Exception {
		System.out.println("\n===================test20_createNewQuestion");
		
		// wrong param, page are supposed >= 1
		this.mockMvc.perform(post("/questions").param("page", "0")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(testQuestionData)))
				.andDo(print())
				.andExpect(status().isBadRequest());
		
		// correct request, and retrieve urls for later use.
		ResultActions ra = this.mockMvc.perform(post("/questions").param("page", "1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(testQuestionData)));		
		ra.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].question")
				.value(testQuestionData.get("question").toString()));
		
		// populate urls for later methods: test40_... and test50_...
		this.getTestUrl(ra);
	}
	
	@Test
	public void test30_listAllQuestions() throws Exception {
		System.out.println("\n===================test30_listAllQuestions");
		// should return data since this method is executed after method test20_...
		this.mockMvc.perform(get("/questions").param("page", "1")
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
		
		// should have no data found
		this.mockMvc.perform(get("/questions").param("page", nonExistParam)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNoContent());
	}
	
	@Test
	public void test40_getQuestionDetail() throws Exception {
		System.out.println("\n===================test40_getQuestionDetail");
		// normal url
		this.mockMvc.perform(get(questionUrl)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
		
		// wrong param
		this.mockMvc.perform(get("/questions/"+nonExistParam)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNoContent());
	}
	
	@Test
	public void test50_voteTest() throws Exception {
		System.out.println("\n===================test50_voteTest");
		
		// first vote
		String stringResult = this.mockMvc.perform(post(choiceUrl)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andReturn().getResponse().getContentAsString();
		Choice choice = objectMapper.readValue(stringResult, Choice.class);
		int voteBefore = choice.getVotes();
		
		// vote again
		String afterStringResult = this.mockMvc.perform(post(choiceUrl)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andReturn().getResponse().getContentAsString();
		Choice afterChoice = objectMapper.readValue(afterStringResult, Choice.class);
		int voteAfter = afterChoice.getVotes();

		// difference between 2 votes should be 1
		Assert.isTrue((voteAfter-voteBefore)==1, "vote failed");
	}

	private static Map<String, Object> initialTestData() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("question", "Favourite programming language?");
		List<String> list = new ArrayList<String>();
		list.add("Swift");
		list.add("Python");
		list.add("Objective-C");
		list.add("Ruby");
		map.put("choices", list);

		return map;
	}
	
	/**
	 * get questionUrl and choiceUrl which will be used later.
	 * @param ra
	 * @throws Exception
	 */
	private void getTestUrl(ResultActions ra) throws Exception {
		String stringResult = ra.andReturn().getResponse().getContentAsString();
		@SuppressWarnings("unchecked")
		List<LinkedHashMap<String, Object>> list = objectMapper.readValue(stringResult, List.class);
		
		questionUrl = list.get(0).get("url").toString();
		
		String choiceStr = list.get(0).get("choices").toString();
		System.out.println(choiceStr);
		int index = choiceStr.indexOf("url=")+4;
		String subStr = choiceStr.substring(index);
		System.out.println(subStr);

		index = subStr.indexOf(",");
		choiceUrl = subStr.substring(0,index);
	}

}
