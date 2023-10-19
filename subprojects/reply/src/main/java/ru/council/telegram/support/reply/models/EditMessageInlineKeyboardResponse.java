package ru.council.telegram.support.reply.models;

import freemarker.template.TemplateException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

@Data
@EqualsAndHashCode
public class EditMessageInlineKeyboardResponse {
    private Long chatId;
    private Integer messageId;
    private InlineKeyboardMarkup inlineKeyboardMarkup;

    public Message process(TelegramLongPollingBot bot) throws TelegramApiException, IOException, TemplateException {
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setChatId(chatId);
        editMessageReplyMarkup.setMessageId(messageId);
        editMessageReplyMarkup.setReplyMarkup(inlineKeyboardMarkup);
        return (Message) bot.execute(editMessageReplyMarkup);
    }
}
