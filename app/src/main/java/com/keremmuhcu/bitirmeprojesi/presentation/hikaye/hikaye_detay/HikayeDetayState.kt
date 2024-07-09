package com.keremmuhcu.bitirmeprojesi.presentation.hikaye.hikaye_detay

import com.keremmuhcu.bitirmeprojesi.data.models.Hikaye
import com.keremmuhcu.bitirmeprojesi.data.models.HikayeBolum

data class HikayeDetayState(
    var bekleniyor:Boolean = true,
    var toastMesaj:String = "",
    var verilerAlindiMi:Boolean = false,
    var secilenBolum:HikayeBolum = HikayeBolum(),
    var secilenBolumIndex:Int = 0,
    var dialogKontrol:Boolean = false,
    var alertKontrol:Boolean = false,
    var gosterilecekAlert:String = "",
    var yeniBolumHikayesi:String = "",
    var duzenlenenHikaye:String = "",
    var baslik:String = "",
    var dropdownKontrol:Boolean = false,
    var hikaye: Hikaye = Hikaye(),
    var dropdownDuzenleKontrol:Boolean = false,
    var duzenleModuAcikMi:Boolean = false,
    var veriEklenipSilinmeKontrolu: Int = 0
)
