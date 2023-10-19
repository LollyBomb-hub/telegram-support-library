package ru.council.telegram.support.reply.models;

import freemarker.template.TemplateException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.council.telegram.support.reply.enums.ResponseType;

import java.io.IOException;
import java.util.List;

public class PhotoResponse extends UserResponse {

    {
        this.responseType = ResponseType.PHOTO;
    }

    @Override
    public List<InputFile> getAudios() {
        return throwNotAllowed();
    }

    @Override
    public void setAudios(List<InputFile> audios) {
        throwNotAllowed();
    }

    @Override
    public List<InputFile> getDocuments() {
        return throwNotAllowed();
    }

    @Override
    public void setDocuments(List<InputFile> documents) {
        throwNotAllowed();
    }

    @Override
    public List<InputFile> getVideos() {
        return throwNotAllowed();
    }

    @Override
    public void setVideos(List<InputFile> videos) {
        throwNotAllowed();
    }

    @Override
    public List<Message> process(TelegramLongPollingBot bot, long chatId) throws TelegramApiException, TemplateException, IOException {
        List<InputFile> photos = getPhotos();
        return processInputFiles(photos, bot, chatId);
    }

    @Override
    public Message processSingleFile(InputFile file, TelegramLongPollingBot bot, long chatId) throws TelegramApiException, TemplateException, IOException {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(getTextFromTemplate());
        sendPhoto.setParseMode(ParseMode.MARKDOWNV2);
        sendPhoto.setPhoto(file);
        sendPhoto.setReplyMarkup(inlineKeyboardMarkup);
        return bot.execute(sendPhoto);
    }

    protected InputMediaPhoto getInputMedia(InputFile photo) {
        InputMediaPhoto inputMediaPhoto = new InputMediaPhoto();
        processInputMedia(inputMediaPhoto, photo);
        return inputMediaPhoto;
    }
}
