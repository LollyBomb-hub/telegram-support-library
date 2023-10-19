package ru.council.telegram.support.reply.models;

import freemarker.template.TemplateException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaDocument;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.council.telegram.support.reply.enums.ResponseType;

import java.io.IOException;
import java.util.List;

public class DocumentResponse extends UserResponse {

    {
        this.responseType = ResponseType.DOCUMENT;
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
    public List<InputFile> getPhotos() {
        return throwNotAllowed();
    }

    @Override
    public void setPhotos(List<InputFile> photos) {
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
        List<InputFile> documents = getDocuments();
        return processInputFiles(documents, bot, chatId);
    }

    @Override
    public Message processSingleFile(InputFile file, TelegramLongPollingBot bot, long chatId) throws TelegramApiException, TemplateException, IOException {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        sendDocument.setDocument(file);
        sendDocument.setCaption(getTextFromTemplate());
        sendDocument.setParseMode(ParseMode.MARKDOWNV2);
        sendDocument.setReplyMarkup(inlineKeyboardMarkup);
        return bot.execute(sendDocument);
    }

    public InputMediaDocument getInputMedia(InputFile document) {
        InputMediaDocument inputMediaDocument = new InputMediaDocument();
        processInputMedia(inputMediaDocument, document);
        return inputMediaDocument;
    }
}
