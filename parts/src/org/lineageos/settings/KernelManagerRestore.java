/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.settings;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemProperties;
import android.provider.Settings;

/**
 * Ax Kernel Manager restores its settings when system_server starts, but Xiaomi's post_boot
 * runs at boot completed and sets the governor to walt and the minimum frequencies to their
 * lowest. Once it's done (init.ruan.axion.rc), notify the manager's observers so that it
 * applies them again.
 */
final class KernelManagerRestore {

    private static final String PROP_POST_BOOT_DONE = "sys.ruan.post_boot_done";

    // Keep in sync with configs/kernel/ax_kernel_manager.xml.
    private static final String[] KEYS = {
        "little_cpu_governor",
        "big_cpu_governor",
        "axion_min_freq",
        "axion_min_freq_big",
    };

    private static final long POLL_INTERVAL_MS = 1000;
    private static final int MAX_POLLS = 180;

    private KernelManagerRestore() {}

    static void run(Context context) {
        // SystemProperties.addChangeCallback() doesn't report properties set by init, so poll.
        poll(context.getApplicationContext().getContentResolver(),
                new Handler(Looper.getMainLooper()), MAX_POLLS);
    }

    private static void poll(ContentResolver resolver, Handler handler, int pollsLeft) {
        if (SystemProperties.getBoolean(PROP_POST_BOOT_DONE, false)) {
            restore(resolver);
        } else if (pollsLeft > 0) {
            handler.postDelayed(() -> poll(resolver, handler, pollsLeft - 1), POLL_INTERVAL_MS);
        }
    }

    private static void restore(ContentResolver resolver) {
        for (String key : KEYS) {
            // The manager writes its default when a setting is unset, which isn't walt.
            if (Settings.Secure.getString(resolver, key) != null) {
                resolver.notifyChange(Settings.Secure.getUriFor(key), null);
            }
        }
    }
}
