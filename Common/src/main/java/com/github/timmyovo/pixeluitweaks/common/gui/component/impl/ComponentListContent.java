package com.github.timmyovo.pixeluitweaks.common.gui.component.impl;

import com.github.timmyovo.pixeluitweaks.common.gui.component.AbstractComponent;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ComponentListContent extends AbstractComponent {
    @SerializedName("slotHeight")
    public int slotHeight;
    @SerializedName("top")
    public int top;
    @SerializedName("bottom")
    public int bottom;
    @SerializedName("right")
    public int right;
    @SerializedName("left")
    public int left;
    @SerializedName("headerPadding")
    public int headerPadding;
    @SerializedName("scrollbarX")
    private int scrollbarX;
    @SerializedName("overlayY")
    private int overlayY;
    @SerializedName("contents")
    private List<AbstractComponent> contents;
}
