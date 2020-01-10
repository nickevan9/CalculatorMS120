package com.phone.extension.calculatorms120.operation

import com.phone.extension.calculatorms120.operation.base.Operation
import com.phone.extension.calculatorms120.operation.base.UnaryOperation
import kotlin.math.sqrt

class RootOperation(value: Double) : UnaryOperation(value), Operation {
    override fun getResult() = sqrt(value)
}
