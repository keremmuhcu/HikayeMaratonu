package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.yarisma_hikayesi

import com.keremmuhcu.bitirmeprojesi.data.models.YarismaHikayesi

data class YarismaHikayesiState(
    var toastMesaj: String = "",
    var alertKontrol: Boolean = false,
    var gosterilecekAlert: String = "",
    var yarismaHikayesi: YarismaHikayesi = YarismaHikayesi(),
    var anaHikaye: String = "",
    var duzenleModu: Boolean = false,
    var dropdownKontrol:Boolean = false,
    var kullaniciYarHikDurum:String = "",
    var kullaniciYarismaHikayesiBaslik:String = "",
    var kullaniciYarismaHikayesi:String = "",
    var hikayeyiGormeKontrolu:Boolean = false,
    var bekleniyor:Boolean = false
)
