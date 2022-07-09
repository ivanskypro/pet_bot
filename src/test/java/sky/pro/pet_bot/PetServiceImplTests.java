package sky.pro.pet_bot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sky.pro.pet_bot.dao.PetRepository;
import sky.pro.pet_bot.model.Pet;
import sky.pro.pet_bot.service.impl.PetServiceInterfaceImpl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static sky.pro.pet_bot.model.Pet.PetType.CAT;
import static sky.pro.pet_bot.model.Pet.PetType.DOG;

@ExtendWith(MockitoExtension.class)
public class PetServiceImplTests {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetServiceInterfaceImpl out;

    private Pet wrongPet = new Pet (null, null, null, CAT);
    private Pet pet = new Pet (1L, "Котик", 1, CAT);
    private Pet pet2 = new Pet (1L, "Песик", 1, DOG);
    private List<Pet> petsCollection;

    @Test
    public void willFindPet(){
        when(petRepository.findById(pet.getId())).thenReturn(Optional.of(pet));
        assertEquals(pet, out.getPetById(pet.getId()));
    }

    @Test
    public void willThrowNullPointerException(){
        when(petRepository.findById(wrongPet.getId())).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, ()-> out.getPetById(wrongPet.getId()));
    }

    @Test
    public void willSavePet (){
        when(petRepository.save(pet)).thenReturn(pet);
        assertEquals(pet, out.addUserPet(pet));
    }

    @Test
    public void willShowCollection(){
    when(petRepository.findAll()).thenReturn(petsCollection);
    assertEquals(petsCollection, out.getAllPets());

    }

}
