package sky.pro.pet_bot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    LocalDateTime dateTimeOfReport;
    Integer fileSize;
    String fileId;
    String text;
    Long chatId;

    public Report() {
    }

    public Report(Long id, LocalDateTime dateTimeOfReport, Integer fileSize, String fileId, String text, Long chatId) {
        this.id = id;
        this.dateTimeOfReport = dateTimeOfReport;
        this.fileSize = fileSize;
        this.fileId = fileId;
        this.text = text;
        this.chatId = chatId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTimeOfReport() {
        return dateTimeOfReport;
    }

    public void setDateTimeOfReport(LocalDateTime dateTimeOfReport) {
        this.dateTimeOfReport = dateTimeOfReport;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
        Report report = (Report) o;
        return Objects.equals(id, report.id) && Objects.equals(dateTimeOfReport, report.dateTimeOfReport) && Objects.equals(fileSize, report.fileSize) && Objects.equals(fileId, report.fileId) && Objects.equals(text, report.text) && Objects.equals(chatId, report.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTimeOfReport, fileSize, fileId, text, chatId);
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", dateTimeOfReport=" + dateTimeOfReport +
                ", fileSize=" + fileSize +
                ", fileId='" + fileId + '\'' +
                ", text='" + text + '\'' +
                ", chatId=" + chatId +
                '}';
    }
}
