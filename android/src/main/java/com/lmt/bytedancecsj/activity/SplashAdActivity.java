package com.lmt.bytedancecsj.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bytedance.sdk.openadsdk.CSJAdError;
import com.bytedance.sdk.openadsdk.CSJSplashAd;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.getcapacitor.JSObject;
import com.lmt.bytedancecsj.Constants;
import com.lmt.bytedancecsj.R;
import com.lmt.bytedancecsj.models.EventMessage;
import com.lmt.bytedancecsj.services.BrowserService;
import com.lmt.bytedancecsj.services.TTAdService;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import kotlin.jvm.internal.Intrinsics;

public final class SplashAdActivity extends Activity
{
    private FrameLayout mSplashContainer;
    private final int timeOut = 10000;
    private boolean forceDestroy;
    private String slotId;

    protected final void setErrorMessage(@NotNull String message)
    {
        Intrinsics.checkNotNullParameter(message, "message");
        Intent var10000 = this.getIntent().putExtra("success", false).putExtra("message", message);
        Intrinsics.checkNotNullExpressionValue(var10000, "intent.putExtra(\"success…Extra(\"message\", message)");
        Intent intent = var10000;
        this.setResult(-1, intent);
    }

    protected final void setSuccessMessage(@Nullable String message)
    {
        Intent var10000 = this.getIntent().putExtra("success", true);
        String var10002 = message;
        if (message == null)
        {
            var10002 = "nope";
        }

        var10000 = var10000.putExtra("message", var10002);
        Intrinsics.checkNotNullExpressionValue(var10000, "intent.putExtra(\"success…sage\", message ?: \"nope\")");
        Intent intent = var10000;
        this.setResult(-1, intent);
    }

    protected final void sendMessage(@NotNull String message, @NotNull JSObject obj)
    {
        Intrinsics.checkNotNullParameter(message, "message");
        Intrinsics.checkNotNullParameter(obj, "obj");
        BrowserService.INSTANCE.eventEmitter(Constants.SplashAdEventName, new EventMessage(message, obj));
    }

    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // get layout id of bytedanceunionad_activity_splash
        int layoutId = R.layout.activity_splash;
        this.setContentView(layoutId);
        // get container id of bytedanceunionad_splash_container
        int id = R.id.splash_container;
        View var10001 = this.findViewById(id);
        if (var10001 == null)
        {
            throw new NullPointerException("null cannot be cast to non-null type android.widget.FrameLayout");
        }
        else
        {
            this.mSplashContainer = (FrameLayout) var10001;
            TTAdManager var2 = null;
            try
            {
                var2 = TTAdService.INSTANCE.getAdManager();
            }
            catch (Throwable e)
            {
                setErrorMessage(Objects.requireNonNull(e.getMessage()));
            }
            Intrinsics.checkNotNull(var2);
            this.setSuccessMessage((String) null);
            this.getExtraInfo();
            this.loadSplashAd();
        }
    }

    private final void getExtraInfo()
    {
        Intent intent = this.getIntent();
        if (intent != null)
        {
            this.slotId = Objects.requireNonNull(intent.getStringExtra("slotId")).trim();
            if (!TextUtils.isEmpty((CharSequence) this.slotId))
            {
                return;
            }
        }

        this.finish();
    }

    private final void loadSplashAd()
    {
        try
        {
            BrowserService var10000 = BrowserService.INSTANCE;
            Intrinsics.checkNotNullExpressionValue(var10000, "BrowserService.INSTANCE");
            Point size = var10000.getScreenSize();
            TTAdService.INSTANCE.showSplashAd(slotId, size, this, (new TTAdNative.CSJSplashAdListener()
            {
                public void onSplashLoadSuccess(@NotNull CSJSplashAd ad)
                {
                    Intrinsics.checkNotNullParameter(ad, "ad");
                    BrowserService.INSTANCE.eventEmitter(Constants.SplashAdEventName, new EventMessage("loaded", (JSObject) null));
                }

                public void onSplashLoadFail(@NotNull CSJAdError error)
                {
                    Intrinsics.checkNotNullParameter(error, "error");
                    SplashAdActivity var10000 = SplashAdActivity.this;
                    String var10001 = error.getMsg();
                    Intrinsics.checkNotNullExpressionValue(var10001, "error.msg");
                    String errCode = String.valueOf(error.getCode());
                    var10000.setErrorMessage(var10001 + "_code: " + errCode);
                    SplashAdActivity.this.finish();
                }

                public void onSplashRenderSuccess(@NotNull CSJSplashAd ad)
                {
                    Intrinsics.checkNotNullParameter(ad, "ad");
                    if (SplashAdActivity.this.mSplashContainer != null && !SplashAdActivity.this.isFinishing())
                    {
                        FrameLayout var10000 = SplashAdActivity.this.mSplashContainer;
                        Intrinsics.checkNotNull(var10000);
                        var10000.removeAllViews();
                        ad.showSplashView((ViewGroup) SplashAdActivity.this.mSplashContainer);
                        ad.setSplashAdListener((CSJSplashAd.SplashAdListener) (new CSJSplashAd.SplashAdListener()
                        {
                            public void onSplashAdShow(@Nullable CSJSplashAd p0)
                            {
                                BrowserService.INSTANCE.eventEmitter(Constants.SplashAdEventName, new EventMessage("show", (JSObject) null));
                            }

                            public void onSplashAdClick(@Nullable CSJSplashAd p0)
                            {
                                BrowserService.INSTANCE.eventEmitter(Constants.SplashAdEventName, new EventMessage("click", (JSObject) null));
                            }

                            public void onSplashAdClose(@Nullable CSJSplashAd p0, int p1)
                            {
                                BrowserService.INSTANCE.eventEmitter(Constants.SplashAdEventName, new EventMessage("closing", (JSObject) null));
                                SplashAdActivity.this.finish();
                            }
                        }));
                    }
                    else
                    {
                        SplashAdActivity.this.setErrorMessage("SplashContainer is NULL!");
                        SplashAdActivity.this.finish();
                    }
                }

                public void onSplashRenderFail(@NotNull CSJSplashAd ad, @NotNull CSJAdError csjAdError)
                {
                    Intrinsics.checkNotNullParameter(ad, "ad");
                    Intrinsics.checkNotNullParameter(csjAdError, "csjAdError");
                    SplashAdActivity var10000 = SplashAdActivity.this;
                    String var10001 = csjAdError.getMsg();
                    Intrinsics.checkNotNullExpressionValue(var10001, "csjAdError.msg");
                    var10000.setErrorMessage(var10001);
                    SplashAdActivity.this.finish();
                }
            }), this.timeOut);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            this.setErrorMessage(Objects.requireNonNull(e.getMessage()));
            this.finish();
        }
    }

    protected void onResume()
    {
        if (this.forceDestroy)
        {
            this.finish();
        }

        super.onResume();
    }

    protected void onStop()
    {
        super.onStop();
        this.forceDestroy = true;
    }

    protected void onDestroy()
    {
        FrameLayout var10000 = this.mSplashContainer;
        Intrinsics.checkNotNull(var10000);
        var10000.removeAllViews();
        super.onDestroy();
    }

    // $FF: synthetic method
    public static final void access$setMSplashContainer$p(SplashAdActivity $this, FrameLayout var1)
    {
        $this.mSplashContainer = var1;
    }
}
