package com.lmt.bytedancecsj.models;


import org.jetbrains.annotations.NotNull;

import kotlin.jvm.internal.Intrinsics;

public final class PluginAdConfig {
    @NotNull
    private String appId;
    @NotNull
    private String appName;
    private boolean debug;

    @NotNull
    public final String getAppId() {
        return this.appId;
    }

    public final void setAppId(@NotNull String var1) {
        Intrinsics.checkNotNullParameter(var1, "<set-?>");
        this.appId = var1;
    }

    @NotNull
    public final String getAppName() {
        return this.appName;
    }

    public final void setAppName(@NotNull String var1) {
        Intrinsics.checkNotNullParameter(var1, "<set-?>");
        this.appName = var1;
    }

    public final boolean getDebug() {
        return this.debug;
    }

    public final void setDebug(boolean var1) {
        this.debug = var1;
    }

    public PluginAdConfig(@NotNull String appId, @NotNull String appName, boolean debug) {
        super();
        Intrinsics.checkNotNullParameter(appId, "appId");
        Intrinsics.checkNotNullParameter(appName, "appName");
        this.appId = appId;
        this.appName = appName;
        this.debug = debug;
    }
}
