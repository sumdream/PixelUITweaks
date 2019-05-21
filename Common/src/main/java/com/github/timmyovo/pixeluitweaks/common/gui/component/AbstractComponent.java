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
public class AbstractComponent {
    @SerializedName("componentId")
    private UUID componentId;
    @SerializedName("type")
    private String type;
    @SerializedName("xPos")
    private int xPos;
    @SerializedName("yPos")
    private int yPos;
    @SerializedName("height")
    private int height;
    @SerializedName("width")
    private int width;
    @SerializedName("viable")
    private boolean viable;
    @SerializedName("renderMethod")
    private RenderMethod renderMethod;
    @SerializedName("textureBinder")
    private TextureBinder textureBinder;
    @SerializedName("contentHover")
    private ContentHover contentHover;
}
