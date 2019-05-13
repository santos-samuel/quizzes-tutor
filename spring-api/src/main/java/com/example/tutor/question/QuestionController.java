package com.example.tutor.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.tutor.ResourceNotFoundException;

import javax.validation.Valid;

@RestController
public class QuestionController {

    private QuestionRepository questionRepository;

    QuestionController(QuestionRepository repository) {
        this.questionRepository = repository;
    }

    @GetMapping("/questions")
    public Page<Question> getQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }

    @GetMapping("/questions/{question_id}")
    public Question getQuestion(@PathVariable Integer question_id) {
        questionRepository.findById(question_id).ifPresent(name -> System.out.println(name.toString()));
        return questionRepository.findById(question_id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + question_id));
    }


    @PostMapping("/questions")
    public Question createQuestion(@Valid @RequestBody Question question) {
        return questionRepository.save(question);
    }

    @PutMapping("/questions/{question_id}")
    public Question updateQuestion(@PathVariable Integer question_id,
                                   @Valid @RequestBody Question questionRequest) {

        return questionRepository.findById(question_id)
                .map(question -> {
                    question.setContent(questionRequest.getContent());
                    question.setImage(questionRequest.getImage());
                    return questionRepository.save(question);
                }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + question_id));
    }


    @DeleteMapping("/questions/{question_id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Integer question_id) {
        return questionRepository.findById(question_id)
                .map(question -> {
                    questionRepository.delete(question);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + question_id));
    }
}