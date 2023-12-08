package com.lmt.bytedancecsj.services;

import android.content.Context;
import android.graphics.Point;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdLoadType;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.lmt.bytedancecsj.models.EventMessage;
import com.lmt.bytedancecsj.models.PluginAdConfig;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;

public final class TTAdService
{
    private static boolean initialized;
    @NotNull
    public static final TTAdService INSTANCE;

    @Nullable
    public final TTAdManager getAdManager() throws Throwable
    {
        if (!initialized)
        {
            throw (Throwable) (new RuntimeException("Please initialize this plugin first!"));
        }
        else
        {
            return TTAdSdk.getAdManager();
        }
    }

    public final void init(@Nullable Context context, @NotNull PluginAdConfig config)
    {
        Intrinsics.checkNotNullParameter(config, "config");
        if (!initialized)
        {
            initialized = true;
        }

        TTAdSdk.init(context, (new TTAdConfig.Builder())
            .appId(config.getAppId())
            .useTextureView(true)
            .appName(config.getAppName())
            .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
            .allowShowNotify(true)
            .debug(config.getDebug())
            .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI, TTAdConstant.NETWORK_STATE_3G)
            .supportMultiProcess(false)
            .build());
        // start
        TTAdSdk.start(new TTAdSdk.Callback()
        {
            @Override
            public void success()
            {
                BrowserService.INSTANCE.eventEmitter("init", new EventMessage("success", null));
            }

            @Override
            public void fail(int i, String s)
            {
                BrowserService.INSTANCE.eventEmitter("init", new EventMessage(s, null));
            }
        });
    }

    public final void showSplashAd(String slotId, Point size, Context context, TTAdNative.CSJSplashAdListener eventListener, int timeout) throws Throwable
    {
        var adNative = getAdManager().createAdNative(context);
        var slot = new AdSlot.Builder()
            .setCodeId(slotId)
            .setImageAcceptedSize(size.x, size.y)
            .setSupportDeepLink(true)
            .build();
        adNative.loadSplashAd(slot, eventListener, timeout);
    }

    public final void showRewardAd(String slotId, String jsonMessage, Context context, TTAdNative.RewardVideoAdListener eventListener) throws Throwable
    {
        var adNative = getAdManager().createAdNative(context);
        var slot = new AdSlot.Builder().setCodeId(slotId).setAdLoadType(TTAdLoadType.LOAD).setMediaExtra(jsonMessage).build();
        adNative.loadRewardVideoAd(slot, eventListener);
    }

    private TTAdService()
    {
    }

    static
    {
        TTAdService var0 = new TTAdService();
        INSTANCE = var0;
    }
}
