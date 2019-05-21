package com.github.timmyovo.pixeluitweaks.common.event;

import com.github.timmyovo.pixeluitweaks.common.api.ISerializable;
import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyboardInputModel implements ISerializable {
    @SerializedName("componentContainer")
    private ComponentContainer componentContainer;
    @SerializedName("keycode")
    private int keycode;
    @SerializedName("typedChar")
    private char typedChar;
}
