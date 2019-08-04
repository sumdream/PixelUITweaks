package com.github.timmyovo.pixeluitweaks.server.manager;

import com.github.timmyovo.pixeluitweaks.common.api.IComp;
import com.github.timmyovo.pixeluitweaks.common.event.models.ComponentEventModel;
import com.github.timmyovo.pixeluitweaks.common.event.models.EventModel;
import com.github.timmyovo.pixeluitweaks.common.gui.component.AbstractComponent;
import com.google.common.collect.Maps;
import javafx.util.Pair;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class CallbackManager implements IComp<CallbackManager> {
    private Map<UUID, Consumer<ComponentEventModel>> componentEventCallbackMap = Maps.newHashMap();
    private Map<Class, Consumer<Pair<Player, EventModel>>> commonEventCallbackMap = Maps.newHashMap();

    public <T extends AbstractComponent> void registerComponentCallback(T abstractComponent, Consumer<ComponentEventModel> consumer) {
        componentEventCallbackMap.put(abstractComponent.getComponentId(), consumer);
    }

    public void registerCommonCallback(Class eventTypeClass, Consumer<Pair<Player, EventModel>> eventModelConsumer) {
        this.commonEventCallbackMap.put(eventTypeClass, eventModelConsumer);
    }

    public void notifyComponentEvent(ComponentEventModel componentEventModel) {
        componentEventCallbackMap.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(componentEventModel.getComponentModel().getComponentId()))
                .map(Map.Entry::getValue)
                .forEach(consumer -> consumer.accept(componentEventModel));
    }

    public void notifyEvent(EventModel eventModel, Player player) {
        Optional extraData = eventModel.getExtraData();
        if (!extraData.isPresent()) {
            return;
        }
        commonEventCallbackMap.entrySet()
                .stream()
                .filter(classConsumerEntry -> classConsumerEntry.getKey().isAssignableFrom(extraData.get().getClass()))
                .map(Map.Entry::getValue)
                .forEach(consumer -> consumer.accept(new Pair<>(player, eventModel)));
    }

    @Override
    public CallbackManager init() {
        return this;
    }
}
