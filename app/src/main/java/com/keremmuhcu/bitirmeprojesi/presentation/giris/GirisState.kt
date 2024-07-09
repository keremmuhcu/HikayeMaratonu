package com.keremmuhcu.bitirmeprojesi.presentation.giris

data class GirisState(
    var kullaniciAdiVeyaEmail: String = "",
    var sifre: String = "",
    var toastMesaj: String = "",
    var girisBasariliMi: Boolean = false,
    var bekleniyor:Boolean = false
)
