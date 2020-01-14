package com.phone.extension.calculatorms120.helpers

import android.content.Context
import com.phone.extension.calculatorms120.R
import com.phone.extension.calculatorms120.operation.OperationFactory
import kotlin.text.StringBuilder

class CalculatorImpl(calculator: Calculator, private val context: Context) {
    private var displayedNumber: String? = null
    private var displayedFormula: String? = null
    var lastKey: String? = null
    private var mLastOperation: String? = null
    private var mCallback: Calculator? = calculator

    private var mIsFirstOperation = false
    private var mResetValue = false
    private var mWasPercentLast = false
    private var mBaseValue = 0.0
    private var mSecondValue = 0.0

    private var mMemoryValue = 0.0
    private var mMemoryState = false

    private var mCostValue = 0.0
    private var mSellValue = 0.0
    private var mMarValue = 0.0

    private var mSetState = false
    private var mSetValue = 0.0

    private var mResultTax = 0.0

    init {

        resetValues()
        setValue("0")
        setFormula("")
    }

    private fun resetValueIfNeeded() {
        if (mResetValue)
            displayedNumber = "0"

        mResetValue = false
    }

    private fun resetValues() {
        mBaseValue = 0.0
        mSecondValue = 0.0
        mCostValue = 0.0
        mSellValue = 0.0
        mMarValue = 0.0
        mResetValue = false
        mLastOperation = ""
        displayedNumber = ""
        displayedFormula = ""
        mIsFirstOperation = true
        lastKey = ""
        mSetState = false
    }

    fun setValue(value: String) {
        mCallback!!.setValue(value, context)
        displayedNumber = value
    }

    private fun setFormula(value: String) {
        mCallback!!.setFormula(value, context)
        displayedFormula = value
    }

    private fun updateFormula() {
        val first = Formatter.doubleToString(mBaseValue)
        val second = Formatter.doubleToString(mSecondValue)
        val sign = getSign(mLastOperation)

        when {
            sign == "√" -> {
                setFormula(sign + first)
            }
            sign == "!" -> {
                setFormula(first + sign)
            }
            sign.isNotEmpty() -> {
                var formula = first + sign + second
                if (mWasPercentLast) {
                    formula += "%"
                }
                setFormula(formula)
            }
        }
    }

    private fun addDigit(number: Int) {
        val currentValue = displayedNumber
        val newValue = formatString(currentValue!! + number)
        mCallback!!.setVisibilityGT(false)

        setValue(newValue)
    }

    private fun formatString(str: String): String {
        // if the number contains a decimal, do not try removing the leading zero anymore, nor add group separator
        // it would prevent writing values like 1.02
        if (str.contains(".")) {
            return str
        }

        val doubleValue = Formatter.stringToDouble(str)
        return Formatter.doubleToString(doubleValue)
    }

    private fun updateResult(value: Double) {
        setValue(Formatter.doubleToString(value))
        mBaseValue = value
    }

    private fun getDisplayedNumberAsDouble() = Formatter.stringToDouble(displayedNumber!!)

    private fun handleResult() {
        mSecondValue = getDisplayedNumberAsDouble()
        calculateResult()
        mBaseValue = getDisplayedNumberAsDouble()
    }

    private fun handleRoot() {
        mBaseValue = getDisplayedNumberAsDouble()
        calculateResult()
    }

    private fun handleFactorial() {
        mBaseValue = getDisplayedNumberAsDouble()
        calculateResult()
    }

    private fun calculateResult() {
        updateFormula()
        if (mWasPercentLast) {
            mSecondValue *= mBaseValue / 100
        }

        val operation = OperationFactory.forId(mLastOperation!!, mBaseValue, mSecondValue)
        if (operation != null) {
            val first = Formatter.doubleToString(mBaseValue)
            val oper = getSign(mLastOperation)
            val second = Formatter.doubleToString(mSecondValue)
            val result = Formatter.doubleToString(operation.getResult())
            updateResult(operation.getResult())
            val valueResult =
                StringBuilder().append("$first ").append("$oper ").append(second).append(" = ").append(result).toString()
            mCallback!!.setCalculatorString(valueResult)
            mCallback!!.setVisibilityGT(true)

        }


        mIsFirstOperation = false
        mResetValue = true

    }

    fun handleOperation(operation: String) {
        mWasPercentLast = operation == PERCENT
        if (lastKey == DIGIT && operation != ROOT && operation != FACTORIAL) {
            handleResult()
        }

        mResetValue = true
        lastKey = operation
        mLastOperation = operation

        setFormula(getSign(operation))

        if (operation == ROOT) {
            handleRoot()
            mResetValue = false
        }
        if (operation == FACTORIAL) {
            handleFactorial()
            mResetValue = false
        }

        if (mSetState) {
            if (operation == PERCENT) {
                mSetValue = displayedNumber?.toDoubleOrNull()!!
            }
        }
    }


