package ru.council.telegram.support.reply.models;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.council.telegram.support.reply.enums.ResponseType;

import javax.ws.rs.NotAllowedException;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Getter
public abstract class UserResponse {

    protected ResponseType responseType;
    @NonNull
    @Setter
    protected String pathToMarkdownV2ResponseBody;
    @Setter
    protected List<InputFile> audios;
    @Setter
    protected List<InputFile> photos;
    @Setter
    protected List<InputFile> documents;
    @Setter
    protected List<InputFile> videos;
    @Setter
    protected Map<String, Object> freemarkerParameters;
    @Setter
    protected InlineKeyboardMarkup inlineKeyboardMarkup;
    @Setter
    protected Function<String, String> templateTextPostProcessor = null;

    protected <T> T throwNotAllowed() {
        throw new NotAllowedException("Method not allowed!");
    }

    public String getTextFromTemplate() throws IOException, TemplateException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);
        Path path = Path.of(pathToMarkdownV2ResponseBody);
        cfg.setDirectoryForTemplateLoading(path.getParent().toFile());
        Template template = cfg.getTemplate(path.getFileName().toString(), "UTF-8");
        StringWriter stringWriter = new StringWriter();
        template.process(freemarkerParameters, stringWriter);
        String result = stringWriter.getBuffer().toString();
        if (templateTextPostProcessor != null) {
            return templateTextPostProcessor.apply(result);
        }
        return result;
    }

    public abstract List<Message> process(TelegramLongPollingBot bot, long chatId) throws TelegramApiException, TemplateException, IOException;

    protected void processInputMedia(InputMedia inputMedia, @NonNull InputFile inputFile) {
        if (inputFile.getNewMediaStream() != null) {
            inputMedia.setMedia(inputFile.getNewMediaStream(), inputFile.getMediaName());
        } else if (inputFile.getNewMediaFile() != null) {
            inputMedia.setMedia(inputFile.getNewMediaFile(), inputFile.getMediaName());
        } else {
            throw new IllegalStateException("Wrong photo passed!");
        }
    }

    protected SendMediaGroup getSendMediaGroup(long chatId, @NonNull List<InputFile> files) throws TemplateException, IOException {
        SendMediaGroup sendMediaGroup = new SendMediaGroup();
        sendMediaGroup.setChatId(chatId);
        List<InputMedia> inputMedias = new ArrayList<>();
        for (InputFile file : files) {
            InputMedia inputMedia = getInputMedia(file);
            if (inputMedias.size() == 0) {
                inputMedia.setCaption(getTextFromTemplate());
                inputMedia.setParseMode(ParseMode.MARKDOWNV2);
            }
            inputMedias.add(inputMedia);
        }
        sendMediaGroup.setMedias(inputMedias);
        return sendMediaGroup;
    }

    protected abstract InputMedia getInputMedia(InputFile file);

    protected List<Message> processInputFiles(List<InputFile> files, TelegramLongPollingBot bot, long chatId) throws TelegramApiException, TemplateException, IOException {
        if (files != null) {
            if (files.size() == 0) {
                throw new IllegalStateException("No files passed to photo response! Use simple message for that purpose!");
            } else if (files.size() == 1) {
                return List.of(processSingleFile(files.get(0), bot, chatId));
            } else {
                SendMediaGroup sendMediaGroup = getSendMediaGroup(chatId, files);
                return bot.execute(sendMediaGroup);
            }
        } else {
            throw new IllegalStateException("No files passed to photo response! Use simple message for that purpose!");
        }
    }

    public abstract Message processSingleFile(InputFile file, TelegramLongPollingBot bot, long chatId) throws TelegramApiException, TemplateException, IOException;

}
