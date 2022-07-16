package sky.pro.pet_bot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.pet_bot.model.User;
import sky.pro.pet_bot.model.Volunteer;

/**
 * репозиторий для хранения сведений о пользователе
 */
public interface UserRepository extends JpaRepository<User,Long> {
    User findByChatId (Long chatId);
}
