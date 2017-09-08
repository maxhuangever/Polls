package com.rh.test.controller;

import com.rh.test.common.PollsConstant;
import com.rh.test.model.Choice;
import com.rh.test.model.Question;
import com.rh.test.service.QuestionsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class QuestionsController {
    private Log logger = LogFactory.getLog(QuestionsController.class);

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

    @PostMapping("/services/upload")
    @ResponseBody
    public Object uploadFile(
            @RequestParam("userPhoto") MultipartFile uploadFile,
            @RequestParam("userName") String userName) {

        logger.info("Single file upload!");

        if (uploadFile.isEmpty()) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }

        File tempFile = null;
        FileOutputStream fos = null;
        try {
            tempFile = File.createTempFile("photostore-", ".tmp", null);
            fos = new FileOutputStream(tempFile);
            fos.write(uploadFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return PollsConstant.Response_BadRequest;
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
                return PollsConstant.Response_ServerError;
            }
        }

        Map<String, String> retMap = new HashMap<>();
        retMap.put("fileName", tempFile.getName());
        retMap.put("filePath", tempFile.getAbsolutePath());
        return retMap;

    }
}
