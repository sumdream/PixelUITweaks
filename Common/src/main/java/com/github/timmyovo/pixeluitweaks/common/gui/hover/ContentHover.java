package com.github.timmyovo.pixeluitweaks.common.gui.hover;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class ContentHover {
    @SerializedName(value = "hoverTexts")
    private List<String> hoverTexts;
}
