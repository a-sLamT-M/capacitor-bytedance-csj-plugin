package com.lmt.bytedancecsj;

import android.content.Context;
import android.content.Intent;
import androidx.activity.result.ActivityResult;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.lmt.bytedancecsj.activity.SplashAdActivity;
import com.lmt.bytedancecsj.models.PluginAdConfig;
import com.lmt.bytedancecsj.services.BrowserService;
import com.lmt.bytedancecsj.services.TTAdService;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

// gosh i hate my life
@CapacitorPlugin(name = "BytedanceAd")
public class BytedanceAdPlugin extends Plugin {
    @PluginMethod
    public final void init(@NotNull PluginCall call) {
        Intrinsics.checkNotNullParameter(call, "call");
        String appId = call.getString("appId");
        String appName = call.getString("appName");
        Boolean debug = call.getBoolean("debug");
        Intrinsics.checkNotNull(appId);
        Intrinsics.checkNotNull(appName);
        Intrinsics.checkNotNull(debug);
        PluginAdConfig config = new PluginAdConfig(appId, appName, debug);
        BrowserService.setCapacitorPlugin(this);
        TTAdService.INSTANCE.init(this.getContext(), config);
        call.resolve();
    }

    @PluginMethod
    public final void showSplashAd(@NotNull PluginCall call) {
        Intrinsics.checkNotNullParameter(call, "call");
        String slotId = call.getString("slotId");
        Intent intent = new Intent();
        intent.setClass((Context)this.getActivity(), SplashAdActivity.class);
        intent.putExtra("slotId", slotId);
        this.startActivityForResult(call, intent, "splashResult");
    }

    @ActivityCallback
    private final void splashResult(PluginCall call, ActivityResult result) {
        JSObject res = new JSObject();
        res.put("result_code", result.getResultCode());
        if (result.getResultCode() == -1) {
            res.put("status", true);
            res.put("message", "nope");
        } else {
            res.put("status", false);
            Intent var10002 = result.getData();
            res.put("message", var10002 != null ? var10002.getStringExtra("message") : null);
        }

        call.resolve(res);
    }

    public final void eventEmitter(@NotNull String event, @NotNull JSObject ret) {
        Intrinsics.checkNotNullParameter(event, "event");
        Intrinsics.checkNotNullParameter(ret, "ret");
        this.notifyListeners(event, ret);
    }
}
