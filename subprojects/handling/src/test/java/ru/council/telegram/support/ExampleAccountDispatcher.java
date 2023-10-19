package ru.council.telegram.support;

import ru.council.telegram.support.annotations.Dispatcher;

@Dispatcher(isMain = true)
public class ExampleAccountDispatcher {

    public BotState state;

    public ExampleAccountDispatcher(BotState state) {
        this.state = state;
    }

    /**
     * Called on user move to this dispatcher!
     */
    @Dispatcher.OnStartInteraction
    public void onStartInteraction() {

    }

    public void viewRequests() {

    }

    public void viewStats() {

    }

}
