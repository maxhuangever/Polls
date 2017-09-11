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

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class QuestionsController {
    private static Log logger = LogFactory.getLog(QuestionsController.class);

    @Autowired
    private QuestionsService questionsService;

    @Autowired
    HttpServletRequest request;

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

        File file = null;
        FileOutputStream fos = null;
        String fileName = uploadFile.getOriginalFilename();
        String fileDir = request.getSession().getServletContext().getRealPath("")+File.separator+"upload"+File.separator;
        createDirIfNotExist(fileDir);
        try {
            file = createFile(fileDir, fileName);
            fos = new FileOutputStream(file);
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
        retMap.put("fileName", file.getName());
        retMap.put("filePath", file.getAbsolutePath());
        return retMap;
    }

    private static File createFile(String filePath, String fileName){
        String filenameTemp = filePath+fileName;
        File file = new File(filenameTemp);
        try {
            if(!file.exists()){
                file.createNewFile();
                logger.info("success create file,the file is "+filenameTemp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    private static void createDirIfNotExist(String dirName) {
        File file = new File(dirName);
        if (file.exists()) {
            if (file.isDirectory()) {
                logger.info("dir exists");
            } else {
                logger.info("the same name file exists, can not create dir");
            }
        } else {
            logger.info("dir not exists, create it ...");
            file.mkdir();
        }
    }
}
