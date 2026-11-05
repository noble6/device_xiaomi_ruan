/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.settings;

import android.content.ContentResolver;
import android.content.Context;
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

    private static boolean sRestored;

    private KernelManagerRestore() {}

    static void run(Context context) {
        ContentResolver resolver = context.getApplicationContext().getContentResolver();
        SystemProperties.addChangeCallback(() -> restoreIfReady(resolver));
        restoreIfReady(resolver);
    }

    private static synchronized void restoreIfReady(ContentResolver resolver) {
        if (sRestored || !SystemProperties.getBoolean(PROP_POST_BOOT_DONE, false)) {
            return;
        }
        sRestored = true;
        for (String key : KEYS) {
            // The manager writes its default when a setting is unset, which isn't walt.
            if (Settings.Secure.getString(resolver, key) != null) {
                resolver.notifyChange(Settings.Secure.getUriFor(key), null);
            }
        }
    }
}
