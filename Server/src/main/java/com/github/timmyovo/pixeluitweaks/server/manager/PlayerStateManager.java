package com.github.timmyovo.pixeluitweaks.server.manager;

import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentTextField;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerStateManager {
    private static List<InputRecorder> PLAYER_INPUT_RECORD_LIST = Lists.newArrayList();
    private static Map<UUID, List<ComponentContainer>> PLAYER_CURRENT_GUI_SCREEN = Maps.newConcurrentMap();

    public static void notifyInputTextChanged(Player player, ComponentTextField componentTextField, String inputText){
        try {
            InputRecorder inputRecorder = getInputRecorder(player).orElseThrow(IllegalAccessException::new);
            inputRecorder.addRecordEntry(InputRecorder.InputRecordEntry.builder()
                    .currentText(inputText)
                    .componentTextField(componentTextField)
                    .build());
        } catch (IllegalAccessException e) {
            ArrayList<InputRecorder.InputRecordEntry> objects = new ArrayList<>();
            objects.add(InputRecorder.InputRecordEntry.builder()
                    .componentTextField(componentTextField)
                    .currentText(inputText)
                    .build());
            PLAYER_INPUT_RECORD_LIST.add(InputRecorder.builder()
                    .player(player.getUniqueId())
                    .inputRecordEntryList(objects)
                    .build());
        }
    }

    public static void notifyPlayerGuiOpen(Player player,List<ComponentContainer> componentContainer){
        PlayerStateManager.PLAYER_CURRENT_GUI_SCREEN.put(player.getUniqueId(),componentContainer);
    }

    public static void notifyPlayerClose(Player player,List<ComponentContainer> componentContainer){
        PlayerStateManager.PLAYER_CURRENT_GUI_SCREEN.remove(player.getUniqueId());
    }

    public static Optional<InputRecorder> getInputRecorder(Player player) {
        return getInputRecorder(player.getUniqueId());
    }

    public static Optional<InputRecorder> getInputRecorder(UUID uuid){
        return PLAYER_INPUT_RECORD_LIST.stream()
                .filter(u->u.getPlayer().equals(uuid))
                .findAny();
    }

    public static Optional<List<ComponentContainer>> getPlayerCurrentContainers(UUID uuid){
        return Optional.ofNullable(PLAYER_CURRENT_GUI_SCREEN.get(uuid));
    }

    public static Optional<List<ComponentContainer>> getPlayerCurrentContainers(Player player){
        return getPlayerCurrentContainers(player.getUniqueId());
    }

    public static boolean isPlayerOpenedScreen(Player player){
        return getPlayerCurrentContainers(player.getUniqueId()).isPresent();
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class InputRecorder {
        private UUID player;
        private List<InputRecordEntry> inputRecordEntryList;

        public InputRecorder(UUID uuid) {
            this.player = uuid;
            this.inputRecordEntryList = Lists.newArrayList();
        }

        public Player asBukkitPlayer(){
            return Bukkit.getPlayer(player);
        }

        public void addRecordEntry(InputRecordEntry inputRecordEntry){
            if (inputRecordEntryList.stream()
                    .filter(inputRecordEntry1 -> inputRecordEntry1.getComponentTextField().equals(inputRecordEntry.getComponentTextField()))
                    .peek(inputRecordEntry1 -> inputRecordEntry1.setCurrentText(inputRecordEntry.getCurrentText()))
                    .count() == 0) {
                this.inputRecordEntryList.add(inputRecordEntry);
            }

        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class InputRecordEntry{
            private String currentText;
            private ComponentTextField componentTextField;
        }
    }
}
