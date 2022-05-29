package sky.pro.pet_bot.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sky.pro.pet_bot.dao.PetRepository;
import sky.pro.pet_bot.model.Pet;
import sky.pro.pet_bot.service.PetServiceInterface;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class PetServiceInterfaceImpl implements PetServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceInterfaceImpl.class);

    private final PetRepository petRepository;

    public PetServiceInterfaceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }


    @Override
    public Pet addUserPet(Pet pet) {
        logger.info("Pet successfully saved");
        return petRepository.save(pet);
    }

    @Override
    public Collection<Pet> getPetById(Integer id) {
        logger.info("Method getPetById is start");
        return getAllPets().stream()
                .filter(pet -> pet.isById(id))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Pet> getAllPets() {
        logger.info("Method getAllPets is start");
        return petRepository.findAll();
    }
}
