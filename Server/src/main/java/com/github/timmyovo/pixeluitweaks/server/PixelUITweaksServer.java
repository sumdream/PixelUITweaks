package com.github.timmyovo.pixeluitweaks.server;

import com.github.skystardust.ultracore.bukkit.commands.MainCommandSpec;
import com.github.skystardust.ultracore.core.PluginInstance;
import com.github.skystardust.ultracore.core.configuration.ConfigurationManager;
import com.github.skystardust.ultracore.core.exceptions.ConfigurationException;
import com.github.skystardust.ultracore.core.utils.FileUtils;
import com.github.timmyovo.pixeluitweaks.common.api.IComp;
import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import com.github.timmyovo.pixeluitweaks.common.message.GuiFactory;
import com.github.timmyovo.pixeluitweaks.server.config.*;
import com.github.timmyovo.pixeluitweaks.server.listener.EventListener;
import com.github.timmyovo.pixeluitweaks.server.manager.CallbackManager;
import com.github.timmyovo.pixeluitweaks.server.manager.PlayerStateManager;
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
    public static final String CHANNEL = "UT|EVENT";
    private static PixelUITweaksServer PIXEL_UI_TWEAKS_SERVER;
    private List<IComp> modules = new ArrayList<>();
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


    @Override
    public void onEnable() {
        PIXEL_UI_TWEAKS_SERVER = this;
        this.modules.add(new PacketManager().init());
        this.modules.add(new CallbackManager().init());
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

        try {
            FileSystem fs = FileSystems.getDefault();
            WatchService ws = fs.newWatchService();
            Path pTemp = Paths.get(getDataFolder().getPath());
            pTemp.register(ws, new WatchEvent.Kind[]{ENTRY_MODIFY, ENTRY_CREATE, ENTRY_DELETE}, FILE_TREE);
            Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
                try {
                    WatchKey k = ws.take();
                    if (!k.pollEvents().isEmpty()) {
                        configurationManager.reloadFiles();
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            Optional<List<ComponentContainer>> playerCurrentContainers = PlayerStateManager.getPlayerCurrentContainers(onlinePlayer);
                            if (playerCurrentContainers.isPresent()) {
                                if (guiConfiguration == null) {
                                    configurationManager.reloadFiles();
                                }
                                List<GuiConfiguration.GuiEntry> guiEntryList = guiConfiguration.getGuiEntryList();
                                GuiConfiguration.GuiEntry guiEntry = guiEntryList.get(0);
                                getModule(PacketManager.class).openScreen(onlinePlayer, guiEntry.getGuiLayoutBase());
                            }
                        }
                    }
                    k.reset();
                } catch (InterruptedException | ConfigurationException e) {
                    e.printStackTrace();
                }
            }, 0L, 10L);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void initConfigurations() {
        this.configurationManager = new ConfigurationManager(this);
        configurationManager.registerConfiguration("guiConfiguration", () -> new GuiConfiguration(Collections.singletonList(new GuiConfiguration.GuiEntry("example", true, Examples.exampleContainer()))));
        configurationManager.registerConfiguration("callbackConfiguration", () -> new CallbackConfiguration(ImmutableMap.of(UUID.randomUUID(), new CommandEntry(Collections.singletonList(new CommandEntry.SingleCommandEntry("exampleCommand", true, true))))));
        configurationManager.registerConfiguration("textureConfiguration", () -> new TextureConfiguration(Collections.singletonList("test.png")));
        configurationManager.registerConfiguration("listContentConfiguration", () -> ListContentConfiguration.builder()
                .sidebarList(Collections.singletonList(Examples.exampleSidebar()))
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
