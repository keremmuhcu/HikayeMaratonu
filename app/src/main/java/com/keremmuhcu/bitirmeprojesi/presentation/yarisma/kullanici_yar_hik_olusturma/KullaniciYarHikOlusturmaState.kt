package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.kullanici_yar_hik_olusturma

data class KullaniciYarHikOlusturmaState(
    var baslik:String = "",
    var kullaniciYarismaHikayesi: String = "",
    var dropdownKontrol: Boolean = false,
    var alertKontrol: Boolean = false,
    var gosterilecekAlert: String = "",
    var bekleniyor: Boolean = false,
    var toastMesaj:String = "",
    var hata:Boolean = false
)
