package ru.council.telegram.support;

import org.checkerframework.checker.units.qual.A;
import ru.council.telegram.support.annotations.Dispatcher;
import ru.council.telegram.support.reply.models.UserResponse;

import java.util.ArrayList;
import java.util.List;

@Dispatcher
public class ExampleRequestsDispatcher {

    public BotState state;

    public ExampleRequestsDispatcher(BotState state) {
        this.state = state;
    }

//    @OnCallbackQueryData(id=1)
    public List<UserResponse> listRequests() {
        return new ArrayList<>();
    }

//    @OnDocument
    public List<UserResponse> uploadNewRequests() {
        return new ArrayList<>();
    }
}
