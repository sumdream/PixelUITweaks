package com.github.timmyovo.pixeluitweaks.common.event.type;

import com.github.timmyovo.pixeluitweaks.common.event.models.ComponentEventModel;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentButton;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ButtonClickModel implements ComponentEventModel<ComponentButton, Object> {
    private ComponentButton componentButton;

    @Override
    public ComponentButton getComponentModel() {
        return componentButton;
    }

    @Override
    public Optional<Object> getExtraData() {
        return Optional.empty();
    }
}
