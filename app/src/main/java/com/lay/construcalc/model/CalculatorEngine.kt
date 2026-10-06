package com.lay.construcalc.model

import java.util.Locale
import kotlin.math.floor
import kotlin.math.ceil

object CalculatorEngine {
    fun calculate(type: CalculatorType, n: List<Double>): String = when (type) {
        CalculatorType.BLOCKS -> {
            val wallArea = n.getOrElse(0) { 0.0 } * n.getOrElse(1) { 0.0 }
            val blockArea = (n.getOrElse(2) { 0.0 } / 100.0) * (n.getOrElse(3) { 0.0 } / 100.0)
            if (wallArea > 0 && blockArea > 0) "≈ ${ceil(wallArea / blockArea).toInt()} blocos" else "Verifica as dimensões."
        }
        CalculatorType.BLOCK_MOLD -> {
            val moldL = n.getOrElse(0) { 0.0 }
            val moldW = n.getOrElse(1) { 0.0 }
            val moldH = n.getOrElse(2) { 0.0 }
            val blockL = n.getOrElse(3) { 0.0 }
            val blockW = n.getOrElse(4) { 0.0 }
            val blockH = n.getOrElse(5) { 0.0 }
            if (listOf(moldL, moldW, moldH, blockL, blockW, blockH).all { it > 0 }) {
                val byLength = floor(moldL / blockL).toInt()
                val byWidth = floor(moldW / blockW).toInt()
                val byHeight = floor(moldH / blockH).toInt()
                val total = byLength * byWidth * byHeight
                if (total > 0) "$total blocos por molde" else "O bloco não cabe no molde."
            } else "Preenche todas as dimensões."
        }
        CalculatorType.CONCRETE -> "≈ ${format(n.getOrElse(0){0.0} * n.getOrElse(1){0.0} * n.getOrElse(2){0.0} * (1 + n.getOrElse(3){0.0}/100.0))} m³"
        CalculatorType.AREA -> "≈ ${format(n.getOrElse(0){0.0} * n.getOrElse(1){0.0} * (1 + n.getOrElse(2){0.0}/100.0))} m²"
        CalculatorType.VOLUME -> "≈ ${format(n.getOrElse(0){0.0} * n.getOrElse(1){0.0} * n.getOrElse(2){0.0})} m³"
    }
    private fun format(value: Double): String = String.format(Locale.getDefault(), "%.2f", value)
}
