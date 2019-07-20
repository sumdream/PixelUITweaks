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
public class KeyboardInputModel implements EventModel<KeyboardInputModel.InputEntry> {

    private InputEntry inputEntry;

    @Override
    public Optional<InputEntry> getExtraData() {
        return Optional.of(inputEntry);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class InputEntry {
        @SerializedName("componentContainer")
        private List<ComponentContainer> componentContainer;
        @SerializedName("keycode")
        private int keycode;
        @SerializedName("typedChar")
        private char typedChar;
    }
}
