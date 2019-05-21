package com.github.timmyovo.pixeluitweaks.client.hook;

import com.github.timmyovo.pixeluitweaks.common.gui.component.AbstractComponent;
import com.github.timmyovo.pixeluitweaks.common.gui.component.GuiComponents;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GuiFactory {
    private static Gson gson = new GsonBuilder().registerTypeAdapterFactory(
            RuntimeTypeAdapter.of(AbstractComponent.class, "type")
                    .registerSubtype(ComponentListContent.class, GuiComponents.LIST_CONTENT.name())
                    .registerSubtype(ComponentSlot.class, GuiComponents.SLOT.name())
                    .registerSubtype(ComponentLabel.class, GuiComponents.LABEL.name())
                    .registerSubtype(ComponentTextField.class, GuiComponents.TEXT_FIELD.name())
                    .registerSubtype(ComponentButton.class, GuiComponents.BUTTON.name())
                    .registerSubtype(ComponentCheckBox.class, GuiComponents.CHECKBOX.name())
                    .registerSubtype(ComponentPicture.class, GuiComponents.PICTURE.name())
    )
            .create();

    public static <T> T fromString(String string, Class<T> clazz) {
        return gson.fromJson(string, clazz);
    }

}
