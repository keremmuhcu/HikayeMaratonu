package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.sonuc

import com.keremmuhcu.bitirmeprojesi.data.models.KullaniciYarismaHikayesi

data class SonucState(
    var toastMesaj:String = "",
    var bekleniyor:Boolean = true,
    var digerleriniGoster: Boolean = false,
    var secilenHikaye: KullaniciYarismaHikayesi = KullaniciYarismaHikayesi(),
    var hikayeDialogKontrol:Boolean = false,
    var gosterilecekAlert: String = "",
    var alertKontrol:Boolean = false,
    var katilimSaglanmisMi:Boolean = false,
    var kacinciOlundu:Int = 0

)
