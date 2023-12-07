package com.lmt.bytedancecsj.models;

import com.getcapacitor.JSObject;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public final class EventMessage {
    @NotNull
    private final String message;
    @Nullable
    private final JSObject data;

    @NotNull
    public final String getMessage() {
        return this.message;
    }

    @Nullable
    public final JSObject getData() {
        return this.data;
    }

    public EventMessage(@NotNull String message, @Nullable JSObject data) {
        super();
        Intrinsics.checkNotNullParameter(message, "message");
        this.message = message;
        this.data = data;
    }

    // $FF: synthetic method
    public EventMessage(String var1, JSObject var2, int var3, DefaultConstructorMarker var4) {
        this(var1, var2);

        if ((var3 & 2) != 0) {
            var2 = null;
        }
    }
}
