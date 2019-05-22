package com.github.timmyovo.pixeluitweaks.common.gui.component;

import com.github.timmyovo.pixeluitweaks.common.gui.hover.ContentHover;
import com.github.timmyovo.pixeluitweaks.common.render.RenderMethod;
import com.github.timmyovo.pixeluitweaks.common.render.texture.TextureBinder;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public abstract class AbstractComponent {
    @SerializedName("componentId")
    protected UUID componentId;
    @SerializedName("xPos")
    protected int xPos;
    @SerializedName("yPos")
    protected int yPos;
    @SerializedName("height")
    protected int height;
    @SerializedName("width")
    protected int width;
    @SerializedName("visible")
    protected boolean visible;
    @SerializedName("renderMethod")
    protected RenderMethod renderMethod;
    @SerializedName("textureBinder")
    protected TextureBinder textureBinder;
    @SerializedName("contentHover")
    protected ContentHover contentHover;
}
