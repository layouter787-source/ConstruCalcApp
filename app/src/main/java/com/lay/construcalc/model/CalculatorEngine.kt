package com.lay.construcalc.model

import java.util.Locale
import kotlin.math.ceil
import kotlin.math.floor

object CalculatorEngine {
    fun calculate(type: CalculatorType, n: List<Double>): String = when (type) {
        CalculatorType.BLOCKS -> {
            val wallArea = n.getOrElse(0) { 0.0 } * n.getOrElse(1) { 0.0 }
            val blockArea = (n.getOrElse(2) { 0.0 } / 100.0) * (n.getOrElse(3) { 0.0 } / 100.0)
            if (wallArea > 0 && blockArea > 0) "≈ ${ceil(wallArea / blockArea).toInt()} blocos" else "Verifica as dimensões."
        }
        CalculatorType.BLOCK_MOLD -> calculateBlockMold(n)
        CalculatorType.CONCRETE -> "≈ ${format(n.getOrElse(0){0.0} * n.getOrElse(1){0.0} * n.getOrElse(2){0.0} * (1 + n.getOrElse(3){0.0}/100.0))} m³"
        CalculatorType.AREA -> "≈ ${format(n.getOrElse(0){0.0} * n.getOrElse(1){0.0} * (1 + n.getOrElse(2){0.0}/100.0))} m²"
        CalculatorType.VOLUME -> "≈ ${format(n.getOrElse(0){0.0} * n.getOrElse(1){0.0} * n.getOrElse(2){0.0})} m³"
    }

    private fun calculateBlockMold(n: List<Double>): String {
        val mold = doubleArrayOf(
            n.getOrElse(0) { 0.0 },
            n.getOrElse(1) { 0.0 },
            n.getOrElse(2) { 0.0 }
        )
        val block = doubleArrayOf(
            n.getOrElse(3) { 0.0 },
            n.getOrElse(4) { 0.0 },
            n.getOrElse(5) { 0.0 }
        )

        if ((mold + block).any { it <= 0.0 }) return "Preenche todas as dimensões."

        val orientations = listOf(
            intArrayOf(0, 1, 2), intArrayOf(0, 2, 1),
            intArrayOf(1, 0, 2), intArrayOf(1, 2, 0),
            intArrayOf(2, 0, 1), intArrayOf(2, 1, 0)
        )

        var bestTotal = 0
        var bestFit = intArrayOf(0, 0, 0)
        var bestOrientation = intArrayOf(0, 1, 2)

        for (orientation in orientations) {
            val x = floor(mold[0] / block[orientation[0]]).toInt()
            val y = floor(mold[1] / block[orientation[1]]).toInt()
            val z = floor(mold[2] / block[orientation[2]]).toInt()
            val total = x * y * z
            if (total > bestTotal) {
                bestTotal = total
                bestFit = intArrayOf(x, y, z)
                bestOrientation = orientation
            }
        }

        if (bestTotal <= 0) return "O bloco não cabe no molde."

        val moldVolume = mold[0] * mold[1] * mold[2]
        val blockVolume = block[0] * block[1] * block[2]
        val utilization = (bestTotal * blockVolume / moldVolume) * 100.0

        return buildString {
            append("$bestTotal blocos por molde")
            append("\n${bestFit[0]} × ${bestFit[1]} × ${bestFit[2]}")
            append("\nAproveitamento: ${format(utilization)}%")
            append("\nOrientação: ${block[bestOrientation[0]].removeTrailingZero()} × ${block[bestOrientation[1]].removeTrailingZero()} × ${block[bestOrientation[2]].removeTrailingZero()} cm")
        }
    }

    private fun Double.removeTrailingZero(): String =
        if (this % 1.0 == 0.0) toInt().toString() else String.format(Locale.getDefault(), "%.2f", this)

    private fun format(value: Double): String = String.format(Locale.getDefault(), "%.2f", value)
}
