package com.keremmuhcu.bitirmeprojesi.data.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class YarismaHikayesi(
    var anaHikaye: String? = "",
    val bittiMi: Boolean? = false,
    val id:String? = "",
    @ServerTimestamp val baslamaTarihi: Date? = null,
    @ServerTimestamp val bitisTarihi: Date? = null,
    @ServerTimestamp val oylamaTarihi: Date? = null
)