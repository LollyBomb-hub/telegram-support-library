package ru.council.telegram.support.reply.models;

import freemarker.template.TemplateException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MediaGroupResponse extends UserResponse {
    @Override
    public List<Message> process(TelegramLongPollingBot bot, long chatId) throws TelegramApiException, TemplateException, IOException {
        SendMediaGroup sendMediaGroup = new SendMediaGroup();
        sendMediaGroup.setChatId(chatId);
        List<InputMedia> inputMedias = new ArrayList<>();
        if (getAudios() != null) {
            for (InputFile audio: getAudios()) {
                InputMediaAudio inputMediaAudio = new InputMediaAudio();
                processInputMedia(inputMediaAudio, audio);
                inputMedias.add(inputMediaAudio);
            }
        }
        if (getPhotos() != null) {
            for (InputFile photo: getPhotos()) {
                InputMediaPhoto inputMediaPhoto = new InputMediaPhoto();
                processInputMedia(inputMediaPhoto, photo);
                inputMedias.add(inputMediaPhoto);
            }
        }
        if (getDocuments() != null) {
            for (InputFile document: getDocuments()) {
                InputMediaDocument inputMediaDocument = new InputMediaDocument();
                processInputMedia(inputMediaDocument, document);
                inputMedias.add(inputMediaDocument);
            }
        }
        if (getVideos() != null) {
            for (InputFile video: getVideos()) {
                InputMediaVideo inputMediaVideo = new InputMediaVideo();
                processInputMedia(inputMediaVideo, video);
                inputMedias.add(inputMediaVideo);
            }
        }
        if (inputMedias.size() == 0) {
            throw new IllegalStateException("Input medias are not passed!");
        }
        inputMedias.get(0).setCaption(getTextFromTemplate());
        inputMedias.get(0).setParseMode(ParseMode.MARKDOWNV2);
        sendMediaGroup.setMedias(inputMedias);
        return bot.execute(sendMediaGroup);
    }

    @Override
    protected InputMedia getInputMedia(InputFile file) {
        return null;
    }

    @Override
    public Message processSingleFile(InputFile file, TelegramLongPollingBot bot, long chatId) throws TelegramApiException {
        return null;
    }
}
