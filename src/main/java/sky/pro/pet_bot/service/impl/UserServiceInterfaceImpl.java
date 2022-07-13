package sky.pro.pet_bot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sky.pro.pet_bot.dao.UserRepository;
import sky.pro.pet_bot.model.User;
import sky.pro.pet_bot.service.UserServiceInterface;

import java.util.Collection;

@Service
public class UserServiceInterfaceImpl implements UserServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceInterfaceImpl.class);

    private final UserRepository userRepository;
    private final TelegramBot telegramBot;

    public UserServiceInterfaceImpl(UserRepository userRepository, TelegramBot telegramBot) {
        this.userRepository = userRepository;
        this.telegramBot = telegramBot;
    }

    @Override
    public User addUser(User user) {
        User storedUser = userRepository.save(user);
        logger.info("User successfully saved " + storedUser);
        return storedUser;
    }

    @Override
    public User getUserById(Long id) {
        logger.info("Method getUserById is started");
        return userRepository.findById(id).get();
    }

    @Override
    public Collection<User> getAllUsers() {
        logger.info("Method getAllUsers is start");
        return userRepository.findAll();
    }
}
