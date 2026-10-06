package com.lay.construcalc.model

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculatorEngineTest {
    @Test fun blocks_round_up() =
        assertEquals("≈ 100 blocos", CalculatorEngine.calculate(CalculatorType.BLOCKS, listOf(10.0, 2.0, 40.0, 40.0)))

    @Test fun concrete_applies_margin() =
        assertEquals("≈ 1.10 m³", CalculatorEngine.calculate(CalculatorType.CONCRETE, listOf(2.0, 0.5, 1.0, 10.0)))

    @Test fun area_applies_extra() =
        assertEquals("≈ 11.00 m²", CalculatorEngine.calculate(CalculatorType.AREA, listOf(5.0, 2.0, 10.0)))

    @Test fun volume_is_length_width_height() =
        assertEquals("≈ 6.00 m³", CalculatorEngine.calculate(CalculatorType.VOLUME, listOf(2.0, 1.5, 2.0)))
}
