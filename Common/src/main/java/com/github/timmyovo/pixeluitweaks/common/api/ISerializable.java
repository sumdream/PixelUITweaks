package com.github.timmyovo.pixeluitweaks.common.api;

import com.github.timmyovo.pixeluitweaks.common.message.GuiFactory;

public interface ISerializable {
    default String asString() {
        return GuiFactory.GSON.toJson(this);
    }
}
