package com.lay.construcalc.model

import java.util.Locale
import kotlin.math.ceil
import kotlin.math.floor

object CalculatorEngine {
    fun calculate(type: CalculatorType, n: List<Double>): String = when (type) {
        CalculatorType.BLOCKS -> calculateBlocks(n)
        CalculatorType.BLOCK_MOLD -> calculateBlockMold(n)
        CalculatorType.CONCRETE -> "≈ ${format(n.getOrElse(0){0.0} * n.getOrElse(1){0.0} * n.getOrElse(2){0.0} * (1 + n.getOrElse(3){0.0}/100.0))} m³"
        CalculatorType.AREA -> "≈ ${format(n.getOrElse(0){0.0} * n.getOrElse(1){0.0} * (1 + n.getOrElse(2){0.0}/100.0))} m²"
        CalculatorType.VOLUME -> "≈ ${format(n.getOrElse(0){0.0} * n.getOrElse(1){0.0} * n.getOrElse(2){0.0})} m³"
    }

    private fun calculateBlocks(n: List<Double>): String {
        val wallLength = n.getOrElse(0) { 0.0 }
        val wallHeight = n.getOrElse(1) { 0.0 }
        val blockLength = n.getOrElse(2) { 0.0 }
        val blockHeight = n.getOrElse(3) { 0.0 }
        val jointCm = n.getOrElse(4) { 0.0 }.coerceAtLeast(0.0)
        val wastePercent = n.getOrElse(5) { 0.0 }.coerceAtLeast(0.0)
        val openings = n.getOrElse(6) { 0.0 }.coerceAtLeast(0.0)

        if (wallLength <= 0 || wallHeight <= 0 || blockLength <= 0 || blockHeight <= 0) return "Verifica as dimensões."

        val netArea = (wallLength * wallHeight - openings).coerceAtLeast(0.0)
        if (netArea <= 0.0) return "A área das aberturas é maior que a parede."

        val effectiveBlockArea = ((blockLength + jointCm) / 100.0) * ((blockHeight + jointCm) / 100.0)
        val base = ceil(netArea / effectiveBlockArea).toInt()
        val finalCount = ceil(base * (1.0 + wastePercent / 100.0)).toInt()

        return buildString {
            append("≈ $finalCount blocos")
            append("\nÁrea líquida: ${format(netArea)} m²")
            append("\nBase: $base blocos")
            if (jointCm > 0) append("\nJunta: ${format(jointCm)} cm")
            if (wastePercent > 0) append("\nDesperdício: ${format(wastePercent)}%")
            if (openings > 0) append("\nAberturas descontadas: ${format(openings)} m²")
        }
    }

    private fun calculateBlockMold(n: List<Double>): String {
        val mold = doubleArrayOf(n.getOrElse(0){0.0}, n.getOrElse(1){0.0}, n.getOrElse(2){0.0})
        val block = doubleArrayOf(n.getOrElse(3){0.0}, n.getOrElse(4){0.0}, n.getOrElse(5){0.0})
        if ((mold + block).any { it <= 0.0 }) return "Preenche todas as dimensões."

        val orientations = listOf(
            intArrayOf(0,1,2), intArrayOf(0,2,1), intArrayOf(1,0,2),
            intArrayOf(1,2,0), intArrayOf(2,0,1), intArrayOf(2,1,0)
        )
        var bestTotal = 0
        var bestFit = intArrayOf(0,0,0)
        var bestOrientation = intArrayOf(0,1,2)

        for (o in orientations) {
            val x = floor(mold[0] / block[o[0]]).toInt()
            val y = floor(mold[1] / block[o[1]]).toInt()
            val z = floor(mold[2] / block[o[2]]).toInt()
            val total = x * y * z
            if (total > bestTotal) {
                bestTotal = total
                bestFit = intArrayOf(x,y,z)
                bestOrientation = o
            }
        }
        if (bestTotal <= 0) return "O bloco não cabe no molde."

        val utilization = (bestTotal * block[0] * block[1] * block[2] / (mold[0] * mold[1] * mold[2])) * 100.0
        return buildString {
            append("$bestTotal blocos por molde")
            append("\n${bestFit[0]} × ${bestFit[1]} × ${bestFit[2]}")
            append("\nAproveitamento: ${format(utilization)}%")
            append("\nOrientação: ${trim(block[bestOrientation[0]])} × ${trim(block[bestOrientation[1]])} × ${trim(block[bestOrientation[2]])} cm")
        }
    }

    private fun trim(value: Double) = if (value % 1.0 == 0.0) value.toInt().toString() else String.format(Locale.US, "%.2f", value)
    private fun format(value: Double) = String.format(Locale.US, "%.2f", value)
}
