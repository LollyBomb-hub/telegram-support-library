package ru.council.telegram.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.*;
import ru.council.telegram.support.annotations.*;
import ru.council.telegram.support.exceptions.MethodWrapperAlreadySetException;
import ru.council.telegram.support.exceptions.NoMethodWrapperFoundException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DispatcherWrapper {
    private final Map<Long, MethodWrapper<?>> textMessageMethodWrapperFromId = new HashMap<>();
    private final Map<Long, MethodWrapper<?>> callbackQueryMethodWrapperFromId = new HashMap<>();
    private final Map<String, MethodWrapper<?>> textMessageMethodWrapperFromPattern = new HashMap<>();
    private final Map<String, MethodWrapper<?>> callbackQueryMethodWrapperFromPattern = new HashMap<>();
    private final Object dispatcherInstance;
    private final Class<?> dispatcherClass;
    private MethodWrapper<OnAudio> onAudioMethodWrapper;
    private MethodWrapper<OnPhoto> onPhotoMethodWrapper;
    private MethodWrapper<OnDocument> onDocumentMethodWrapper;
    private MethodWrapper<OnVideo> onVideoMethodWrapper;
    private MethodWrapper<OnVoice> onVoiceMethodWrapper;
    private MethodWrapper<Dispatcher.OnStartInteraction> onStartInteractionMethodWrapper;
    private MethodWrapper<Dispatcher.OnStopInteraction> onStopInteractionMethodWrapper;
    private MethodWrapper<Dispatcher.FallbackMethod> fallbackMethodMethodWrapper;

    public DispatcherWrapper(Object dispatcherInstance, Class<?> dispatcherClass) {
        this.dispatcherInstance = dispatcherInstance;
        this.dispatcherClass = dispatcherClass;
        processPassedValues();
    }

    public Object onStartInteraction(@NonNull Update update, @NonNull ApplicationState applicationState) throws InvocationTargetException, IllegalAccessException {
        if (onStartInteractionMethodWrapper != null) {
            return onStartInteractionMethodWrapper.execute(dispatcherInstance, update, applicationState);
        }
        return null;
    }

    public Object onMessage(@NonNull Message message, @NonNull ApplicationState applicationState) throws InvocationTargetException, IllegalAccessException {
        Document document = message.getDocument();
        if (document != null) {
            return executeMessageSpecificOrThrow(message, document, onDocumentMethodWrapper, applicationState);
        }
        Audio audio = message.getAudio();
        if (audio != null) {
            return executeMessageSpecificOrThrow(message, audio, onAudioMethodWrapper, applicationState);
        }
        Video video = message.getVideo();
        if (video != null) {
            return executeMessageSpecificOrThrow(message, video, onVideoMethodWrapper, applicationState);
        }
        Voice voice = message.getVoice();
        if (voice != null) {
            return executeMessageSpecificOrThrow(message, voice, onVoiceMethodWrapper, applicationState);
        }
        List<PhotoSize> photo = message.getPhoto();
        if (photo != null) {
            return executeMessageSpecificOrThrow(message, photo, onPhotoMethodWrapper, applicationState);
        }
        String text = message.getText();
        if (text != null) {
            // Check for text
            if (isNumeric(text)) {
                long id = Long.parseLong(text);
                if (textMessageMethodWrapperFromId.containsKey(id)) {
                    return textMessageMethodWrapperFromId.get(id).execute(dispatcherInstance, message, text, applicationState);
                }
            }
            for (String pattern : textMessageMethodWrapperFromPattern.keySet()) {
                Pattern compiled = Pattern.compile(pattern);
                Matcher matcher = compiled.matcher(text);
                if (matcher.matches()) {
                    return textMessageMethodWrapperFromPattern.get(pattern).execute(dispatcherInstance, message, text, matcher, applicationState);
                }
            }
        }
        // Did not match any
        if (fallbackMethodMethodWrapper != null) {
            return fallbackMethodMethodWrapper.execute(dispatcherInstance, message, applicationState);
        }
        throw new NoMethodWrapperFoundException("No method wrapper to solve this task.");
    }

    public Object onCallbackQuery(@NonNull CallbackQuery callbackQuery, @NonNull ApplicationState applicationState) throws InvocationTargetException, IllegalAccessException {
        String data = callbackQuery.getData();
        if (isNumeric(data)) {
            // check for id field
            long id = Long.parseLong(data);
            if (callbackQueryMethodWrapperFromId.containsKey(id)) {
                return callbackQueryMethodWrapperFromId.get(id).execute(dispatcherInstance, callbackQuery, applicationState);
            }
        }
        // check with pattern
        for (String pattern : callbackQueryMethodWrapperFromPattern.keySet()) {
            Pattern compiled = Pattern.compile(pattern);
            Matcher matcher = compiled.matcher(data);
            if (matcher.matches()) {
                return callbackQueryMethodWrapperFromPattern.get(pattern).execute(dispatcherInstance, callbackQuery, data, matcher, applicationState);
            }
        }
        if (fallbackMethodMethodWrapper != null) {
            return fallbackMethodMethodWrapper.execute(dispatcherInstance, callbackQuery, applicationState);
        }
        throw new NoMethodWrapperFoundException("No method wrapper to solve this task.");
    }

    public Object onStopInteraction(@NonNull Update update, @NonNull ApplicationState applicationState) throws InvocationTargetException, IllegalAccessException {
        if (onStopInteractionMethodWrapper != null) {
            return onStopInteractionMethodWrapper.execute(dispatcherInstance, update, applicationState);
        }
        return null;
    }

    public Object fallbackMethod(@NonNull Update update, @NonNull ApplicationState applicationState) throws InvocationTargetException, IllegalAccessException {
        if (fallbackMethodMethodWrapper != null) {
            return fallbackMethodMethodWrapper.execute(dispatcherInstance, update, applicationState);
        }
        return null;
    }

    public boolean hasFallbackMethod() {
        return fallbackMethodMethodWrapper != null;
    }

    private Object executeMessageSpecificOrThrow(@NonNull Message message, @NonNull Object argument, MethodWrapper<?> methodWrapper, @NonNull ApplicationState applicationState) throws InvocationTargetException, IllegalAccessException {
        if (methodWrapper != null) {
            return methodWrapper.execute(dispatcherInstance, message, argument, applicationState);
        } else {
            throw new NoMethodWrapperFoundException("No method wrapper found to solve this task.");
        }
    }

    private void processPassedValues() {
        Method[] methods = dispatcherClass.getMethods();
        for (Method m : methods) {
            OnCallbackQuery onCallbackQuery = m.getAnnotation(OnCallbackQuery.class);
            OnTextMessage onTextMessage = m.getAnnotation(OnTextMessage.class);
            OnAudio onAudio = m.getAnnotation(OnAudio.class);
            OnPhoto onPhoto = m.getAnnotation(OnPhoto.class);
            OnDocument onDocument = m.getAnnotation(OnDocument.class);
            OnVideo onVideo = m.getAnnotation(OnVideo.class);
            OnVoice onVoice = m.getAnnotation(OnVoice.class);
            Dispatcher.OnStartInteraction onStartInteraction = m.getAnnotation(Dispatcher.OnStartInteraction.class);
            Dispatcher.OnStopInteraction onStopInteraction = m.getAnnotation(Dispatcher.OnStopInteraction.class);
            Dispatcher.FallbackMethod fallbackMethod = m.getAnnotation(Dispatcher.FallbackMethod.class);
            if (onStartInteraction != null) {
                onStartInteractionMethodWrapper = getMethodWrapperOrThrow(onStartInteractionMethodWrapper, m, onStartInteraction);
            } else if (onStopInteraction != null) {
                onStopInteractionMethodWrapper = getMethodWrapperOrThrow(onStopInteractionMethodWrapper, m, onStopInteraction);
            } else if (fallbackMethod != null) {
                fallbackMethodMethodWrapper = getMethodWrapperOrThrow(fallbackMethodMethodWrapper, m, fallbackMethod);
            } else if (onAudio != null) {
                onAudioMethodWrapper = getMethodWrapperOrThrow(onAudioMethodWrapper, m, onAudio);
            } else if (onVoice != null) {
                onVoiceMethodWrapper = getMethodWrapperOrThrow(onVoiceMethodWrapper, m, onVoice);
            } else if (onPhoto != null) {
                onPhotoMethodWrapper = getMethodWrapperOrThrow(onPhotoMethodWrapper, m, onPhoto);
            } else if (onDocument != null) {
                onDocumentMethodWrapper = getMethodWrapperOrThrow(onDocumentMethodWrapper, m, onDocument);
            } else if (onVideo != null) {
                onVideoMethodWrapper = getMethodWrapperOrThrow(onVideoMethodWrapper, m, onVideo);
            } else if (onTextMessage != null) {
                if (onTextMessage.isPattern()) {
                    if (textMessageMethodWrapperFromPattern.containsKey(onTextMessage.pattern())) {
                        throw new MethodWrapperAlreadySetException("Method wrapper for pattern = " + onTextMessage.pattern() + " already set.");
                    }
                    textMessageMethodWrapperFromPattern.put(onTextMessage.pattern(), new MethodWrapper<>(m, onTextMessage));
                } else {
                    if (textMessageMethodWrapperFromId.containsKey(onTextMessage.id())) {
                        throw new MethodWrapperAlreadySetException("Method wrapper for id = " + onTextMessage.id() + " already set.");
                    }
                    textMessageMethodWrapperFromId.put(onTextMessage.id(), new MethodWrapper<>(m, onTextMessage));
                }
            } else if (onCallbackQuery != null) {
                if (onCallbackQuery.isPattern()) {
                    if (callbackQueryMethodWrapperFromPattern.containsKey(onCallbackQuery.pattern())) {
                        throw new MethodWrapperAlreadySetException("Method wrapper for pattern = " + onCallbackQuery.pattern() + " already set.");
                    }
                    callbackQueryMethodWrapperFromPattern.put(onCallbackQuery.pattern(), new MethodWrapper<>(m, onCallbackQuery));
                } else {
                    if (callbackQueryMethodWrapperFromId.containsKey(onCallbackQuery.id())) {
                        throw new MethodWrapperAlreadySetException("Method wrapper for id = " + onCallbackQuery.id() + " already set.");
                    }
                    callbackQueryMethodWrapperFromId.put(onCallbackQuery.id(), new MethodWrapper<>(m, onCallbackQuery));
                }
            }
        }
    }

    private <T> MethodWrapper<T> getMethodWrapperOrThrow(Object target, Method m, T annotation) {
        if (target != null) {
            // already set
            throw new MethodWrapperAlreadySetException("Method wrapper already exists.");
        }
        return new MethodWrapper<>(m, annotation);
    }

    private boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    @Data
    @AllArgsConstructor
    private static class MethodWrapper<AnnotationType> {
        private Method method;
        private AnnotationType annotation;

        public Object execute(Object instanceOfDispatcher, Object... params) throws InvocationTargetException, IllegalAccessException {
            return method.invoke(instanceOfDispatcher, params);
        }
    }

}
