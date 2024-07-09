package com.keremmuhcu.bitirmeprojesi.data.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class KullaniciYarismaHikayesi(
    val kullaniciYarismaHikayeBaslik:String? = "",
    val kullaniciYarismaHikayesi:String? = "",
    val yazarKullaniciAdi:String? = "",
    val yazarEmail:String? = "",
    val kabulDurumu:String? = "",
    val oylar:Int? = 0,
    val derece:String? = "",
    @ServerTimestamp val olusturmaTarihi: Date? = null,
    @ServerTimestamp val yarismaTarihi: Date? = null
)
