package sky.pro.pet_bot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sky.pro.pet_bot.dao.AnswerRepository;
import sky.pro.pet_bot.dao.UserRepository;
import sky.pro.pet_bot.dao.VolunteerRepository;
import sky.pro.pet_bot.model.User;
import sky.pro.pet_bot.model.Volunteer;
import sky.pro.pet_bot.service.impl.AnswerServiceInterfaceImpl;
import sky.pro.pet_bot.service.impl.PictureServiceInterfaceImpl;
import sky.pro.pet_bot.service.impl.VolunteerServiceInterfaceImpl;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.NoSuchElementException;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private static final String START_CMD = "/start";
    String phonePatterns = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";
    private final TelegramBot telegramBot;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, AnswerServiceInterfaceImpl answerServiceInterface, AnswerRepository answerRepository, PictureServiceInterfaceImpl pictureServiceInterface, UserRepository userRepository, VolunteerServiceInterfaceImpl volunteerServiceInterface, VolunteerRepository volunteerRepository) {
        this.telegramBot = telegramBot;
        this.answerServiceInterface = answerServiceInterface;
        this.answerRepository = answerRepository;
        this.pictureServiceInterface = pictureServiceInterface;
        this.userRepository = userRepository;
        this.volunteerServiceInterface = volunteerServiceInterface;
        this.volunteerRepository = volunteerRepository;
    }

    private final AnswerServiceInterfaceImpl answerServiceInterface;
    private final AnswerRepository answerRepository;
    private final PictureServiceInterfaceImpl pictureServiceInterface;
    private final UserRepository userRepository;
    private final VolunteerServiceInterfaceImpl volunteerServiceInterface;
    private final VolunteerRepository volunteerRepository;

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
                        if (update.message().text().equals("Кошки")) {
                            ReplyKeyboardMarkup keyboard = keyboardCreator("Как ухаживать за кошками",
                                                                                    "Основные правила ухода за кошками",
                                                                                    "Как до нас добраться",
                                                                                    "Какие нужны документы",
                                                                                    "Что важно знать о котах вообще",
                                                                                    "Соединить с волонтером",
                                                                                    "Назад");
                           SendMessage request = new SendMessage(chatId, "Хорошо, значит коты:)\nВот немного info о нашем кошачьем приюте, а также общие правила обращения с кошками")
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

                        if (update.message().text().equals("Собаки")) {
                            ReplyKeyboardMarkup keyboard = keyboardCreator("Как ухаживать за собаками",
                                    "Основные правила ухода за собаками",
                                    "Как до нас добраться",
                                    "Какие нужны документы",
                                    "Что важно знать о собаках вообще",
                                    "Соединить с волонтером",
                                    "Назад");
                            SendMessage request = new SendMessage(chatId, "Хорошо, значит собаки:)\nВот немного info о нашем собачьем приюте, а также общие правила обращения с собаками")
                                    .replyMarkup(keyboard);
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
                            user.setOwner(false);
                            userRepository.save(user);

                            Long volunteerChatId = volunteerRepository.findById(1L).get().getChatId();

                            SendMessage messageToVolunteer = new SendMessage( volunteerChatId, "Привет, пользователь "+ update.message().chat().firstName() +
                                    " " + " просил позвонить по этому номеру:"+ update.message().text()+
                                    " chatId: " + update.message().chat().id());
                            telegramBot.execute(messageToVolunteer);
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
                        if (doesVolunteerExist(chatId) && update.message().text().equals(START_CMD)){
                            Keyboard keyboard = keyboardCreator("Подтвердить испытательный срок",
                                                                        "Изменить испытательный срок");

                            SendMessage message = new SendMessage(chatId, "Привет, волонтер!").replyMarkup(keyboard);
                            telegramBot.execute(message);
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






}

