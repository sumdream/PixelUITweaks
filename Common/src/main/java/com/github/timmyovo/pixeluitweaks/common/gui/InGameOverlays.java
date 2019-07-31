package com.github.timmyovo.pixeluitweaks.common.gui;

import com.github.timmyovo.pixeluitweaks.common.render.RenderMethod;
import com.github.timmyovo.pixeluitweaks.common.render.texture.TextureBinder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InGameOverlays {
    private TextureBinder textureBinder;
    private RenderMethod renderMethod;
}
