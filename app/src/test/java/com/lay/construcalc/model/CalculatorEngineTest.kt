package com.lay.construcalc.model

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculatorEngineTest {
    @Test fun blocks_round_up() =
        assertEquals(
            "≈ 100 blocos\nÁrea líquida: 20.00 m²\nBase: 100 blocos",
            CalculatorEngine.calculate(CalculatorType.BLOCKS, listOf(10.0, 2.0, 40.0, 40.0))
        )

    @Test fun blocks_applies_joint_waste_and_openings() =
        assertEquals(
            "≈ 83 blocos\nÁrea líquida: 18.00 m²\nBase: 75 blocos\nJunta: 1.00 cm\nDesperdício: 10.00%\nAberturas descontadas: 2.00 m²",
            CalculatorEngine.calculate(CalculatorType.BLOCKS, listOf(10.0, 2.0, 40.0, 40.0, 1.0, 10.0, 2.0))
        )

    @Test fun block_mold_checks_rotated_orientation() =
        assertEquals(
            "4 blocos por molde\n1 × 2 × 2\nAproveitamento: 66.67%\nOrientação: 20 × 10 × 5 cm",
            CalculatorEngine.calculate(CalculatorType.BLOCK_MOLD, listOf(30.0, 20.0, 10.0, 10.0, 5.0, 20.0))
        )

    @Test fun concrete_applies_margin() =
        assertEquals("≈ 1.10 m³", CalculatorEngine.calculate(CalculatorType.CONCRETE, listOf(2.0, 0.5, 1.0, 10.0)))

    @Test fun area_applies_extra() =
        assertEquals("≈ 11.00 m²", CalculatorEngine.calculate(CalculatorType.AREA, listOf(5.0, 2.0, 10.0)))

    @Test fun volume_is_length_width_height() =
        assertEquals("≈ 6.00 m³", CalculatorEngine.calculate(CalculatorType.VOLUME, listOf(2.0, 1.5, 2.0)))
}
