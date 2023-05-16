package dev.zwander.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.github.weisj.darklaf.LafManager
import com.github.weisj.darklaf.theme.spec.ColorToneRule
import dev.zwander.common.monet.ColorScheme

@Composable
actual fun getThemeInfo(): ThemeInfo {
    val manager = LafManager.getPreferredThemeStyle()
    val dark = manager.colorToneRule == ColorToneRule.DARK

    return ThemeInfo(
        isDarkMode = dark,
        colors = ColorScheme(
            manager.accentColorRule?.accentColor?.rgb
                ?: Color(red = 208, green = 188, blue = 255).toArgb(),
            dark
        ).toComposeColorScheme().toNullableColorScheme(),
    )
}
