package sky.pro.pet_bot.model;

import javax.persistence.*;
import java.util.Objects;
/**Класс, описывающий волонтера, взаимодействующего с ботом
 *
 */
@Entity
@Table(name = "volunteers")
public class Volunteer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phoneNumber;
    private Long chatId;

    public Volunteer(Long id, String name, String phoneNumber, Long chatId) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.chatId = chatId;
    }

    public Volunteer() {
    }

    public boolean isById(Long id) {
        if (this.id != id) {
            return false;
        }
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Volunteer volunteer = (Volunteer) o;
        return Objects.equals(id, volunteer.id) && Objects.equals(name, volunteer.name) && Objects.equals(phoneNumber, volunteer.phoneNumber) && Objects.equals(chatId, volunteer.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phoneNumber, chatId);
    }

    @Override
    public String toString() {
        return "Volunteer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", chatId=" + chatId +
                '}';
    }
}
