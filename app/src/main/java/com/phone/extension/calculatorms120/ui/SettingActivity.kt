package com.phone.extension.calculatorms120.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import com.phone.extension.calculatorms120.R
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        ln_function.setOnClickListener {  }
        ln_calculator.setOnClickListener {  }
        ln_history.setOnClickListener {  }

        ck_sound.setOnCheckedChangeListener { buttonView, isChecked ->

        }

        ck_vibrate.setOnCheckedChangeListener { buttonView, isChecked ->

        }

    }
}
