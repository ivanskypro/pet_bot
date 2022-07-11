package sky.pro.pet_bot;

import com.google.gson.JsonObject;
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
import sky.pro.pet_bot.controller.VolunteerController;
import sky.pro.pet_bot.dao.VolunteerRepository;
import sky.pro.pet_bot.model.Volunteer;
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
public class VolunteerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VolunteerRepository volunteerRepository;

    @MockBean
    private PictureServiceInterfaceImpl pictureServiceInterface;

    @MockBean
    private AnswerServiceInterfaceImpl answerServiceInterface;

    @MockBean
    private PetServiceInterfaceImpl petServiceInterface;

    @SpyBean
    private VolunteerServiceInterfaceImpl volunteerServiceInterface;

    @InjectMocks
    private VolunteerController volunteerController;

    @Test
    public void willVolunteerBeSaved () throws Exception{
        Long id = 1L;
        String name = "Nikolay";
        String phoneNumber = "87456473944";

        JSONObject volunteerObject = new JSONObject();
        volunteerObject.put("name", name);
        volunteerObject.put("phoneNumber", phoneNumber);

        Volunteer volunteer = new Volunteer();
        volunteer.setId(id);
        volunteer.setName(name);
        volunteer.setPhoneNumber(phoneNumber);

        when(volunteerRepository.save(any(Volunteer.class))).thenReturn(volunteer);
        when(volunteerRepository.findById(any(Long.class))).thenReturn(Optional.of(volunteer));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/volunteers")
                        .content(volunteerObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(id));
    }

}
