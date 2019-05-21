package com.github.timmyovo.pixeluitweaks.common.event;

import com.github.timmyovo.pixeluitweaks.common.api.ISerializable;
import com.github.timmyovo.pixeluitweaks.common.gui.GuiContainer;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyboardInputModel implements ISerializable {
    @SerializedName("guiContainer")
    private GuiContainer guiContainer;
    @SerializedName("keycode")
    private int keycode;
    @SerializedName("typedChar")
    private char typedChar;
}
