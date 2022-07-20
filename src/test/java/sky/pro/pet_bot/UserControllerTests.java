package sky.pro.pet_bot;

import com.pengrad.telegrambot.TelegramBot;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sky.pro.pet_bot.controller.UserController;
import sky.pro.pet_bot.dao.UserRepository;
import sky.pro.pet_bot.model.User;
import sky.pro.pet_bot.service.impl.*;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PictureServiceInterfaceImpl pictureServiceInterface;

    @MockBean
    private AnswerServiceInterfaceImpl answerServiceInterface;

    @MockBean
    private PetServiceInterfaceImpl petServiceInterface;

    @MockBean
    private TelegramBot telegramBot;

    @MockBean
    private VolunteerServiceInterfaceImpl volunteerServiceInterface;

    @SpyBean
    private UserServiceInterfaceImpl userServiceInterface;

    @InjectMocks
    private UserController userController;

    @Test
    public void willUserBeSaved () throws Exception{
        Long id = 1L;
        String phoneNumber = "897654535454";
        String name = "Геннадий";
        Long chatId = 16739485746L;
        boolean isOwner = true;
        int probationPeriod = 30;

        JSONObject userObject = new JSONObject();
        userObject.put("phoneNumber", phoneNumber);
        userObject.put("name", name);
        userObject.put("chatId", chatId);
        userObject.put("isOwner", isOwner);
        userObject.put("probationPeriod", probationPeriod);

        User user = new User();
        user.setId(id);
        user.setPhoneNumber(phoneNumber);
        user.setChatId(chatId);
        user.isOwner();
        user.setProbationPeriod(probationPeriod);

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .content(userObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }
}
