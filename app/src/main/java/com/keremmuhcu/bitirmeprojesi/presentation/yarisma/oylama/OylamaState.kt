package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.oylama

import com.keremmuhcu.bitirmeprojesi.data.models.KullaniciYarismaHikayesi

data class OylamaState(
    var toastMesaj: String = "",
    var bekleniyor:Boolean = false,
    var tiklananHikayeKontrol: Boolean = false,
    var secilenHikaye:KullaniciYarismaHikayesi = KullaniciYarismaHikayesi(),
    var yazarOyVermisMi:Boolean = false,
    var kalanZaman:Long = -1L,
    var gun: Long = 0L,
    var saat: Long = 0L,
    var dakika: Long = 0L,
    var saniye: Long = 0L,
    var gosterim: String = "",
    var sureBittiMi: Boolean = false,
    var hikayeYazarinMi: Boolean = false,
    var dropdownKontrol: Boolean = false,
    var verilenOy:String = "",
    var infoButonunaTiklandiMi: Boolean = false,
    var katilimSaglanmisMi:Boolean = false
)
