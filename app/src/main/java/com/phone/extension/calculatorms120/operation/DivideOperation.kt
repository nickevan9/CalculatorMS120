package com.phone.extension.calculatorms120.operation

import com.phone.extension.calculatorms120.operation.base.BinaryOperation
import com.phone.extension.calculatorms120.operation.base.Operation

class DivideOperation(baseValue: Double, secondValue: Double) : BinaryOperation(baseValue, secondValue),
    Operation {

    override fun getResult(): Double {
        var result = 0.0
        if (secondValue != 0.0) {
            result = baseValue / secondValue
        }
        return result
    }
}
