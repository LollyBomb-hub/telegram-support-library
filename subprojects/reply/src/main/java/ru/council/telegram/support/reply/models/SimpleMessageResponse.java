package ru.council.telegram.support.reply.models;

import freemarker.template.TemplateException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.council.telegram.support.reply.enums.ResponseType;

import java.io.IOException;
import java.util.List;

public class SimpleMessageResponse extends UserResponse {

    {
        this.responseType = ResponseType.SIMPLE_MESSAGE;
    }

    @Override
    public List<InputFile> getAudios() {
        return throwNotAllowed();
    }

    @Override
    public List<InputFile> getPhotos() {
        return throwNotAllowed();
    }

    @Override
    public List<InputFile> getDocuments() {
        return throwNotAllowed();
    }

    @Override
    public List<InputFile> getVideos() {
        return throwNotAllowed();
    }

    @Override
    public void setAudios(List<InputFile> audios) {
        throwNotAllowed();
    }

    @Override
    public void setPhotos(List<InputFile> photos) {
        throwNotAllowed();
    }

    @Override
    public void setDocuments(List<InputFile> documents) {
        throwNotAllowed();
    }

    @Override
    public void setVideos(List<InputFile> videos) {
        throwNotAllowed();
    }

    @Override
    public List<Message> process(TelegramLongPollingBot bot, long chatId) throws TelegramApiException, TemplateException, IOException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(getTextFromTemplate());
        sendMessage.setParseMode(ParseMode.MARKDOWNV2);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return List.of(bot.execute(sendMessage));
    }

    @Override
    protected InputMedia getInputMedia(InputFile file) {
        return throwNotAllowed();
    }

    @Override
    public Message processSingleFile(InputFile file, TelegramLongPollingBot bot, long chatId) {
        return throwNotAllowed();
    }
}
