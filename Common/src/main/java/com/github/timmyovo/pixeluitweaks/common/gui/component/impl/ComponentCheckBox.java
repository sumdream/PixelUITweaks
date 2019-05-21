package com.github.timmyovo.pixeluitweaks.common.gui.component.impl;

import com.github.timmyovo.pixeluitweaks.common.gui.component.AbstractComponent;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComponentCheckBox extends AbstractComponent {
    @SerializedName("boxWidth")
    protected int boxWidth;
    @SerializedName("displayString")
    protected String displayString;
}
