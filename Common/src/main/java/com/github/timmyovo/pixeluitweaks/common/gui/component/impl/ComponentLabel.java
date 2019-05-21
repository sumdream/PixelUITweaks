package com.github.timmyovo.pixeluitweaks.common.gui.component.impl;

import com.github.timmyovo.pixeluitweaks.common.gui.component.AbstractComponent;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ComponentLabel extends AbstractComponent {
    @SerializedName("centered")
    protected boolean centered;
    @SerializedName("visible")
    protected boolean visible;
    @SerializedName("textColor")
    protected int textColor;
    @SerializedName("border")
    private int border;
    @SerializedName("labels")
    private List<String> labels;
}
