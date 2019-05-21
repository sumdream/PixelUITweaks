package com.github.timmyovo.pixeluitweaks.common.api;

import com.google.gson.Gson;

public interface ISerializable {
    default String asString() {
        return new Gson().toJson(this);
    }
}
