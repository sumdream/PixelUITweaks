package com.github.timmyovo.pixeluitweaks.common.gui;

import com.github.timmyovo.pixeluitweaks.common.render.RenderMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InGameOverlays {
    private List<RenderMethod> renderMethodList;


}
