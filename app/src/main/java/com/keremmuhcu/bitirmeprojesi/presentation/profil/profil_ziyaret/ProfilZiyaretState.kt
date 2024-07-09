package com.keremmuhcu.bitirmeprojesi.presentation.profil.profil_ziyaret

import com.keremmuhcu.bitirmeprojesi.data.models.Yazar

data class ProfilZiyaretState(
    var toastMesaj: String = "",
    var aktifYazar: Yazar = Yazar(),
    var yazar: Yazar = Yazar(),
    var bekleniyor:Boolean = true,
    var dropdownGoster: Boolean = false
)
