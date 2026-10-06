package com.lay.construcalc.model

import java.util.Locale
import kotlin.math.ceil

object CalculatorEngine {
    fun calculate(type: CalculatorType, n: List<Double>): String = when (type) {
        CalculatorType.BLOCKS -> {
            val wallArea = n.getOrElse(0) { 0.0 } * n.getOrElse(1) { 0.0 }
            val blockArea = (n.getOrElse(2) { 0.0 } / 100.0) * (n.getOrElse(3) { 0.0 } / 100.0)
            if (wallArea > 0 && blockArea > 0) "≈ ${ceil(wallArea / blockArea).toInt()} blocos" else "Verifica as dimensões."
        }
        CalculatorType.CONCRETE -> "≈ ${format(n.getOrElse(0){0.0} * n.getOrElse(1){0.0} * n.getOrElse(2){0.0} * (1 + n.getOrElse(3){0.0}/100.0))} m³"
        CalculatorType.AREA -> "≈ ${format(n.getOrElse(0){0.0} * n.getOrElse(1){0.0} * (1 + n.getOrElse(2){0.0}/100.0))} m²"
        CalculatorType.VOLUME -> "≈ ${format(n.getOrElse(0){0.0} * n.getOrElse(1){0.0} * n.getOrElse(2){0.0})} m³"
    }
    private fun format(value: Double): String = String.format(Locale.getDefault(), "%.2f", value)
}
