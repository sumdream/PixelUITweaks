package com.github.timmyovo.pixeluitweaks.common.event;

import com.github.timmyovo.pixeluitweaks.common.api.ISerializable;
import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MouseInputModel implements ISerializable {
    @SerializedName("componentContainer")
    private List<ComponentContainer> componentContainer;
    @SerializedName("mouseX")
    private int mouseX;
    @SerializedName("mouseY")
    private int mouseY;
    @SerializedName("mouseButton")
    private int mouseButton;
}
