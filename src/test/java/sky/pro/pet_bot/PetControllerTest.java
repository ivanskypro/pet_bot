package sky.pro.pet_bot;

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
import sky.pro.pet_bot.controller.PetController;
import sky.pro.pet_bot.dao.PetRepository;
import sky.pro.pet_bot.model.Pet;
import sky.pro.pet_bot.service.impl.AnswerServiceInterfaceImpl;
import sky.pro.pet_bot.service.impl.PetServiceInterfaceImpl;
import sky.pro.pet_bot.service.impl.PictureServiceInterfaceImpl;
import sky.pro.pet_bot.service.impl.VolunteerServiceInterfaceImpl;

import java.util.Optional;

import static sky.pro.pet_bot.model.Pet.PetType.CAT;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sky.pro.pet_bot.model.Pet.PetType.DOG;


    @WebMvcTest
    public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetRepository petRepository;

    @MockBean
    private PictureServiceInterfaceImpl pictureServiceInterface;

    @MockBean
    private AnswerServiceInterfaceImpl answerServiceInterface;

    @MockBean
    private VolunteerServiceInterfaceImpl volunteerServiceInterface;

    @SpyBean
    private PetServiceInterfaceImpl petServiceInterface;

    @InjectMocks
    private PetController petController;

    @Test
    public void willPetBeSaved () throws Exception{

       Long id = 1L;
       String name = "Песик";
       Integer age = 1;

       JSONObject petObject = new JSONObject();
       petObject.put("name", name);
       petObject.put("age", age);

       Pet pet = new Pet();
       pet.setId(id);
       pet.setName(name);
       pet.setAge(age);
       pet.setType(DOG);

        when(petRepository.save(any(Pet.class))).thenReturn(pet);
        when(petRepository.findById(any(Long.class))).thenReturn(Optional.of(pet));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/pets")
                .content(petObject.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));




    }



}
