package sky.pro.pet_bot.service;

import sky.pro.pet_bot.model.Pet;

import java.util.Collection;

public interface PetServiceInterface {

    Pet addUserPet(Pet pet);

    Collection<Pet> getPetById(Integer id);

    Collection<Pet> getAllPets();

}
