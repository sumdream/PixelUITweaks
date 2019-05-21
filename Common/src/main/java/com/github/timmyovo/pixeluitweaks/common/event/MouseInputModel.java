package com.github.timmyovo.pixeluitweaks.common.event;

import com.github.timmyovo.pixeluitweaks.common.api.ISerializable;
import com.github.timmyovo.pixeluitweaks.common.gui.GuiContainer;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MouseInputModel implements ISerializable {
    @SerializedName("guiContainer")
    private GuiContainer guiContainer;
    @SerializedName("mouseX")
    private int mouseX;
    @SerializedName("mouseY")
    private int mouseY;
    @SerializedName("mouseButton")
    private int mouseButton;
}
