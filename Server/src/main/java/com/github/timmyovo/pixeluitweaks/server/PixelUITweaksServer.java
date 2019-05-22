package com.github.timmyovo.pixeluitweaks.server;

import com.github.skystardust.ultracore.bukkit.commands.MainCommandSpec;
import com.github.timmyovo.pixeluitweaks.common.api.IComp;
import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentButton;
import com.github.timmyovo.pixeluitweaks.common.render.texture.impl.LocalTextureBinder;
import com.github.timmyovo.pixeluitweaks.server.packet.PacketManager;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public final class PixelUITweaksServer extends JavaPlugin {
    private static PixelUITweaksServer PIXEL_UI_TWEAKS_SERVER;
    private List<IComp> modules = new ArrayList<>();

    public static PixelUITweaksServer getPixelUiTweaksServer() {
        return PIXEL_UI_TWEAKS_SERVER;
    }

    public static <T> T getModule(Class<T> clazz) {
        return clazz.cast(getPixelUiTweaksServer().modules.stream()
                .filter(iComp -> iComp.getClass() == clazz)
                .findAny().orElse(null));
    }

    @Override
    public void onEnable() {
        PIXEL_UI_TWEAKS_SERVER = this;
        this.modules.add(new PacketManager().init());
        MainCommandSpec.newBuilder()
                .addAlias("ui")
                .withCommandSpecExecutor((commandSender, strings) -> {
                    PacketManager module = getModule(PacketManager.class);
                    module.openScreen(((Player) commandSender), ComponentContainer.builder()
                            .width(256)
                            .height(256)
                            .componentList(Arrays.asList(ComponentButton.newBuilder()
                                    .withHeight(16)
                                    .withWidth(48)
                                    .withDisplayString("TestButton1")
                                    .withVisible(true)
                                    .withXPos(10)
                                    .withYPos(10)
                                    .build()))
                            .textureBinder(LocalTextureBinder.newBuilder()
                                    .withTexturePath("textures/gui/widgets.png")
                                    .build())
                            .build());
                    return true;
                })
                .build()
                .register();
    }

}
