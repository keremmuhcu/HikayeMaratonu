package com.keremmuhcu.bitirmeprojesi.presentation.profil

import com.keremmuhcu.bitirmeprojesi.data.models.Yazar

data class ProfilState(
    var toastMesaj: String = "",
    var yazar: Yazar = Yazar(),
    var bekleniyor:Boolean = true
)
