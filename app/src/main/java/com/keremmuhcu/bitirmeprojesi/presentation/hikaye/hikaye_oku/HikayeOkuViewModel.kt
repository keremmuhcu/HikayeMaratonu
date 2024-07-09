package com.keremmuhcu.bitirmeprojesi.presentation.hikaye.hikaye_oku

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.keremmuhcu.bitirmeprojesi.data.FirebaseRepository
import com.keremmuhcu.bitirmeprojesi.data.models.Hikaye
import com.keremmuhcu.bitirmeprojesi.data.models.HikayeBolum

class HikayeOkuViewModel:ViewModel() {
    private val firebaseRepository = FirebaseRepository()
    private val _hikayeOkuState = mutableStateOf(HikayeOkuState())
    val hikayeOkuState: State<HikayeOkuState> = _hikayeOkuState

    private val _bolumlerListesi = mutableStateOf<List<HikayeBolum>>(emptyList())
    val bolumlerListesi:State<List<HikayeBolum>> = _bolumlerListesi

    fun kutuphanedeMi(hikayeId:String, bittiMi:(Boolean) -> Unit) {
        firebaseRepository.hikayeYazarinKutuphanesindeMi(hikayeId) {varMi,hikaye ->
            if (varMi) {
                setKutuphanedeMi(true)
                setHikaye(hikaye!!)
            } else {
                setKutuphanedeMi(false)
            }
            bittiMi(true)
        }
    }

    fun hikayeyiKutuphaneyeEkleCikar(eklenecekMi: Boolean, basariliMi: (Boolean) -> Unit) {
        firebaseRepository.hikayeyiKutuphaneyeEkleCikar(_hikayeOkuState.value.hikaye,eklenecekMi,_hikayeOkuState.value.kalinanBolum) {
            if (it) {
                if (!eklenecekMi) {
                    setKutuphanedeMi(false)
                } else {
                    setKutuphanedeMi(true)
                }
                basariliMi(true)
            } else {
                basariliMi(false)
            }
        }
    }

    fun hikayeyiGetir(hikayeId: String, email:String, basariliMi: (Boolean) -> Unit) {
        firebaseRepository.hikayeyiGetir(email,hikayeId) {basariDurumu, hikaye ->
            if (basariDurumu) {
                setHikaye(hikaye)
                basariliMi(true)
            } else {
                basariliMi(false)
            }

        }
    }

    fun bolumleriGetir(hikayeId: String, email:String, basariliMi: (Boolean) -> Unit) {
        firebaseRepository.hikayeninBolumleriniGetir(email,hikayeId) {basariDurumu, liste ->
            if (basariDurumu) {
                _bolumlerListesi.value = liste!!.filter {
                    it.yayinlandiMi!!
                }.sortedBy {
                    it.bolumId
                }
                println(_bolumlerListesi.value.get(0).hikaye)
                //setBekleniyor(false)
                basariliMi(true)
            } else {
                basariliMi(false)
            }
        }
    }

    fun kalinanBolumuKaydet(basariliMi: (Boolean) -> Unit) {
        firebaseRepository.kalinanYeriKaydet(_hikayeOkuState.value.hikaye.id!!,_hikayeOkuState.value.kalinanBolum) {
            if (it) {
                basariliMi(true)
            } else {
                basariliMi(false)
            }
        }

    }

    fun setBekleniyor(durum:Boolean) {
        _hikayeOkuState.value = _hikayeOkuState.value.copy(bekleniyor = durum)
    }

    fun setVerilerAlindiMi(durum:Boolean) {
        _hikayeOkuState.value = _hikayeOkuState.value.copy(verilerAlindiMi = durum)
    }

    fun setToastMesaj(toastMesaj:String) {
        _hikayeOkuState.value = _hikayeOkuState.value.copy(toastMesaj = toastMesaj)
    }

    fun setHikaye(hikaye:Hikaye) {
        _hikayeOkuState.value = _hikayeOkuState.value.copy(hikaye = hikaye)
    }

    fun setKutuphanedeMi(durum: Boolean) {
        _hikayeOkuState.value = _hikayeOkuState.value.copy(kutuphanedeMi = durum)
    }

    fun setKalinanBolum(bolum:Int) {
        _hikayeOkuState.value = _hikayeOkuState.value.copy(kalinanBolum = bolum)
    }

    fun getAktifKullaniciEmail() :String {
        return firebaseRepository.aktifKullanici?.email!!.toString()
    }
}