package com.github.timmyovo.pixeluitweaks.common.gui.component.impl;

import com.github.timmyovo.pixeluitweaks.common.gui.component.AbstractComponent;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComponentTextField extends AbstractComponent {
    @SerializedName("text")
    protected String text;
    @SerializedName("maxStringLength")
    protected int maxStringLength;
    @SerializedName("enableBackgroundDrawing")
    protected boolean enableBackgroundDrawing;
    @SerializedName("canLoseFocus")
    protected boolean canLoseFocus;

}
