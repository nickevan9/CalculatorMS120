package com.phone.extension.calculatorms120.operation

import com.phone.extension.calculatorms120.operation.base.BinaryOperation
import com.phone.extension.calculatorms120.operation.base.Operation

class MultiplyOperation(baseValue: Double, secondValue: Double) : BinaryOperation(baseValue, secondValue),
    Operation {

    override fun getResult() = baseValue * secondValue
}
