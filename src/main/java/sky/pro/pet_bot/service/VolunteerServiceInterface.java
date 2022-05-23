package sky.pro.pet_bot.service;


import sky.pro.pet_bot.model.Volunteer;

import java.util.Collection;

public interface VolunteerServiceInterface {

    Volunteer addUserPet(Volunteer volunteer);

    Collection<Volunteer> getVolunteerById(Integer id);

    Collection<Volunteer> getAllVolunteers();
}
