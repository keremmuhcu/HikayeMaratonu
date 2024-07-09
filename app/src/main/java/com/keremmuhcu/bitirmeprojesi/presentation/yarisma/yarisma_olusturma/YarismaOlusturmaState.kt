package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.yarisma_olusturma

data class YarismaOlusturmaState(
    var anaHikaye: String = "",
    var alertAcilisKontrol: Boolean = false,
    var kacGunYarisilacak: String = "",
    var gosterilecekAlert: String = "",
    var bekleniyor: Boolean = false,
    var toastMesaj: String = ""
)
