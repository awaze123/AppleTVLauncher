package com.awaze.appletvlauncher

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    private val apps = listOf(
        App("YouTube", "com.google.android.youtube.tv", "▶", 0xFFE53935.toInt()),
        App("Fastway Live TV", "com.fastway.fastwaylivetv", "LIVE", 0xFF2764C8.toInt()),
        App("NetMirror", "app.netmirror.newtv", "N", 0xFF7653D6.toInt()),
        App("Hotstar", "in.startv.hotstar", "★", 0xFF173F9E.toInt())
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(8, 11, 22)
        window.navigationBarColor = Color.rgb(8, 11, 22)
        buildHome()
    }

    private fun buildHome() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(28), dp(18), dp(28), dp(18))
            background = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(
                    Color.rgb(12, 22, 48),
                    Color.rgb(8, 11, 22),
                    Color.rgb(35, 19, 55)
                )
            )
        }
        setContentView(root)

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        header.addView(label("  TV", 25, Color.WHITE, true),
            LinearLayout.LayoutParams(0, -2, 1f))
        header.addView(label("YOUR ENTERTAINMENT", 12, 0xFFB5C5E7.toInt(), true))
        root.addView(header)

        root.addView(space(18))

        val hero = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(26), dp(20), dp(20), dp(20))
            background = rounded(
                Color.rgb(36, 55, 105), Color.rgb(84, 39, 104), 24
            )
        }
        hero.addView(label("WELCOME BACK", 12, 0xFFBBD3FF.toInt(), true))
        hero.addView(space(8))
        hero.addView(label("Your entertainment.\nAll in one place.", 30, Color.WHITE, true))
        hero.addView(space(8))
        hero.addView(label("Movies • Live TV • Your favourite apps", 14,
            0xFFE1E8F7.toInt(), false))
        root.addView(hero, LinearLayout.LayoutParams(-1, 0, 1.2f))

        root.addView(space(18))
        root.addView(label("TRENDING", 15, Color.WHITE, true))
        root.addView(space(10))

        val trends = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
        }
        val trendData = listOf(
            Triple("Cinematic Picks", 0xFF963A5B.toInt(), 0xFF272D66.toInt()),
            Triple("Live Entertainment", 0xFF176B7E.toInt(), 0xFF192B58.toInt()),
            Triple("Family Time", 0xFF9B7136.toInt(), 0xFF4A285C.toInt())
        )
        trendData.forEachIndexed { i, item ->
            val card = TextView(this).apply {
                text = item.first
                textSize = 14f
                setTextColor(Color.WHITE)
                typeface = Typeface.DEFAULT_BOLD
                gravity = Gravity.BOTTOM or Gravity.START
                setPadding(dp(14), dp(12), dp(8), dp(12))
                background = rounded(item.second, item.third, 18)
                isFocusable = true
            }
            trends.addView(card, LinearLayout.LayoutParams(0, dp(72), 1f).apply {
                if (i > 0) leftMargin = dp(10)
            })
        }
        root.addView(trends)

        root.addView(space(16))

        val dock = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(dp(10), dp(9), dp(10), dp(9))
            background = rounded(Color.rgb(30, 37, 60), Color.rgb(20, 26, 45), 24)
        }

        apps.forEach { app ->
            val tile = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(dp(5), dp(5), dp(5), dp(5))
                background = rounded(app.color, app.color, 15)
                isFocusable = true
                isFocusableInTouchMode = true
                setOnFocusChangeListener { v, focused ->
                    v.scaleX = if (focused) 1.10f else 1f
                    v.scaleY = if (focused) 1.10f else 1f
                }
                setOnClickListener { openApp(app) }
            }

            tile.addView(label(app.glyph, 18, Color.WHITE, true).apply {
                gravity = Gravity.CENTER
            })
            tile.addView(label(app.name, 10, Color.WHITE, true).apply {
                gravity = Gravity.CENTER
            })

            dock.addView(tile, LinearLayout.LayoutParams(0, dp(62), 1f).apply {
                if (dock.childCount > 0) leftMargin = dp(8)
            })
        }

        root.addView(dock)
        root.addView(space(8))
        root.addView(label("Use your TV remote to select an app", 10,
            0xFF9EABC9.toInt(), false).apply {
            gravity = Gravity.CENTER
        })
    }

    private fun openApp(app: App) {
        val intent = packageManager.getLeanbackLaunchIntentForPackage(app.pkg)
            ?: packageManager.getLaunchIntentForPackage(app.pkg)

        if (intent != null) {
            try {
                startActivity(intent)
            } catch (_: Exception) {
                Toast.makeText(this, "Could not open ${app.name}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "${app.name} is not installed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun label(
        value: String,
        size: Int,
        color: Int,
        bold: Boolean
    ) = TextView(this).apply {
        text = value
        textSize = size.toFloat()
        setTextColor(color)
        if (bold) typeface = Typeface.DEFAULT_BOLD
    }

    private fun space(height: Int) = View(this).apply {
        layoutParams = LinearLayout.LayoutParams(1, dp(height))
    }

    private fun rounded(c1: Int, c2: Int, radius: Int) =
        GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(c1, c2)
        ).apply {
            cornerRadius = dp(radius).toFloat()
        }

    private fun dp(value: Int) =
        (value * resources.displayMetrics.density).toInt()

    data class App(
        val name: String,
        val pkg: String,
        val glyph: String,
        val color: Int
    )
}
