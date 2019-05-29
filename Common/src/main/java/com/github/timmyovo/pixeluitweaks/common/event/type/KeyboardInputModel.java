package com.github.timmyovo.pixeluitweaks.common.event.type;

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
public class KeyboardInputModel implements ISerializable {
    @SerializedName("componentContainer")
    private List<ComponentContainer> componentContainer;
    @SerializedName("keycode")
    private int keycode;
    @SerializedName("typedChar")
    private char typedChar;
}
