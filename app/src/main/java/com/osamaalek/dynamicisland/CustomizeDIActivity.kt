package com.osamaalek.dynamicisland

import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.view.Display
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import org.greenrobot.eventbus.EventBus

class CustomizeDIActivity : AppCompatActivity() {

    val eventBus: EventBus = EventBus.getDefault()
    lateinit var editor: Editor
    lateinit var seekBarY: SeekBar
    lateinit var seekBarX: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customize_diactivity)

        seekBarY = findViewById(R.id.seekBar_y)
        seekBarX = findViewById(R.id.seekBar_x)

        editor = getSharedPreferences(Constants.MY_DATA, MODE_PRIVATE).edit()

        val display: Display = windowManager.defaultDisplay

        initY(display.height)

        initX(display.width)
    }

    private fun initX(width: Int) {
        seekBarX.max = width / 2
        seekBarX.min = -width / 2
        seekBarX.progress = 0
        seekBarX.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                eventBus.post(EventPositionChanged(seekBarY.progress, progress))
                editor.putInt(Constants.X_KEY, progress).apply()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }

    private fun initY(height: Int) {
        seekBarY.min = -height / 2 - 100
        seekBarY.max = height / 2
        seekBarY.progress = 0
        seekBarY.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                eventBus.post(EventPositionChanged(progress, seekBarX.progress))
                editor.putInt(Constants.Y_KEY, progress).apply()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }

    override fun onDestroy() {
        eventBus.unregister(this)
        super.onDestroy()
    }
}
