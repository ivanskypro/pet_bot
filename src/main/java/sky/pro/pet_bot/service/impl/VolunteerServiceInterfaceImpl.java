package sky.pro.pet_bot.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sky.pro.pet_bot.dao.VolunteerRepository;
import sky.pro.pet_bot.model.Volunteer;
import sky.pro.pet_bot.service.VolunteerServiceInterface;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class VolunteerServiceInterfaceImpl implements VolunteerServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceInterfaceImpl.class);

    private final VolunteerRepository volunteerRepository;

    public VolunteerServiceInterfaceImpl(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    @Override
    public Volunteer addUserPet(Volunteer volunteer) {
        logger.info("Volunteer successfully saved");
        return volunteerRepository.save(volunteer);
    }

    @Override
    public Collection<Volunteer> getVolunteerById(Integer id) {
        logger.info("Method getAllVolunteerById is start");
        return getAllVolunteers().stream()
                .filter(volunteer -> volunteer.isById(id))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Volunteer> getAllVolunteers() {
        logger.info("Method getAllVolunteers is start");
        return volunteerRepository.findAll();
    }
}
