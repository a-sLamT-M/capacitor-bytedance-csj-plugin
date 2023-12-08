package com.lmt.bytedancecsj;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Browser;

import androidx.activity.result.ActivityResult;

import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.lmt.bytedancecsj.activity.SplashAdActivity;
import com.lmt.bytedancecsj.models.EventMessage;
import com.lmt.bytedancecsj.models.PluginAdConfig;
import com.lmt.bytedancecsj.services.BrowserService;
import com.lmt.bytedancecsj.services.TTAdService;

import kotlin.jvm.internal.Intrinsics;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

// gosh i hate my life
@CapacitorPlugin(name = "BytedanceAd")
public class BytedanceAdPlugin extends Plugin
{
    @PluginMethod
    public final void init(@NotNull PluginCall call)
    {
        Intrinsics.checkNotNullParameter(call, "call");
        var appId = call.getString("appId");
        var appName = call.getString("appName");
        var debug = call.getBoolean("debug");
        Intrinsics.checkNotNull(appId);
        Intrinsics.checkNotNull(appName);
        Intrinsics.checkNotNull(debug);
        PluginAdConfig config = new PluginAdConfig(appId, appName, debug);
        BrowserService.setCapacitorPlugin(this);
        TTAdService.INSTANCE.init(this.getContext(), config);
        call.resolve();
    }

    @PluginMethod
    public final void showSplashAd(@NotNull PluginCall call)
    {
        Intrinsics.checkNotNullParameter(call, "call");
        var slotId = call.getString("slotId");
        var intent = new Intent();
        intent.setClass((Context) this.getActivity(), SplashAdActivity.class);
        intent.putExtra("slotId", slotId);
        this.startActivityForResult(call, intent, "splashResult");
    }

    @PluginMethod
    public final void showRewardVideoAd(@NotNull PluginCall call)
    {
        var slotId = call.getString("slotId");
        var activity = this.getActivity();
        var mediaExtra = call.getObject("extra");
        // convert json to string
        var extra = mediaExtra != null ? mediaExtra.toString() : null;
        try
        {
            TTAdService.INSTANCE.showRewardAd(slotId, extra, activity, new TTAdNative.RewardVideoAdListener()
            {

                @Override
                public void onError(int i, String s)
                {
                    BrowserService.INSTANCE.eventEmitter(Constants.RewardAdEventName, new EventMessage("error",
                        new JSObject()
                            .put("code", i)
                            .put("message", s)
                        ));
                }

                @Override
                public void onRewardVideoAdLoad(TTRewardVideoAd ttRewardVideoAd)
                {
                    BrowserService.INSTANCE.eventEmitter(Constants.RewardAdEventName, new EventMessage("load", null));
                }

                // Deprecated shit
                @Override
                @Deprecated
                public void onRewardVideoCached()
                {}

                @Override
                public void onRewardVideoCached(TTRewardVideoAd ttRewardVideoAd)
                {
                    BrowserService.INSTANCE.eventEmitter(Constants.RewardAdEventName, new EventMessage("cache", null));
                    ttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener()
                    {
                        @Override
                        public void onAdShow()
                        {
                            BrowserService.INSTANCE.eventEmitter(Constants.RewardAdEventName, new EventMessage("show", null));
                        }

                        @Override
                        public void onAdVideoBarClick()
                        {
                            BrowserService.INSTANCE.eventEmitter(Constants.RewardAdEventName, new EventMessage("video_bar_click", null));
                        }

                        @Override
                        public void onAdClose()
                        {
                            BrowserService.INSTANCE.eventEmitter(Constants.RewardAdEventName, new EventMessage("close", null));
                        }

                        @Override
                        public void onVideoComplete()
                        {
                            BrowserService.INSTANCE.eventEmitter(Constants.RewardAdEventName, new EventMessage("video_complete", null));
                        }

                        @Override
                        public void onVideoError()
                        {
                            BrowserService.INSTANCE.eventEmitter(Constants.RewardAdEventName, new EventMessage("video_error", null));
                        }

                        // Deprecated
                        @Deprecated
                        @Override
                        public void onRewardVerify(boolean b, int i, String s, int i1, String s1)
                        {}

                        @Override
                        public void onRewardArrived(boolean b, int i, Bundle bundle)
                        {
                            BrowserService.INSTANCE.eventEmitter(Constants.RewardAdEventName, new EventMessage("reward_arrived", null));
                        }

                        @Override
                        public void onSkippedVideo()
                        {
                            BrowserService.INSTANCE.eventEmitter(Constants.RewardAdEventName, new EventMessage("skipped_video", null));
                        }
                    });
                    ttRewardVideoAd.showRewardVideoAd(activity);
                }
            });
        }
        catch (Throwable e)
        {
            BrowserService.INSTANCE.eventEmitter(Constants.RewardAdEventName, new EventMessage(Objects.requireNonNull(e.getMessage()), null));
        }
    }

    @ActivityCallback
    private final void splashResult(PluginCall call, ActivityResult result)
    {
        var res = new JSObject();
        res.put("result_code", result.getResultCode());
        if (result.getResultCode() == -1)
        {
            res.put("status", true);
            res.put("message", "nope");
        }
        else
        {
            res.put("status", false);
            Intent var10002 = result.getData();
            res.put("message", var10002 != null ? var10002.getStringExtra("message") : null);
        }

        call.resolve(res);
    }

    public final void eventEmitter(@NotNull String event, @NotNull JSObject ret)
    {
        Intrinsics.checkNotNullParameter(event, "event");
        Intrinsics.checkNotNullParameter(ret, "ret");
        this.notifyListeners(event, ret);
    }
}
