package ru.council.telegram.support.models;

import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

@Data
public class OnUpdateProcessed {

    private List<Integer> editedMessages = new ArrayList<>();
    private List<Integer> couldNotDeleteMessages = new ArrayList<>();
    private List<Integer> deletedMessages = new ArrayList<>();
    private List<Message> sentMessages = new ArrayList<>();

    public void append(OnUpdateProcessed anotherResult) {
        this.editedMessages.addAll(anotherResult.getEditedMessages());
        this.couldNotDeleteMessages.addAll(anotherResult.getCouldNotDeleteMessages());
        this.deletedMessages.addAll(anotherResult.getDeletedMessages());
        this.sentMessages.addAll(anotherResult.getSentMessages());
    }

}
