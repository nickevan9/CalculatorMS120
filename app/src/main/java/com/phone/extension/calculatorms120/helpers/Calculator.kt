package com.phone.extension.calculatorms120.helpers

import android.content.Context

interface Calculator {
    fun setValue(value: String, context: Context)

    fun setValueDouble(d: Double)

    fun setFormula(value: String, context: Context)

    fun setVisibilityGT(visibility: Boolean)

    fun setVisibilityMemory(visibility: Boolean, value: String)

    fun setVisibilityTAX(visibility: Boolean, value: String)

    fun setVisibilityMargin(visibility: Boolean, value: String)

    fun setVisibilitySET(visibility: Boolean)

    fun setCalculatorString(value: String)


}
