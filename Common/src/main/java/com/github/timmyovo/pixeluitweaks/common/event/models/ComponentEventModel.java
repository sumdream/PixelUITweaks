package com.github.timmyovo.pixeluitweaks.common.event.models;

import com.github.timmyovo.pixeluitweaks.common.gui.component.AbstractComponent;

public interface ComponentEventModel<T extends AbstractComponent, ExtraData> extends EventModel<ExtraData> {
    T getComponentModel();

}
