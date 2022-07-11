package sky.pro.pet_bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sky.pro.pet_bot.controller.AnswerController;
import sky.pro.pet_bot.dao.AnswerRepository;
import sky.pro.pet_bot.model.Answer;
import sky.pro.pet_bot.service.impl.AnswerServiceInterfaceImpl;
import sky.pro.pet_bot.service.impl.PetServiceInterfaceImpl;
import sky.pro.pet_bot.service.impl.PictureServiceInterfaceImpl;
import sky.pro.pet_bot.service.impl.VolunteerServiceInterfaceImpl;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

   @WebMvcTest
   public class AnswerControllerTest {

   @Autowired
   private MockMvc mockMvc;

   @MockBean
   private AnswerRepository answerRepository;

   @MockBean
   private PictureServiceInterfaceImpl pictureServiceInterface;

   @MockBean
   private PetServiceInterfaceImpl petServiceInterface;

   @MockBean
   private VolunteerServiceInterfaceImpl volunteerServiceInterface;

   @SpyBean
   private AnswerServiceInterfaceImpl answerServiceInterface;

   @InjectMocks
    private AnswerController answerController;

   @Test
    public void willAnswerBeSaved()throws Exception{
       Long id = 1L;
       String textMessage = "От метро направо";

       JSONObject answerObject = new JSONObject();
       answerObject.put("textMessage", textMessage);

       Answer answer = new Answer();
       answer.setId(id);
       answer.setTextMessage(textMessage);

       when(answerRepository.save(any(Answer.class))).thenReturn(answer);
       when(answerRepository.findById(any(Long.class))).thenReturn(Optional.of(answer));

       mockMvc.perform(MockMvcRequestBuilders.post("/answers")
               .content(answerObject.toString())
               .contentType(MediaType.APPLICATION_JSON)
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(id))
               .andExpect(jsonPath("$.textMessage").value(textMessage));
   }


}
