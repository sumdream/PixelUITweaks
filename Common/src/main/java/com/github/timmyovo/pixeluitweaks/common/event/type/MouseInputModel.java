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
public class MouseInputModel implements EventModel<MouseInputModel.MouseEventDataModel> {
    private MouseEventDataModel mouseEventDataModel;

    @Override
    public Optional<MouseEventDataModel> getExtraData() {
        return Optional.of(mouseEventDataModel);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MouseEventDataModel {
        @SerializedName("componentContainer")
        private List<ComponentContainer> componentContainer;
        @SerializedName("mouseX")
        private int mouseX;
        @SerializedName("mouseY")
        private int mouseY;
        @SerializedName("mouseButton")
        private int mouseButton;
    }
}
