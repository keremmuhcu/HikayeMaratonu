package com.keremmuhcu.bitirmeprojesi.presentation.biten_yarismalar

data class BitenYarismalarState(
    var toastMesaj:String = "",
    var bekleniyor:Boolean = false,
    var verilerBasariylaAlindiMi: Boolean = true,
    var formatliTarih:String = "",
)
