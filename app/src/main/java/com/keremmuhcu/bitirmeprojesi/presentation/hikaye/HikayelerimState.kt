package com.keremmuhcu.bitirmeprojesi.presentation.hikaye

data class HikayelerimState(
    var toastMesaj:String = "",
    var bekleniyor:Boolean = true,
    var dialogKontrol: Boolean = false,
    var yeniHikayeBaslik:String = "",
    var yeniHikayeGiris:String = "",
    var yeniHikayeIlkBolum:String = "",
    var gosterilecekAlert:String = "",
    var alertKontrol:Boolean = false,
    var dropdownKontrol:Boolean = false,
    var dropdownSiralaKontrol:Boolean = false,
    var verilerAlindiMi:Boolean = false,
    var ziyaretciMi:Boolean = false,
)
