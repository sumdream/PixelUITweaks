package com.github.timmyovo.pixeluitweaks.server;

import com.github.skystardust.ultracore.bukkit.commands.MainCommandSpec;
import com.github.skystardust.ultracore.core.PluginInstance;
import com.github.skystardust.ultracore.core.configuration.ConfigurationManager;
import com.github.skystardust.ultracore.core.exceptions.ConfigurationException;
import com.github.skystardust.ultracore.core.utils.FileUtils;
import com.github.timmyovo.pixeluitweaks.common.api.IComp;
import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentButton;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentCheckBox;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentSlot;
import com.github.timmyovo.pixeluitweaks.common.gui.hover.ContentHover;
import com.github.timmyovo.pixeluitweaks.common.gui.sidebar.Sidebar;
import com.github.timmyovo.pixeluitweaks.common.gui.sidebar.SidebarType;
import com.github.timmyovo.pixeluitweaks.common.message.GuiFactory;
import com.github.timmyovo.pixeluitweaks.common.render.RenderMethod;
import com.github.timmyovo.pixeluitweaks.common.render.texture.impl.DynamicNetworkTextureBinder;
import com.github.timmyovo.pixeluitweaks.common.render.texture.impl.WebTextureBinder;
import com.github.timmyovo.pixeluitweaks.server.config.*;
import com.github.timmyovo.pixeluitweaks.server.listener.EventListener;
import com.github.timmyovo.pixeluitweaks.server.packet.PacketManager;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Logger;

import static com.sun.nio.file.ExtendedWatchEventModifier.FILE_TREE;
import static java.nio.file.StandardWatchEventKinds.*;

@Getter
public final class PixelUITweaksServer extends JavaPlugin implements PluginInstance {
    private static PixelUITweaksServer PIXEL_UI_TWEAKS_SERVER;
    private List<IComp> modules = new ArrayList<>();
    public static final String CHANNEL = "UT|EVENT";
    private ConfigurationManager configurationManager;

    private GuiConfiguration guiConfiguration;
    private CallbackConfiguration callbackConfiguration;
    private TextureConfiguration textureConfiguration;
    private ListContentConfiguration listContentConfiguration;

    public static PixelUITweaksServer getPixelUiTweaksServer() {
        return PIXEL_UI_TWEAKS_SERVER;
    }

    public static <T> T getModule(Class<T> clazz) {
        return clazz.cast(getPixelUiTweaksServer().modules.stream()
                .filter(iComp -> iComp.getClass() == clazz)
                .findAny().orElse(null));
    }

