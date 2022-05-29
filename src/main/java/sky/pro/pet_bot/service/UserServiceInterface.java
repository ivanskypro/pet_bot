package sky.pro.pet_bot.service;

import sky.pro.pet_bot.model.User;

import java.util.Collection;

public interface UserServiceInterface {
    User addUser(User user, Long chatId);
    Collection<User> getUserById(Integer id);
    Collection<User> getAllUsers();
}
