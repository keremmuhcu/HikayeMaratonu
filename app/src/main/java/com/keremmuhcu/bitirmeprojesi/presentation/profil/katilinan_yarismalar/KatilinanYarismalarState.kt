package com.keremmuhcu.bitirmeprojesi.presentation.profil.katilinan_yarismalar

import com.keremmuhcu.bitirmeprojesi.data.models.KullaniciYarismaHikayesi

data class KatilinanYarismalarState(
    var toastMesaj:String = "",
    var bekleniyor:Boolean = true,
    var dropdownGoster: Boolean = false,
    var siralamaYonu:String = "azalan",
    var tiklananYarismaAnaHikaye:String = "",
    var siralamaKriteri:String = "derece",
    var dialogKontrol: Boolean = false,
    var secilenHikaye:KullaniciYarismaHikayesi = KullaniciYarismaHikayesi()
)
