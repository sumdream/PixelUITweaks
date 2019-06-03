package com.github.timmyovo.pixeluitweaks.server.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallbackConfiguration {
    private Map<UUID, CommandEntry> guiContextCallback;
}
