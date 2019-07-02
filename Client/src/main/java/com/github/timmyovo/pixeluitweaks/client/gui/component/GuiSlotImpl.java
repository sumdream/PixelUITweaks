package com.github.timmyovo.pixeluitweaks.client.gui.component;

import com.github.timmyovo.pixeluitweaks.client.gui.ClientComponent;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentSlot;
import com.github.timmyovo.pixeluitweaks.common.gui.hover.ContentHover;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.Gui;

@Getter
@Setter
public class GuiSlotImpl extends Gui implements ClientComponent<ComponentSlot> {
    private ComponentSlot componentSlot;

    @Override
    public ComponentSlot getComponentModel() {
        return componentSlot;
    }

    @Override
    public void updateComponent(ComponentSlot componentModel) {

    }
}
