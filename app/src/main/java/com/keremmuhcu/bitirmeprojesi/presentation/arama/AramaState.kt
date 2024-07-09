package com.keremmuhcu.bitirmeprojesi.presentation.arama

data class AramaState(
    var sorgu:String = "",
    var aktiflik:Boolean = false,
    var bekleniyor:Boolean = false,
    var toastMesaj:String = "",
    var email:String = "",
)
