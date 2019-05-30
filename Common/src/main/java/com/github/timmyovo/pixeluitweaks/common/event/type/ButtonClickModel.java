package com.github.timmyovo.pixeluitweaks.common.event.type;

import com.github.timmyovo.pixeluitweaks.common.api.ISerializable;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentButton;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ButtonClickModel implements ISerializable {
    private ComponentButton componentButton;
}
