package com.github.timmyovo.pixeluitweaks.server.manager;

import com.github.timmyovo.pixeluitweaks.common.event.models.ComponentEventModel;
import com.github.timmyovo.pixeluitweaks.common.event.models.EventModel;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class CallbackManager {
    private Map<UUID, Consumer<ComponentEventModel>> callbackMap = Maps.newHashMap();
    private Map<Class, Consumer<EventModel>> commonEventCallbackMap = Maps.newHashMap();

    public void notifyComponentEvent(ComponentEventModel componentEventModel) {
        callbackMap.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(componentEventModel.getComponentModel().getComponentId()))
                .map(Map.Entry::getValue)
                .forEach(consumer -> consumer.accept(componentEventModel));
    }

    public void notifyEvent(EventModel eventModel) {
        Optional extraData = eventModel.getExtraData();
        if (!extraData.isPresent()) {
            return;
        }
        commonEventCallbackMap.entrySet()
                .stream()
                .filter(classConsumerEntry -> classConsumerEntry.getKey().isAssignableFrom(extraData.get().getClass()))
                .map(Map.Entry::getValue)
                .forEach(consumer -> consumer.accept(eventModel));
    }
}