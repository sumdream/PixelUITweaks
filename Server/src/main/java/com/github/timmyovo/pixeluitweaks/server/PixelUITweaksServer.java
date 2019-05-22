package com.github.timmyovo.pixeluitweaks.server;

import com.github.skystardust.ultracore.bukkit.commands.MainCommandSpec;
import com.github.timmyovo.pixeluitweaks.common.api.IComp;
import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentButton;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentCheckBox;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentSlot;
import com.github.timmyovo.pixeluitweaks.common.gui.hover.ContentHover;
import com.github.timmyovo.pixeluitweaks.common.render.RenderMethod;
import com.github.timmyovo.pixeluitweaks.common.render.texture.impl.DynamicNetworkTextureBinder;
import com.github.timmyovo.pixeluitweaks.server.packet.PacketManager;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
                    try {
                        module.sendTextureToPlayer(((Player) commandSender), "test.png", ImageIO.read(new File("./test.png")));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    module.openContainerScreen(((Player) commandSender), ComponentContainer.builder()
                            .width(256)
                            .height(256)
                            .componentList(Arrays.asList(ComponentButton.newBuilder()
                                    .withHeight(16)
                                    .withWidth(48)
                                    .withDisplayString("TestButton1")
                                    .withVisible(true)
                                    .withXPos(10)
                                    .withYPos(10)
                                            .build(),
                                    ComponentCheckBox.newBuilder()
                                            .withVisible(true)
                                            .withBoxWidth(16)
                                            .withXPos(64)
                                            .withYPos(64)
                                            .withHeight(16)
                                            .withWidth(16)
                                            .withDisplayString("TestCheckBox")
                                            .withContentHover(new ContentHover(Arrays.asList("TestLine1", "TestLine2")))
                                            .build(),
                                    ComponentSlot.newBuilder()
                                            .withVisible(true)
                                            .withXPos(8)
                                            .withYPos(8)
                                            .withHeight(16)
                                            .withWidth(16)
                                            .withSlotIndex(100)
                                            .build()
                            ))
                            .textureBinder(DynamicNetworkTextureBinder.newBuilder()
                                    .withNetworkTextureName("test.png")
                                    .build())
                            .renderMethod(RenderMethod.builder()
                                    .entryList(Collections.singletonList(RenderMethod.RenderEntry.builder()
                                            .xOffset(88)
                                            .yOffset(139)
                                            .scaledHeight(220)
                                            .scaledWidth(175)
                                            .textureHeight(256)
                                            .textureWidth(256)
                                            .textureX(0)
                                            .textureY(0)
                                            .build()))
                                    .build())
                            .build());
                    return true;
                })
                .build()
                .register();
    }

}