    private ComponentContainer exampleContainer() {
        return ComponentContainer.builder()
                .width(256)
                .height(256)
                .componentList(Arrays.asList(ComponentButton.newBuilder()
                                .withHeight("16")
                                .withWidth("48")
                                .withDisplayString("TestButton1")
                                .withVisible(true)
                                .withXPos("10")
                                .withYPos("10")
                                .build(),
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
                                .textureHeight(256)
                                .textureWidth(256)
                                .textureX(0)
                                .textureY(0)
                                .build()))
                        .build())
                .build();
    }

    private Sidebar exampleSidebar() {
        return Sidebar.builder()
                .sidebarType(SidebarType.ADD)
                .name("sss")
                .strings(Arrays.asList("str1", "str2", "adsaasdjsabjdhsf"))
                .xOffset("-(w /2)")
                .yOffset("- (h / 8)")
                .build();
    }

    @Override
    public void onEnable() {
        PIXEL_UI_TWEAKS_SERVER = this;
        this.modules.add(new PacketManager().init());
        try {
            Field field = FileUtils.class.getField("GSON");
            field.setAccessible(true);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, GuiFactory.GSON);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        initConfigurations();
        readTexture();
        uiCommandInit();
        readSidebar();
        Bukkit.getMessenger().registerIncomingPluginChannel(this, CHANNEL, new EventListener());
        openUICommand();
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            try {
                FileSystem fs = FileSystems.getDefault();
                WatchService ws = fs.newWatchService();
                Path pTemp = Paths.get(getDataFolder().getPath());
                pTemp.register(ws, new WatchEvent.Kind[]{ENTRY_MODIFY, ENTRY_CREATE, ENTRY_DELETE}, FILE_TREE);
                WatchKey k = ws.take();
                List<WatchEvent<?>> watchEvents = k.pollEvents();

                if (watchEvents.stream()
                        .anyMatch(watchEvent -> watchEvent.kind().equals(ENTRY_MODIFY))) {
                    configurationManager.reloadFiles();
                    return;
                }
//                for (WatchEvent<?> e : watchEvents)
//                {
//                    Object c = e.context();
//                    getLogger().info("File: " + c + " Reloaded.");
//                }
                k.reset();
            } catch (IOException | InterruptedException | ConfigurationException e) {
                e.printStackTrace();
            }
        }, 0L, 10L);
    }

    private void openUICommand() {
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

    private void initConfigurations() {

        this.configurationManager = new ConfigurationManager(this);
        configurationManager.registerConfiguration("guiConfiguration", () -> new GuiConfiguration(Collections.singletonList(new GuiConfiguration.GuiEntry("example", true, exampleContainer()))));
        configurationManager.registerConfiguration("callbackConfiguration", () -> new CallbackConfiguration(ImmutableMap.of(UUID.randomUUID(), new CommandEntry(Collections.singletonList(new CommandEntry.SingleCommandEntry("exampleCommand", true, true))))));
        configurationManager.registerConfiguration("textureConfiguration", () -> new TextureConfiguration(Collections.singletonList("test.png")));
        configurationManager.registerConfiguration("listContentConfiguration", () -> ListContentConfiguration.builder()
                .sidebarList(Collections.singletonList(exampleSidebar()))
                .build());
        configurationManager.init(getClass(), this).start();
    }

    private void readSidebar() {
        MainCommandSpec.newBuilder()
                .addAlias("sidebar")
                .addAlias("usb")
                .withCommandSpecExecutor((commandSender, strings) -> {
                    if (strings.length < 1) {
                        return true;
                    }
                    Player player = null;
                    if (strings.length == 1) {
                        if (commandSender instanceof Player) {
                            player = ((Player) commandSender);
                        } else {
                            return false;
                        }
                    }
                    if (strings.length >= 2) {
                        player = Bukkit.getPlayer(strings[1]);
                    }
                    if (player == null) {
                        commandSender.sendMessage("CAN NOT FIND PLAYER");
                        return false;
                    }
                    Player finalPlayer = player;
                    getListContentConfiguration().getSidebarList()
                            .stream()
                            .filter(sidebar -> sidebar.getName().equalsIgnoreCase(strings[0]))
                            .forEach(sidebar -> {
                                PacketManager module = getModule(PacketManager.class);
                                module.sendListContent(finalPlayer, sidebar);
                            });
                    return true;
                })
                .build()
                .register();
    }

    private void uiCommandInit() {
        MainCommandSpec.newBuilder()
                .addAlias("ui")
                .withCommandSpecExecutor((commandSender, strings) -> {
                    if (strings.length < 1) {
                        return true;
                    }
                    Player player = null;
                    if (strings.length == 1) {
                        if (commandSender instanceof Player) {
                            player = ((Player) commandSender);
                        } else {
                            return false;
                        }
                    }
                    if (strings.length >= 2) {
                        player = Bukkit.getPlayer(strings[1]);
                    }
                    if (player == null) {
                        commandSender.sendMessage("CAN NOT FIND PLAYER");
                        return false;
                    }

                    Player finalPlayer = player;
                    getGuiConfiguration().getGuiEntryList().stream()
                            .filter(guiEntry -> guiEntry.getName().equalsIgnoreCase(strings[0]))
                            .forEach(guiEntry -> {
                                PacketManager module = getModule(PacketManager.class);
                                if (guiEntry.isSlotUI()) {
                                    module.openContainerScreen(finalPlayer, guiEntry.getGuiLayoutBase());
                                } else {
                                    module.openScreen(finalPlayer, guiEntry.getGuiLayoutBase());
                                }

                            });

                    return true;
                })
                .build()
                .register();
    }

    private void readTexture() {
        File img = new File(getDataFolder(), "img");
        if (!img.exists() || img.isFile()) {
            img.mkdirs();
        }
        getTextureConfiguration().getTextureList().forEach(texture -> {
            File input = new File(img, texture);
            if (!input.exists()) {
                getLogger().warning("Can not find image: " + input.toString());
                return;
            }
            try {
                BufferedImage read = ImageIO.read(input);
                TextureConfiguration.texturesMap.put(texture, read);
                getLogger().info("Loaded texture: " + texture + ". Texture Size: " + read.getWidth() + "*" + read.getHeight());
            } catch (IOException e) {
                getLogger().warning("Can not find image: " + input.toString());
                getLogger().warning(e.getLocalizedMessage());
            }
        });
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
                TextureConfiguration.texturesMap.forEach((name, img) -> {
                    PacketManager module = getModule(PacketManager.class);
                    module.sendTextureToPlayer(playerJoinEvent.getPlayer(), name, (BufferedImage) img);
                    getLogger().info("Sending texture " + name + " to player " + playerJoinEvent.getPlayer().getName());
                });
            }
        }, this);
    }

    @Override
    public Logger getPluginLogger() {
        return getLogger();
    }
}
