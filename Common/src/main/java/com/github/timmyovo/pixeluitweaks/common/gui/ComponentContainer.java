package com.github.timmyovo.pixeluitweaks.common.gui;

import com.github.timmyovo.pixeluitweaks.common.gui.component.AbstractComponent;
import com.github.timmyovo.pixeluitweaks.common.render.RenderMethod;
import com.github.timmyovo.pixeluitweaks.common.render.texture.TextureBinder;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ComponentContainer {
    @SerializedName("height")
    private int height;
    @SerializedName("width")
    private int width;
    @SerializedName("textureBinder")
    private TextureBinder textureBinder;
    @SerializedName("renderMethod")
    private RenderMethod renderMethod;
    @SerializedName("componentList")
    private List<AbstractComponent> componentList;
}
