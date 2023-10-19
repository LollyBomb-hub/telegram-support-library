package ru.council.telegram.support.reply.models;

import freemarker.template.TemplateException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaVideo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.council.telegram.support.reply.enums.ResponseType;

import java.io.IOException;
import java.util.List;

public class VideoResponse extends UserResponse {

    {
        this.responseType = ResponseType.VIDEO;
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
    public List<Message> process(TelegramLongPollingBot bot, long chatId) throws TelegramApiException, TemplateException, IOException {
        List<InputFile> videos = getVideos();
        return processInputFiles(videos, bot, chatId);
    }

    @Override
    protected InputMedia getInputMedia(InputFile file) {
        InputMediaVideo inputMediaVideo = new InputMediaVideo();
        processInputMedia(inputMediaVideo, file);
        return inputMediaVideo;
    }

    @Override
    public Message processSingleFile(InputFile file, TelegramLongPollingBot bot, long chatId) throws TelegramApiException, TemplateException, IOException {
        SendVideo sendVideo = new SendVideo();
        sendVideo.setChatId(chatId);
        sendVideo.setCaption(getTextFromTemplate());
        sendVideo.setParseMode(ParseMode.MARKDOWNV2);
        sendVideo.setVideo(file);
        sendVideo.setReplyMarkup(inlineKeyboardMarkup);
        return bot.execute(sendVideo);
    }
}
