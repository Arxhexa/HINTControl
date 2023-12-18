@file:JvmName("ThemeUtilsJVM")

package dev.zwander.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.jthemedetecor.OsThemeDetector
import com.sun.jna.platform.win32.Advapi32Util
import com.sun.jna.platform.win32.WinReg
import dev.zwander.common.monet.ColorScheme
import dev.zwander.common.util.UserDefaults
import korlibs.memory.Platform
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs

@Composable
actual fun getThemeInfo(): ThemeInfo {
    val (osThemeDetector, isSupported) = remember {
        OsThemeDetector.getDetector() to OsThemeDetector.isSupported()
    }

    val genericDetector = remember {
        GenericLinuxThemeDetector()
    }

    var dark by remember {
        mutableStateOf(
            when {
                Platform.isLinux -> genericDetector.isDark
                isSupported -> osThemeDetector.isDark
                else -> true
            }
        )
    }

    DisposableEffect(osThemeDetector, isSupported, genericDetector) {
        val listener = { darkMode: Boolean ->
            dark = darkMode
        }

        if (Platform.isLinux) {
            genericDetector.registerListener(listener)
        } else if (isSupported) {
            osThemeDetector.registerListener(listener)
        }

        onDispose {
            if (Platform.isLinux) {
                genericDetector.removeListener(listener)
            } else if (isSupported) {
                osThemeDetector.removeListener(listener)
            }
        }
    }

    val accentColor = remember {
        when (hostOs) {
            OS.Windows -> {
                java.awt.Color(
                    Advapi32Util.registryGetIntValue(
                        WinReg.HKEY_CURRENT_USER,
                        "Software\\Microsoft\\Windows\\DWM",
                        "AccentColor",
                    )
                ).rgb
            }
            OS.MacOS -> {
                UserDefaults.standardUserDefaults().getAccentColor().toArgb()
            }
            else -> {
                Color(red = 208, green = 188, blue = 255).toArgb()
            }
        }
    }

    return ThemeInfo(
        isDarkMode = dark,
        colors = ColorScheme(
            accentColor,
            dark
        ).toComposeColorScheme().toNullableColorScheme(),
    )
}
