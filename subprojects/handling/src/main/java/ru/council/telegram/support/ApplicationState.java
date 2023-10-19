package ru.council.telegram.support;

import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class ApplicationState {
    private String canonicalScreenClassName;
    private String methodName;
    private boolean messagesAllowed;
    private long chatId;

    private Map<String, Object> additionalParameters = new HashMap<>();

    public ApplicationState(String canonicalScreenClassName, String methodName, boolean messagesAllowed, long chatId) {
        this.canonicalScreenClassName = canonicalScreenClassName;
        this.methodName = methodName;
        this.messagesAllowed = messagesAllowed;
        this.chatId = chatId;
    }

    public ApplicationState copy() {
        ApplicationState applicationState = new ApplicationState(canonicalScreenClassName, methodName, messagesAllowed, chatId);
        applicationState.setAdditionalParameters(new HashMap<>(additionalParameters));
        return applicationState;
    }

    public boolean equals(ApplicationState applicationState) {
        if (!Objects.equals(canonicalScreenClassName, applicationState.canonicalScreenClassName)) {
            return false;
        }
        if (!Objects.equals(methodName, applicationState.methodName)) {
            return false;
        }
        if (!Objects.equals(messagesAllowed, applicationState.messagesAllowed)) {
            return false;
        }
        if (!Objects.equals(chatId, applicationState.chatId)) {
            return false;
        }
        if (additionalParameters != null) {
            return additionalParameters.equals(applicationState.additionalParameters);
        } else return applicationState.additionalParameters == null;
    }
}
