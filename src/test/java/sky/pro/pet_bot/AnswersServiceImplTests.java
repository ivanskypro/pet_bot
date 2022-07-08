package sky.pro.pet_bot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sky.pro.pet_bot.dao.AnswerRepository;
import sky.pro.pet_bot.model.Answer;
import sky.pro.pet_bot.service.impl.AnswerServiceInterfaceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnswersServiceImplTests {

    private Answer answer = new Answer(1L, "Направо от метро");
    private Answer wrongAnswer = new Answer(null, null);
    private List<Answer> collectionOfAnswers;

    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private AnswerServiceInterfaceImpl out;

    @Test
    public void willAddAnswer(){
        when(answerRepository.save(answer)).thenReturn(answer);
        assertEquals(answer, out.addAnswer(answer));
    }
    @Test
    public void willFindAnswer(){
        when(answerRepository.findById(answer.getId())).thenReturn(Optional.of(answer));
        assertEquals(answer, out.getAnswerById(answer.getId()));
    }

    @Test
    public void willThrowNullPointerException (){
        when(answerRepository.findById(wrongAnswer.getId())).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, ()-> out.getAnswerById(wrongAnswer.getId()) );
    }

    @Test
    public void willShowCollection(){
        when(answerRepository.findAll()).thenReturn(collectionOfAnswers);
        assertEquals(collectionOfAnswers, out.getAllAnswers());
    }

}
