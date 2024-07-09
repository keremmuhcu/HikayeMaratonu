package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.onaylama

import com.keremmuhcu.bitirmeprojesi.data.models.KullaniciYarismaHikayesi

data class OnaylamaState(
    var toastMesaj: String = "",
    var bekleniyor:Boolean = false,
    var onaylamaPencereKontrol:Boolean = false,
    var secilenHikaye:KullaniciYarismaHikayesi = KullaniciYarismaHikayesi()
)
