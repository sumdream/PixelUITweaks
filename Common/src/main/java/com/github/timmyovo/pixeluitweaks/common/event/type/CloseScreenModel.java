package com.github.timmyovo.pixeluitweaks.common.event.type;

import com.github.timmyovo.pixeluitweaks.common.event.models.EventModel;
import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloseScreenModel implements EventModel<List<ComponentContainer>> {
    @SerializedName(value = "screenContainers")
    private List<ComponentContainer> screenContainers;

    @Override
    public Optional<List<ComponentContainer>> getExtraData() {
        return Optional.of(screenContainers);
    }
}
