package com.keremmuhcu.bitirmeprojesi.presentation.biten_yarismalar.biten_yarisma_detay

import com.keremmuhcu.bitirmeprojesi.data.models.KullaniciYarismaHikayesi

data class BitenYarismaDetayState(
    var toastMesaj:String = "",
    var bekleniyor:Boolean = true,
    var digerleriniGoster: Boolean = false,
    var secilenHikaye: KullaniciYarismaHikayesi = KullaniciYarismaHikayesi(),
    var hikayeDialogKontrol:Boolean = false,
    var veriCekmeMetoduCalistiMi: Boolean = false,
)
