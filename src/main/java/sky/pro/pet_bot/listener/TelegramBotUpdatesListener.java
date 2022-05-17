package sky.pro.pet_bot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private static final String START_CMD = "/start";
    private final TelegramBot telegramBot;

    public TelegramBotUpdatesListener(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }


    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            Message message = update.message();
            Long chatId = message.chat().id();
            if (update.message() != null && update.message().text().equals(START_CMD)){
                List<BotCommand> commandList = new ArrayList<>();
                commandList.add(new BotCommand("Information","Узнать информацию о приюте"));
                commandList.add(new BotCommand("How take pet","Как взять собаку из приюта"));
                commandList.add(new BotCommand("Send report about pet","Прислать отчет о питомце"));
                commandList.add(new BotCommand("Call a volunteer","Позвать волонтера"));
                //this.execute(new SetMyCommands((BotCommand) commandList));
            } else {
                try{

                }catch (IllegalFormatException | DateTimeParseException e){

                    logger.error("Saving to DB failed:", e);
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void sendMessage(long chatId, List<BotCommand> commands){
        logger.info("Method sendMessage has been run: {}, {}", chatId, commands);
        SendMessage request = new SendMessage(chatId, "")
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true);

        SendResponse sendResponse = telegramBot.execute(request);
        if (!sendResponse.isOk()){
            int codeError = sendResponse.errorCode();
            String description = sendResponse.description();
            logger.info("code of error: {}", codeError);
            logger.info("description -: {}", description);
        }
    }
}
