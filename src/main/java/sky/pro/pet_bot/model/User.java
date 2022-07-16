package sky.pro.pet_bot.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

/**Класс, описывающий пользователя, взаимодействующего с ботом
 *
 */

@Entity
@Table (name= "users")
public class User {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNumber;
    private String name;
    private Long chatId;
    private boolean isOwner;

    public User() {
    }

    public User(Long id, String phoneNumber, String name, Long chatId, boolean isOwner) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.chatId = chatId;
        this.isOwner = isOwner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public boolean isOwner() {
        return isOwner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isOwner == user.isOwner && Objects.equals(id, user.id) && Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(name, user.name) && Objects.equals(chatId, user.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phoneNumber, name, chatId, isOwner);
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", phoneNumber=" + phoneNumber +
                ", name='" + name + '\'' +
                ", chatId=" + chatId +
                ", isOwner=" + isOwner +
                '}';
    }
}

