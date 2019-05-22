package com.github.timmyovo.pixeluitweaks.common.message;

import com.github.timmyovo.pixeluitweaks.common.gui.component.AbstractComponent;
import com.github.timmyovo.pixeluitweaks.common.gui.component.GuiComponents;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.*;
import com.github.timmyovo.pixeluitweaks.common.render.texture.TextureBinder;
import com.github.timmyovo.pixeluitweaks.common.render.texture.TextureBinderType;
import com.github.timmyovo.pixeluitweaks.common.render.texture.impl.DynamicNetworkTextureBinder;
import com.github.timmyovo.pixeluitweaks.common.render.texture.impl.LocalTextureBinder;
import com.github.timmyovo.pixeluitweaks.common.render.texture.impl.WebTextureBinder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GuiFactory {
    public static Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapterFactory(
            RuntimeTypeAdapter.of(AbstractComponent.class, "type")
                    .registerSubtype(ComponentListContent.class, GuiComponents.LIST_CONTENT.name())
                    .registerSubtype(ComponentSlot.class, GuiComponents.SLOT.name())
                    .registerSubtype(ComponentLabel.class, GuiComponents.LABEL.name())
                    .registerSubtype(ComponentTextField.class, GuiComponents.TEXT_FIELD.name())
                    .registerSubtype(ComponentButton.class, GuiComponents.BUTTON.name())
                    .registerSubtype(ComponentCheckBox.class, GuiComponents.CHECKBOX.name())
                    .registerSubtype(ComponentPicture.class, GuiComponents.PICTURE.name())
    )
            .registerTypeAdapterFactory(
                    RuntimeTypeAdapter.of(TextureBinder.class, "type")
                            .registerSubtype(DynamicNetworkTextureBinder.class, TextureBinderType.DYNAMIC_NETWORK.name())
                            .registerSubtype(LocalTextureBinder.class, TextureBinderType.LOCAL.name())
                            .registerSubtype(WebTextureBinder.class, TextureBinderType.WEB.name())

            )
            .create();

    public static <T> T fromString(String string, Class<T> clazz) {
        return GSON.fromJson(string, clazz);
    }

}