package ru.council.telegram.support;

import freemarker.template.TemplateException;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.council.telegram.support.reply.models.SimpleMessageResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class Test {

    @org.junit.jupiter.api.Test
    public void testState() {
        final String canonicalName = "canonicalName";
        final String method = "m";
        final long chatId = 1;
        ApplicationState as1 = new ApplicationState(canonicalName, method, false, chatId);
        as1.getAdditionalParameters().put("p1", 1);
        ApplicationState as2 = new ApplicationState(canonicalName, method, false, chatId);
        as2.getAdditionalParameters().put("p1", 1);
        System.out.println(as1.equals(as2));
        as1.getAdditionalParameters().put("p2", 1);
        System.out.println(as1.equals(as2));
    }

}
