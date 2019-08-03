package com.github.timmyovo.pixeluitweaks.server;

import com.github.skystardust.ultracore.bukkit.commands.MainCommandSpec;
import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import com.github.timmyovo.pixeluitweaks.common.gui.InGameOverlays;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentButton;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentCheckBox;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentSlot;
import com.github.timmyovo.pixeluitweaks.common.gui.hover.ContentHover;
import com.github.timmyovo.pixeluitweaks.common.gui.sidebar.Sidebar;
import com.github.timmyovo.pixeluitweaks.common.gui.sidebar.SidebarType;
import com.github.timmyovo.pixeluitweaks.common.render.RenderMethod;
import com.github.timmyovo.pixeluitweaks.common.render.texture.impl.DynamicNetworkTextureBinder;
import com.github.timmyovo.pixeluitweaks.common.render.texture.impl.WebTextureBinder;
import com.github.timmyovo.pixeluitweaks.server.manager.CallbackManager;
import com.github.timmyovo.pixeluitweaks.server.packet.PacketManager;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static com.github.timmyovo.pixeluitweaks.server.PixelUITweaksServer.getModule;

public class Examples {
    public static void exampleOverlay(Player player) {
        PacketManager packetManager = getModule(PacketManager.class);
        packetManager.setPlayerOverlay(player, InGameOverlays.builder()
                .textureBinder(WebTextureBinder.newBuilder()
                        .withUrl("https://i.loli.net/.png")
                        .build())
                .renderMethod(RenderMethod.builder()
                        .entryList(Arrays.asList(
                                RenderMethod.RenderEntry.builder()
                                        .xOffset("w / 2")
                                        .yOffset("h / 2")
                                        .scaledHeight("316")
                                        .scaledWidth("234")
                                        .textureHeight("316")
                                        .textureWidth("234")
                                        .textureX(0)
                                        .textureY(0)
                                        .build()
                        ))
                        .build())
                .build());
    }

    public static void openUICommand() {
        MainCommandSpec.newBuilder()
                .addAlias("ui")
                .withCommandSpecExecutor((commandSender, strings) -> {
                    PacketManager module = getModule(PacketManager.class);
                    try {
                        module.sendTextureToPlayer(((Player) commandSender), "test.png", ImageIO.read(new File("./test.png")));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    module.sendListContent(((Player) commandSender), exampleSidebar());
                    module.openContainerScreen(((Player) commandSender), exampleContainer());
                    return true;
                })
                .build()
                .register();

    }

    public static ComponentContainer exampleContainer() {
        ComponentButton testButton1 = ComponentButton.newBuilder()
                .withHeight("16")
                .withWidth("48")
                .withDisplayString("TestButton1")
                .withVisible(true)
                .withXPos("10")
                .withYPos("10")
                .build();
        getModule(CallbackManager.class).registerComponentCallback(testButton1, componentEventModel -> {
            System.out.println("a8105 sb!");
        });
        return ComponentContainer.builder()
                .width(256)
                .height(256)
                .componentList(Arrays.asList(testButton1,
                        ComponentCheckBox.newBuilder()
                                .withTextureBinder(WebTextureBinder.newBuilder()
                                        .withUrl("https://www.baidu.com")
                                        .build())
                                .withVisible(true)
                                .withBoxWidth(16)
                                .withXPos("64")
                                .withYPos("64")
                                .withHeight("16")
                                .withWidth("16")
                                .withDisplayString("TestCheckBox")
                                .withContentHover(new ContentHover(Arrays.asList("TestLine1", "TestLine2")))
                                .build(),
                        ComponentSlot.newBuilder()
                                .withVisible(true)
                                .withSlotX(16)
                                .withSlotY(16)
                                .withHeight("16")
                                .withWidth("16")
                                .withSlotIndex(37)
                                .build()
                ))
                .textureBinder(DynamicNetworkTextureBinder.newBuilder()
                        .withNetworkTextureName("test.png")
                        .build())
                .renderMethod(RenderMethod.builder()
                        .entryList(Collections.singletonList(RenderMethod.RenderEntry.builder()
                                .xOffset(" w / 2 - 120 ")
                                .yOffset("h / 2 + 30")
                                .scaledHeight("220")
                                .scaledWidth("175")
                                .textureHeight("256")
                                .textureWidth("256")
                                .textureX(0)
                                .textureY(0)
                                .build()))
                        .build())
                .build();
    }

    public static Sidebar exampleSidebar() {
        return Sidebar.builder()
                .sidebarType(SidebarType.ADD)
                .name("sss")
                .strings(Arrays.asList("str1", "str2", "adsaasdjsabjdhsf"))
                .xOffset("-(w /2)")
                .yOffset("- (h / 8)")
                .build();
    }
}
