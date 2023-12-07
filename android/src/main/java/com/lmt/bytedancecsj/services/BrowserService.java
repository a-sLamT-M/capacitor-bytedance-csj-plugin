package com.lmt.bytedancecsj.services;

import android.content.Context;
import android.graphics.Insets;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;

import com.getcapacitor.JSObject;
import com.lmt.bytedancecsj.BytedanceAdPlugin;
import com.lmt.bytedancecsj.models.EventMessage;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.internal.Intrinsics;

public class BrowserService {
    private static BytedanceAdPlugin capacitorPlugin;
    @NotNull
    public static final BrowserService INSTANCE;

    public static void setCapacitorPlugin(BytedanceAdPlugin plugin)
    {
        if(capacitorPlugin != null) {
            return;
        }
        capacitorPlugin = plugin;
    }

    public final void eventEmitter(@NotNull String event, @NotNull EventMessage ret) {
        Intrinsics.checkNotNullParameter(event, "event");
        Intrinsics.checkNotNullParameter(ret, "ret");
        JSObject data = new JSObject();
        data.put("status", ret.getMessage());
        data.put("data", ret.getData());
        BytedanceAdPlugin var10000 = capacitorPlugin;
        if (var10000 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("capacitorPlugin");
        }

        var10000.eventEmitter("myPluginEvent", data);
    }

    @NotNull
    public final Point getScreenSize() {
        new DisplayMetrics();
        BytedanceAdPlugin var10000 = capacitorPlugin;
        if (var10000 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("capacitorPlugin");
        }

        Object var14 = var10000.getActivity().getSystemService(Context.WINDOW_SERVICE);
        if (var14 == null) {
            throw new NullPointerException("null cannot be cast to non-null type android.view.WindowManager");
        } else {
            WindowManager wm = (WindowManager)var14;
            int width;
            int height;

            if (Build.VERSION.SDK_INT >= 30) {
                WindowMetrics var15 = wm.getCurrentWindowMetrics();
                Intrinsics.checkNotNullExpressionValue(var15, "wm.currentWindowMetrics");
                WindowMetrics windowMetrics = var15;
                WindowInsets var17 = windowMetrics.getWindowInsets();
                Intrinsics.checkNotNullExpressionValue(var17, "windowMetrics.windowInsets");
                WindowInsets windowInsets = var17;
                Insets var18 = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars() | WindowInsets.Type.displayCutout());
                Intrinsics.checkNotNullExpressionValue(var18, "windowInsets.getInsetsIg…ets.Type.displayCutout())");
                Insets insets = var18;
                int insetsWidth = insets.right + insets.left;
                int insetsHeight = insets.top + insets.bottom;
                Rect var19 = windowMetrics.getBounds();
                Intrinsics.checkNotNullExpressionValue(var19, "windowMetrics.bounds");
                Rect b = var19;
                width = b.width() - insetsWidth;
                height = b.height() - insetsHeight;
            } else {
                Point size = new Point();
                Display display = wm.getDefaultDisplay();
                if (display != null) {
                    display.getSize(size);
                }

                width = size.x;
                height = size.y;
            }

            return new Point(width, height);
        }
    }

    private BrowserService() {
    }

    static {
        BrowserService var0 = new BrowserService();
        INSTANCE = var0;
    }
}
