package com.lay.construcalc.model

enum class CalculatorType(val title: String, val subtitle: String, val icon: String) {
    BLOCKS("Blocos", "Quantidade para uma parede", "▦"),
    BLOCK_MOLD("Blocos no molde", "Quantos cabem no molde", "▤"),
    CONCRETE("Concreto", "Volume necessário", "⬡"),
    AREA("Área", "Pisos, paredes e terrenos", "□"),
    VOLUME("Volume", "Espaço tridimensional", "◇")
}