    fun handleClear() {
        if (displayedNumber.equals(NAN)) {
            handleReset()
        } else {
            val oldValue = displayedNumber
            var newValue = "0"
            val len = oldValue!!.length
            var minLen = 1
            if (oldValue.contains("-"))
                minLen++

            if (len > minLen) {
                newValue = oldValue.substring(0, len - 1)
            }

            newValue = newValue.replace("\\.$".toRegex(), "")
            newValue = formatString(newValue)
            setValue(newValue)
            mBaseValue = Formatter.stringToDouble(newValue)
        }
    }

    fun handleReset() {
        resetValues()
        setValue("0")
        setFormula("")
        hiddenAllState()
    }

    fun handleEquals() {
        if (lastKey == EQUALS)
            calculateResult()

        if (lastKey != DIGIT)
            return

        mSecondValue = getDisplayedNumberAsDouble()
        calculateResult()

        lastKey = EQUALS
    }

    private fun decimalClicked() {
        var value = displayedNumber
        if (!value!!.contains(".")) {
            value += "."
        }
        setValue(value)
    }

    private fun zeroClicked() {
        val value = displayedNumber
        if (value != "0") {
            addDigit(0)
        }
    }

    private fun getSign(lastOperation: String?) = when (lastOperation) {
        PLUS -> "+"
        MINUS -> "-"
        MULTIPLY -> "×"
        DIVIDE -> "÷"
        PERCENT -> "%"
        POWER -> "^"
        ROOT -> "√"
        FACTORIAL -> "!"
        else -> ""
    }

    fun numpadClicked(id: Int) {
        if (lastKey == EQUALS) {
            mLastOperation = EQUALS
        }

        lastKey = DIGIT
        resetValueIfNeeded()

        when (id) {
            R.id.btn_decimal -> decimalClicked()
            R.id.btn_0 -> zeroClicked()
            R.id.btn_1 -> addDigit(1)
            R.id.btn_2 -> addDigit(2)
            R.id.btn_3 -> addDigit(3)
            R.id.btn_4 -> addDigit(4)
            R.id.btn_5 -> addDigit(5)
            R.id.btn_6 -> addDigit(6)
            R.id.btn_7 -> addDigit(7)
            R.id.btn_8 -> addDigit(8)
            R.id.btn_9 -> addDigit(9)
        }
    }

    fun addZero() {
        if (lastKey == EQUALS) {
            mLastOperation = EQUALS
        }

        lastKey = DIGIT
        resetValueIfNeeded()
        addDigit(0)
        addDigit(0)
    }

    fun resetMemory() {
        mCallback!!.setVisibilityMemory(true, "M")
        mMemoryState = true
        mMemoryValue = 0.0
    }

    fun setMemory(value: String) {
        if (mMemoryState) {
            mCallback!!.setVisibilityMemory(true, value)
            if (value == "M-") {
                subToMemory()
            } else {
                plusToMemory()
            }
        }
    }

    private fun plusToMemory() {
        mMemoryValue += if (mBaseValue != 0.0) {
            mBaseValue
        } else {
            displayedNumber?.toDoubleOrNull()!!
        }
        mResetValue = true
    }

    private fun subToMemory() {
        mMemoryValue -= if (mBaseValue != 0.0) {
            mBaseValue
        } else {
            displayedNumber?.toDoubleOrNull()!!
        }
        mResetValue = true
    }

    fun memoryResult() {
        mBaseValue = mMemoryValue
        updateResult(mBaseValue)
    }


    fun setSet() {
        mCallback!!.setVisibilitySET(true)
        mSetState = true
    }

    fun setCost() {
        mCostValue = displayedNumber?.toDoubleOrNull()!!
        mCallback!!.setVisibilityMargin(true, "COST")
        mResetValue = true
    }

    fun setSell() {
        mSellValue = displayedNumber?.toDoubleOrNull()!!
        mCallback!!.setVisibilityMargin(true, "SELL")

        calculatorMargin()
    }

    fun taxAdd() {
        if (mSetValue != 0.0) {
            mCallback!!.setVisibilityTAX(true, "TAX+")
            mResultTax = displayedNumber?.toDoubleOrNull()!! * (100 + mSetValue) / 100
            updateResult(mResultTax)
        }
    }

    fun taxSub() {
        if (mSetValue != 0.0) {
            mCallback!!.setVisibilityTAX(true, "TAX−")
            mResultTax = displayedNumber?.toDoubleOrNull()!! * (100 - mSetValue) / 100
            updateResult(mResultTax)
        }
    }

    private fun calculatorMargin() {
        mMarValue = mSellValue - mCostValue
        mCallback!!.setVisibilityMargin(true, "MAR")
        updateResult(mMarValue)
    }

    private fun hiddenAllState() {
        mCallback!!.setVisibilityMargin(false, "")
        mCallback!!.setVisibilityGT(false)
        mCallback!!.setVisibilityMemory(false, "")
        mCallback!!.setVisibilityTAX(false, "")
        mCallback!!.setVisibilitySET(false)


    }
}
