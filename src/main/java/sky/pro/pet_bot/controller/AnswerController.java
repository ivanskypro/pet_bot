package sky.pro.pet_bot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sky.pro.pet_bot.model.Answer;
import sky.pro.pet_bot.service.impl.AnswerServiceInterfaceImpl;

import java.util.Collection;

@RestController
@RequestMapping("/answers")
public class AnswerController {

    private final AnswerServiceInterfaceImpl answerServiceInterfaceImpl;


    public AnswerController(AnswerServiceInterfaceImpl answerServiceInterface) {
        this.answerServiceInterfaceImpl = answerServiceInterface;
    }

    @GetMapping("/{id}")
    public Collection <Answer> getAnswerById (@PathVariable Long id){
        return answerServiceInterfaceImpl.getAnswersById(id);
    }

    @PostMapping
    public ResponseEntity <Answer> addAnswer (@RequestBody Answer answer){
        return ResponseEntity.ok(answerServiceInterfaceImpl.addAnswer(answer));
    }

    @PutMapping
    public ResponseEntity<Answer> updateAnswer (@RequestBody Answer answer){
        return ResponseEntity.ok(answerServiceInterfaceImpl.update(answer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Answer> deleteAnswer (@PathVariable Long id){
         answerServiceInterfaceImpl.deleteAnswer(id);
         return ResponseEntity.ok().build();
    }

    @GetMapping("/getAllAnswers")
    public Collection<Answer> getAllAnswers (){
        return answerServiceInterfaceImpl.getAllAnswers();
    }
}
