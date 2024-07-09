package com.keremmuhcu.bitirmeprojesi.presentation.kayit

data class KayitState(
    var kullaniciAdi: String = "",
    var email: String = "",
    var sifre: String = "",
    var toastMesaj: String = "",
    var bekleniyor:Boolean = false,
    var kayitBasariliMi:Boolean = false
)
