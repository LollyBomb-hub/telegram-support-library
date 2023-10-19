package ru.council.telegram.support.reply.models;

import freemarker.template.TemplateException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaAudio;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.council.telegram.support.reply.enums.ResponseType;

import java.io.IOException;
import java.util.List;

public class AudioResponse extends UserResponse {

    {
        this.responseType = ResponseType.AUDIO;
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
    public List<InputFile> getDocuments() {
        return throwNotAllowed();
    }

    @Override
    public void setDocuments(List<InputFile> documents) {
        throwNotAllowed();
    }

    @Override
    public List<InputFile> getPhotos() {
        return throwNotAllowed();
    }

    @Override
    public void setPhotos(List<InputFile> photos) {
        throwNotAllowed();
    }

    @Override
    public List<Message> process(TelegramLongPollingBot bot, long chatId) throws TelegramApiException, TemplateException, IOException {
        List<InputFile> audios = getAudios();
        return processInputFiles(audios, bot, chatId);
    }

    @Override
    public Message processSingleFile(InputFile file, TelegramLongPollingBot bot, long chatId) throws TelegramApiException, TemplateException, IOException {
        SendAudio sendAudio = new SendAudio();
        sendAudio.setAudio(file);
        sendAudio.setChatId(chatId);
        sendAudio.setCaption(getTextFromTemplate());
        sendAudio.setParseMode(ParseMode.MARKDOWNV2);
        sendAudio.setReplyMarkup(inlineKeyboardMarkup);
        return bot.execute(sendAudio);
    }

    protected InputMediaAudio getInputMedia(InputFile audio) {
        InputMediaAudio inputMediaAudio = new InputMediaAudio();
        processInputMedia(inputMediaAudio, audio);
        return inputMediaAudio;
    }
}
