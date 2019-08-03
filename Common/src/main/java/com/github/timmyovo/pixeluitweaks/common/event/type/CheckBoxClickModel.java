package com.github.timmyovo.pixeluitweaks.common.event.type;

import com.github.timmyovo.pixeluitweaks.common.event.models.ComponentEventModel;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentCheckBox;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckBoxClickModel implements ComponentEventModel<ComponentCheckBox, Boolean> {
    private ComponentCheckBox componentCheckBox;
    private boolean isChecked;


    @Override
    public ComponentCheckBox getComponentModel() {
        return componentCheckBox;
    }

    @Override
    public Optional<Boolean> getExtraData() {
        return Optional.of(isChecked);
    }
}
