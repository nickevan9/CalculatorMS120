package com.phone.extension.calculatorms120.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.phone.extension.calculatorms120.R
import com.phone.extension.calculatorms120.extensions.config
import com.phone.extension.calculatorms120.helpers.*
import com.simplemobiletools.commons.extensions.copyToClipboard
import com.simplemobiletools.commons.extensions.performHapticFeedback
import com.simplemobiletools.commons.extensions.value
import kotlinx.android.synthetic.main.activity_main.*
import me.grantland.widget.AutofitHelper

class MainActivity : AppCompatActivity(), Calculator {

    private var vibrateOnButtonPress = true

    lateinit var calc: CalculatorImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calc = CalculatorImpl(this, applicationContext)

        btn_add_0.setOnClickListener { calc.addZero() }

        btn_plus.setOnClickListener { calc.handleOperation(PLUS); checkHaptic(it) }
        btn_minus.setOnClickListener { calc.handleOperation(MINUS); checkHaptic(it) }
        btn_multiply.setOnClickListener { calc.handleOperation(MULTIPLY); checkHaptic(it) }
        btn_divide.setOnClickListener { calc.handleOperation(DIVIDE); checkHaptic(it) }
        btn_percent.setOnClickListener { calc.handleOperation(PERCENT); checkHaptic(it) }
        btn_root.setOnClickListener { calc.handleOperation(ROOT); checkHaptic(it) }
        btn_percent.setOnLongClickListener { calc.setSet();true }

        btn_mrc.setOnLongClickListener { calc.resetMemory();true }
        btn_mrc.setOnClickListener { calc.memoryResult() }

        btn_m_add.setOnClickListener { calc.setMemory("M+") }
        btn_m_sub.setOnClickListener { calc.setMemory("M-") }

        btn_clear.setOnClickListener { calc.handleClear(); checkHaptic(it) }

        btn_ac.setOnClickListener { calc.handleReset() }

        btn_cost.setOnClickListener { calc.setCost() }

        btn_sell.setOnClickListener { calc.setSell() }

        btn_mar.setOnClickListener { }

        btn_tax_add.setOnClickListener { calc.taxAdd() }
        btn_tax_sub.setOnClickListener { calc.taxSub() }

        getButtonIds().forEach {
            it.setOnClickListener { calc.numpadClicked(it.id); checkHaptic(it) }
        }

        btn_equals.setOnClickListener { calc.handleEquals(); checkHaptic(it) }
        tv_set.setOnLongClickListener { copyToClipboard(false) }
        result.setOnLongClickListener { copyToClipboard(true) }

        btn_menu.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        AutofitHelper.create(result)
        AutofitHelper.create(tv_set)
    }

    override fun onResume() {
        super.onResume()

        if (config.preventPhoneFromSleeping) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

        vibrateOnButtonPress = config.vibrateOnButtonPress
    }

    override fun onPause() {
        super.onPause()
        if (config.preventPhoneFromSleeping) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }


    private fun checkHaptic(view: View) {
        if (vibrateOnButtonPress) {
            view.performHapticFeedback()
        }
    }


    private fun getButtonIds() =
        arrayOf(btn_decimal, btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9)

    private fun copyToClipboard(copyResult: Boolean): Boolean {
        var value = tv_set.value
        if (copyResult) {
            value = result.value
        }

        return if (value.isEmpty()) {
            false
        } else {
            copyToClipboard(value)
            true
        }
    }

    override fun setValue(value: String, context: Context) {
        result.text = value
    }


    // used only by Robolectric
    override fun setValueDouble(d: Double) {
        calc.setValue(Formatter.doubleToString(d))
        calc.lastKey = DIGIT
    }

    override fun setFormula(value: String, context: Context) {
        tv_formular.text = value
    }

    override fun setVisibilityGT(visibility: Boolean) {
        if (visibility) {
            tv_gt.visibility = View.VISIBLE
            tv_gt.text = "GT"
        } else {
            tv_gt.visibility = View.INVISIBLE
        }
    }

    override fun setVisibilityMemory(visibility: Boolean, value: String) {
        if (visibility) {
            tv_memory.visibility = View.VISIBLE
            tv_memory.text = value
        } else {
            tv_memory.visibility = View.INVISIBLE
        }
    }


    override fun setVisibilityTAX(visibility: Boolean, value: String) {
        if (visibility) {
            tv_tax.visibility = View.VISIBLE
            tv_tax.text = value
        } else {
            tv_tax.visibility = View.INVISIBLE
        }
    }

    override fun setVisibilityMargin(visibility: Boolean, value: String) {
        if (visibility) {
            tv_margin.visibility = View.VISIBLE
            tv_margin.text = value
        } else {
            tv_margin.visibility = View.INVISIBLE
        }
    }

    override fun setVisibilitySET(visibility: Boolean) {
        if (visibility) {
            tv_set.visibility = View.VISIBLE
            tv_set.text = "SET"
        } else {
            tv_set.visibility = View.INVISIBLE
        }
    }
}
