package com.github.timmyovo.pixeluitweaks.common.gui.hover;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ContentHover {
    @SerializedName(value = "hoverTexts")
    private List<String> hoverTexts;
}
