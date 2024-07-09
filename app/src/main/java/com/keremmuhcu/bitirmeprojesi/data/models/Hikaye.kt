package com.keremmuhcu.bitirmeprojesi.data.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Hikaye(
    val baslik:String? = "",
    val id:String? = "",
    val yayinlandiMi:Boolean? = false,
    val bittiMi:Boolean? = false,
    var yazarEmail:String? = "",
    var yazarKullaniciAdi:String? = "",
    var kalinanBolum:Int = 0,
    @ServerTimestamp val olusturmaTarihi: Date? = null,
    @ServerTimestamp val guncellenmeTarihi: Date? = null,
    @ServerTimestamp val okunmaTarihi: Date? = null
)
