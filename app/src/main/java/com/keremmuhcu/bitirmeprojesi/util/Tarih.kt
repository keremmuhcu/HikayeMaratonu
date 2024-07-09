package com.keremmuhcu.bitirmeprojesi.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Tarih {
    fun tarihiFormatla(
        baslamaTarihi: Date,
    ):String {
        val tarih = baslamaTarihi.toString()

        val gelenFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
        val hedefFormat = SimpleDateFormat("dd MMMM yyyy", Locale("tr"))

        val simpleTarih = gelenFormat.parse(tarih)
        val sonuc = hedefFormat.format(simpleTarih)
        return sonuc
    }
}