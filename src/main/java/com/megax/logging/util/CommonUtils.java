package com.megax.logging.util;

import java.util.concurrent.TimeUnit;

public class CommonUtils {
    public static String getCountdownTimerFormat(long milliseconds) {
        String timer = String.format("%02d:%02d:%02d.%03d", TimeUnit.MILLISECONDS.toHours(milliseconds),
                TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(milliseconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(milliseconds)),
                TimeUnit.MILLISECONDS.toMillis(milliseconds) - TimeUnit.SECONDS.toMillis(
                        TimeUnit.MILLISECONDS.toSeconds(milliseconds)));
        return timer;
    }
}
