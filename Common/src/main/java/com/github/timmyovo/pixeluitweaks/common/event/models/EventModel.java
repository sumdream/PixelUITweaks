package com.github.timmyovo.pixeluitweaks.common.event.models;

import com.github.timmyovo.pixeluitweaks.common.api.ISerializable;

import java.util.Optional;

public interface EventModel<ExtraData> extends ISerializable {
    Optional<ExtraData> getExtraData();
}
