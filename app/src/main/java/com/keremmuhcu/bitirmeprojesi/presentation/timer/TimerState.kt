package com.keremmuhcu.bitirmeprojesi.presentation.timer

data class TimerState(
    var kalanZaman:Long = -1L,
    var gun: Long = 0L,
    var saat: Long = 0L,
    var dakika: Long = 0L,
    var saniye: Long = 0L,
    var gosterim: String = "",
    var bittiMi: Boolean = false,
    var asama: String = "",
    var bekleniyor:Boolean = true
)
