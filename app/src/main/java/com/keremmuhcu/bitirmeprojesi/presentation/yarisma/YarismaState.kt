package com.keremmuhcu.bitirmeprojesi.presentation.yarisma

data class YarismaState(
    var bekleniyor: Boolean = true,
    var toastMesaj: String = "",
    var hataDurumu: Boolean = false,
    var baslangicBilgileriAlindiMi: Boolean = false,
    var hikayeAsamasiAlindiMi:Boolean = false,

)
