# Polls App
  
# start it
  mvn clean package  
  java -jar polls-0.1.0.jar
  
# Features  
  **-Maven build**  
  **-support ORM mapping to a H2 in memory database**  
  **-support CORS**  
  **-Spring Boot controller test case**  

# sample urls:  
## retrieveEntryPoint
url:  
http://localhost:8080/  
method:  
get  

## create new question
url:  
http://localhost:8080/questions?page=1  
method:  
post  
request body:  
{  
  "question": "Favourite programming language?",  
  "choices": [  
    "Swift",  
    "Python",  
    "Objective-C",  
    "Ruby"  
  ]  
}  

## getQuestionDetail
url:  
http://localhost:8080/questions/5  
method:  
get  

## listAllQuestions
url:  
http://localhost:8080/questions?page=1  
method:  
get  

## vote
url:  
http://localhost:8080/questions/1/choices/1  
method:  
post  
