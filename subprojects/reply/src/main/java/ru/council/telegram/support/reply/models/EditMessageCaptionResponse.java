package ru.council.telegram.support.reply.models;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Function;

@Data
@EqualsAndHashCode(callSuper = true)
public class EditMessageCaptionResponse extends EditMessageInlineKeyboardResponse {
    private String captionTextFilePath;
    private Map<String, Object> freemarkerOptions;
    protected Function<String, String> templateTextPostProcessor = null;

    private String getTextForCaptionFromTemplate() throws IOException, TemplateException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);
        Path path = Path.of(captionTextFilePath);
        cfg.setDirectoryForTemplateLoading(path.getParent().toFile());
        Template template = cfg.getTemplate(path.getFileName().toString());
        StringWriter stringWriter = new StringWriter();
        template.process(freemarkerOptions, stringWriter);
        String result = stringWriter.getBuffer().toString();
        if (templateTextPostProcessor != null) {
            return templateTextPostProcessor.apply(result);
        }
        return result;
    }

    @Override
    public Message process(TelegramLongPollingBot bot) throws TelegramApiException, TemplateException, IOException {
        EditMessageCaption editMessageCaption = new EditMessageCaption();
        editMessageCaption.setCaption(getTextForCaptionFromTemplate());
        editMessageCaption.setParseMode(ParseMode.MARKDOWNV2);
        editMessageCaption.setMessageId(getMessageId());
        editMessageCaption.setChatId(getChatId());
        editMessageCaption.setReplyMarkup(getInlineKeyboardMarkup());
        return (Message) bot.execute(editMessageCaption);
    }
}
