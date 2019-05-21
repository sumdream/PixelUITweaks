package com.github.timmyovo.pixeluitweaks.common.gui;

import com.github.timmyovo.pixeluitweaks.common.gui.component.AbstractComponent;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GuiContainer {
    @SerializedName("height")
    private int height;
    @SerializedName("width")
    private int width;
    @SerializedName("componentList")
    private List<AbstractComponent> componentList;
}
