package com.phone.extension.calculatorms120.operation

import com.phone.extension.calculatorms120.helpers.*
import com.phone.extension.calculatorms120.operation.base.Operation

object OperationFactory {

    fun forId(id: String, baseValue: Double, secondValue: Double): Operation? {
        return when (id) {
            PLUS -> PlusOperation(baseValue, secondValue)
            MINUS -> MinusOperation(baseValue, secondValue)
            DIVIDE -> DivideOperation(baseValue, secondValue)
            MULTIPLY -> MultiplyOperation(baseValue, secondValue)
            PERCENT -> PercentOperation(baseValue, secondValue)
            POWER -> PowerOperation(baseValue, secondValue)
            ROOT -> RootOperation(baseValue)
            FACTORIAL -> FactorialOperation(baseValue)
            else -> null
        }
    }
}
