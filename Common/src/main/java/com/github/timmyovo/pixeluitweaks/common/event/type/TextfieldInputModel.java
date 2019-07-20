package com.github.timmyovo.pixeluitweaks.common.event.type;

import com.github.timmyovo.pixeluitweaks.common.event.models.ComponentEventModel;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentTextField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TextfieldInputModel implements ComponentEventModel<ComponentTextField, String> {
    private ComponentTextField componentTextField;
    private String inputText;

    @Override
    public ComponentTextField getComponentModel() {
        return getComponentTextField();
    }

    @Override
    public Optional<String> getExtraData() {
        return Optional.of(inputText);
    }
}
