package com.keremmuhcu.bitirmeprojesi.presentation.hikaye.hikaye_oku

import com.keremmuhcu.bitirmeprojesi.data.models.Hikaye

data class HikayeOkuState(
    var bekleniyor:Boolean = true,
    var toastMesaj:String = "",
    var hikaye: Hikaye = Hikaye(),
    var kutuphanedeMi:Boolean = false,
    var verilerAlindiMi:Boolean = false,
    var kalinanBolum:Int = 1,
)

