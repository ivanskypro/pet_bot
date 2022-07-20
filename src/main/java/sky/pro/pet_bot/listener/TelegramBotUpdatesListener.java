package sky.pro.pet_bot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sky.pro.pet_bot.dao.AnswerRepository;
import sky.pro.pet_bot.dao.ReportRepository;
import sky.pro.pet_bot.dao.UserRepository;
import sky.pro.pet_bot.dao.VolunteerRepository;
import sky.pro.pet_bot.model.Answer;
import sky.pro.pet_bot.model.Report;
import sky.pro.pet_bot.model.User;
import sky.pro.pet_bot.model.Volunteer;
import sky.pro.pet_bot.service.impl.AnswerServiceInterfaceImpl;
import sky.pro.pet_bot.service.impl.PictureServiceInterfaceImpl;
import sky.pro.pet_bot.service.impl.ReportServiceInterfaceImpl;
import sky.pro.pet_bot.service.impl.VolunteerServiceInterfaceImpl;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private static final String START_CMD = "/start";

    private final TelegramBot telegramBot;

    private final AnswerServiceInterfaceImpl answerServiceInterface;

    private final AnswerRepository answerRepository;
    private final PictureServiceInterfaceImpl pictureServiceInterface;
    private final UserRepository userRepository;
    private final VolunteerServiceInterfaceImpl volunteerServiceInterface;
    private final VolunteerRepository volunteerRepository;
    private final ReportRepository reportRepository;
    private final ReportServiceInterfaceImpl reportServiceInterface;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, AnswerServiceInterfaceImpl answerServiceInterface, AnswerRepository answerRepository, PictureServiceInterfaceImpl pictureServiceInterface, UserRepository userRepository, VolunteerServiceInterfaceImpl volunteerServiceInterface, VolunteerRepository volunteerRepository, ReportRepository reportRepository, ReportServiceInterfaceImpl reportServiceInterface) {
        this.telegramBot = telegramBot;
        this.answerServiceInterface = answerServiceInterface;
        this.answerRepository = answerRepository;
        this.pictureServiceInterface = pictureServiceInterface;
        this.userRepository = userRepository;
        this.volunteerServiceInterface = volunteerServiceInterface;
        this.volunteerRepository = volunteerRepository;
        this.reportRepository = reportRepository;
        this.reportServiceInterface = reportServiceInterface;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }


    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            try {
                Long chatId = update.message().chat().id();

                 if ( update.message().photo() != null){
                         logger.info("Сохраняю отчет");
                         Report report = new Report();
                         LocalDateTime dateTimeOfReportUpload = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
                         report.setDateTimeOfReport(dateTimeOfReportUpload);
                         String caption = update.message().caption();
                         report.setText(caption);
                         String fileId = update.message().photo()[2].fileId();
                         report.setFileId(fileId);
                         Integer fileSize = update.message().photo()[2].fileSize();
                         report.setFileSize(fileSize);
                         report.setChatId(chatId);
                         reportRepository.save(report);

                         SendMessage message = new SendMessage(update.message().chat().id(), "Отчет сохранен");
                         telegramBot.execute(message);

                         telegramBot.execute(new SendPhoto(volunteerRepository.findById(1L).get().getChatId(), report.getFileId()));
                         telegramBot.execute(new SendMessage(volunteerRepository.findById(1L).get().getChatId(), "Владелец "+ update.message().chat().firstName() + " прислал отчёт с сообщением: " + report.getText()));

                     }
                 if (update.message().text().equals(START_CMD)) {
                            Keyboard keyboard = new ReplyKeyboardMarkup("Кошки", "Собаки");
                            SendMessage request = new SendMessage(chatId, "Привет! Выбери один из приютов для получения info")
                                    .replyMarkup(keyboard);
                            telegramBot.execute(request);
                        }
                 if (update.message().text().equals("Назад")) {
                            Keyboard keyboard = new ReplyKeyboardMarkup("Кошки", "Собаки");
                            SendMessage request = new SendMessage(chatId, "Давай попробуем ещё раз! Выбери приют снова!")
                                    .replyMarkup(keyboard);
                            telegramBot.execute(request);
                        }
                 if (update.message().text().equals("Как до нас добраться")){
                     SendMessage message = new SendMessage(chatId, answerServiceInterface.getAnswerById(1L).getTextMessage());
                     telegramBot.execute(message);
                     Keyboard keyboard = new ReplyKeyboardMarkup("Кошки", "Собаки");
                     SendMessage request = new SendMessage(chatId, "Выбери приют!")
                                    .replyMarkup(keyboard);
                     telegramBot.execute(request);
                 }
                if (update.message().text().equals("Соединить с волонтером")) {
                    SendMessage request = new SendMessage(chatId, "Напишите свой номер телефона и мы с вами свяжемся");
                    telegramBot.execute(request);
                }


                if (update.message().text().matches("[0-9]+")){
                    Keyboard keyboard = new ReplyKeyboardMarkup("Кошки", "Собаки");
                    SendMessage request = new SendMessage(chatId, "Хорошо, Волонтер с Вами обязательно свяжется").replyMarkup(keyboard);
                    telegramBot.execute(request);

                    User user = new User();
                    user.setChatId(chatId);
                    user.setName(update.message().chat().firstName());
                    user.setPhoneNumber(update.message().text());
                    userRepository.save(user);

                    Long volunteerChatId = volunteerRepository.findById(1L).get().getChatId();

                    SendMessage messageToVolunteer = new SendMessage( volunteerChatId, "Привет, пользователь "+ update.message().chat().firstName() +
                                    " " + " просил позвонить по этому номеру:"+ update.message().text()+
                                    " chatId: " + update.message().chat().id());
                    telegramBot.execute(messageToVolunteer);
                }

                if (update.message().text().equals("Кошки")) {
                    ReplyKeyboardMarkup keyboard = keyboardCreator("Как ухаживать за кошками",
                            "Как добраться до приюта котов",
                            "Какие нужны документы для усыновления кота",
                            "Что важно знать о котах вообще",
                            "Соединить с волонтером",
                            "Назад");
                    SendMessage request = new SendMessage(chatId, "Хорошо, значит коты:)\nВот немного info о нашем кошачьем приюте, а также общие правила обращения с кошками")
                            .replyMarkup(keyboard);
                    telegramBot.execute(request);
                }


                if (update.message().text().equals("Как ухаживать за кошками")){
                    Optional<Answer> answer = answerRepository.findById(1L);
                    String answerText = answer.get().getTextMessage();

                    SendMessage message = new SendMessage(chatId, answerText);
                    telegramBot.execute(message);
                }

                if (update.message().text().equals("Как добраться до приюта котов")){
                    Optional<Answer> answer = answerRepository.findById(2L);
                    String answerText = answer.get().getTextMessage();

                    SendMessage message = new SendMessage(chatId, answerText);
                    telegramBot.execute(message);
                }

                if (update.message().text().equals("Какие нужны документы для усыновления кота")){
                    Optional<Answer> answer = answerRepository.findById(3L);
                    String answerText = answer.get().getTextMessage();

                    SendMessage message = new SendMessage(chatId, answerText);
                    telegramBot.execute(message);
                }

                if (update.message().text().equals("Что важно знать о котах вообще")){
                    Optional<Answer> answer = answerRepository.findById(4L);
                    String answerText = answer.get().getTextMessage();

                    SendMessage message = new SendMessage(chatId, answerText);
                    telegramBot.execute(message);
                }

                if (update.message().text().equals("Собаки")) {
                    ReplyKeyboardMarkup keyboard = keyboardCreator("Как ухаживать за собаками",
                            "Как добраться до приюта собак",
                            "Какие нужны документы для усыновления собаки",
                            "Что важно знать о собаках вообще",
                            "Соединить с волонтером",
                            "Назад");
                    SendMessage request = new SendMessage(chatId, "Хорошо, значит собаки:)\nВот немного info о нашем собачьем приюте, а также общие правила обращения с собаками")
                            .replyMarkup(keyboard);
                    telegramBot.execute(request);
                }

                if (update.message().text().equals("Как ухаживать за собаками")){
                    Optional<Answer> answer = answerRepository.findById(5L);
                    String answerText = answer.get().getTextMessage();

                    SendMessage message = new SendMessage(chatId, answerText);
                    telegramBot.execute(message);
                }

                if (update.message().text().equals("Как добраться до приюта собак")){
                    Optional<Answer> answer = answerRepository.findById(6L);
                    String answerText = answer.get().getTextMessage();

                    SendMessage message = new SendMessage(chatId, answerText);
                    telegramBot.execute(message);
                }

                if (update.message().text().equals("Какие нужны документы для усыновления собаки")){
                    Optional<Answer> answer = answerRepository.findById(7L);
                    String answerText = answer.get().getTextMessage();

                    SendMessage message = new SendMessage(chatId, answerText);
                    telegramBot.execute(message);
                }

                if (update.message().text().equals("Что важно знать о собаках вообще")){
                    Optional<Answer> answer = answerRepository.findById(8L);
                    String answerText = answer.get().getTextMessage();

                    SendMessage message = new SendMessage(chatId, answerText);
                    telegramBot.execute(message);
                }


                if (update.message().text().equals("wbv2022")){
                    Volunteer volunteer = new Volunteer();
                            volunteer.setChatId(update.message().chat().id());
                            volunteer.setName(update.message().chat().firstName());
                            volunteerRepository.save(volunteer);
                            Keyboard keyboard = keyboardCreator("Подтвердить испытательный срок",
                                    "Изменить испытательный срок",
                                    "Создать пользователя");

                            SendMessage request = new SendMessage(chatId, "Поздравляю, теперь ты Волонтер!").replyMarkup(keyboard);
                            telegramBot.execute(request);
                        }

                 if (update.message().text().equals("Изменить испытательный срок")){
            }

                 if (doesVolunteerExist(chatId) && update.message().text().equals(START_CMD)){
                            SendMessage message = new SendMessage(chatId, "Привет, волонтер!");
                            telegramBot.execute(message);
                        }

                 if (update.message().text().equals("Продлить срок на 14 дней")){

                 }

                    } catch (NullPointerException | NoSuchElementException ignored) {
                    }
                }
        );
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public ReplyKeyboardMarkup keyboardCreator (String ... buttons){
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(buttons[0]);
        for (int i = 1; i < buttons.length ; i++) {
            keyboard.addRow(buttons[i]);
        }
        return keyboard.oneTimeKeyboard(true);
    }

    public boolean doesVolunteerExist(Long chatId){
        Volunteer volunteer = volunteerRepository.findByChatId(chatId);
        Long volunteerId = volunteer.getChatId();
        return volunteerId.equals(chatId);
    }

    @Scheduled(cron ="0 0 10 * * *" )
    public void reportChecking (){
        logger.info("Проверка отчетов работает");
    LocalDateTime lastReportDate = reportRepository.getLastReport(1).getDateTimeOfReport();
    LocalDateTime actualDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
    if (lastReportDate.isBefore(actualDateTime)&&(lastReportDate.getDayOfMonth()-actualDateTime.getDayOfMonth())>2){
        Long userChatId = reportRepository.getLastReport(1).getChatId();
        SendMessage message = new SendMessage(userChatId, "Вы забыли представить отчёт волонтеру");
        telegramBot.execute(message);

        Long volunteerChatId = volunteerRepository.findById(1L).get().getChatId();
        User user = userRepository.findByChatId(userChatId);
        String userName = user.getName();
        SendMessage messageToVolunteer = new SendMessage(volunteerChatId, "Владелец: "+ userName + "нарушил сроки сдачи отчета");
        telegramBot.execute(messageToVolunteer);
    }
    }

    @Scheduled(cron ="0 0 9 * * *" )
    public void probationPeriodCounter (){
        logger.info("Counter is working");
        List<User> allUsers = userRepository.findAll();
        for (User user: allUsers){
            if (user.getProbationPeriod()!=0){
                int probationPeriod = user.getProbationPeriod()-1;
                user.setProbationPeriod(probationPeriod);
                userRepository.save(user);
            }
            if (user.isOwner() && user.getProbationPeriod()==0){

                SendMessage message = new SendMessage(volunteerRepository.findById(1L).get().getChatId(), "У владельца "
                        +user.getName()+" закончился испытательный период" +
                        "\n пожалуйста, установите новый испытательный срок для этого владельца в Личном кабинете, либо отметьте его успешно прошедшим срок");
                telegramBot.execute(message);
            }
        }



    }









}

