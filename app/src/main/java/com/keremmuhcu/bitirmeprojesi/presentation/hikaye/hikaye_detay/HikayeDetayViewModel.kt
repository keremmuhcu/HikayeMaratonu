package com.keremmuhcu.bitirmeprojesi.presentation.hikaye.hikaye_detay

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.keremmuhcu.bitirmeprojesi.data.FirebaseRepository
import com.keremmuhcu.bitirmeprojesi.data.models.Hikaye
import com.keremmuhcu.bitirmeprojesi.data.models.HikayeBolum

class HikayeDetayViewModel:ViewModel() {
    private val firebaseRepository = FirebaseRepository()

    private val _hikayeDetayState = mutableStateOf(HikayeDetayState())
    val hikayeDetayState:State<HikayeDetayState> = _hikayeDetayState

    private val _bolumlerListesi = mutableStateOf(firebaseRepository.hikayeBolumleriListesi.value)
    var bolumlerListesi: State<List<HikayeBolum>> = _bolumlerListesi

    fun hikayeyiGetir(hikayeId:String,basariliMi: (Boolean) -> Unit,) {
        setBekleniyor(true)
        firebaseRepository.hikayeyiGetir("aktif", hikayeId) { basariDurumu,hikaye->
            if (basariDurumu) {
                setHikaye(hikaye)
                setBaslik(hikaye.baslik!!)
                basariliMi(true)
            } else {
                basariliMi(false)
            }
        }
    }


    fun bolumleriGetir(email:String, hikayeId: String,basariliMi:(Boolean) -> Unit) {
        firebaseRepository.hikayeninBolumleriniGetir(email, hikayeId) {it, _ ->
            if (it) {
                _bolumlerListesi.value = firebaseRepository.hikayeBolumleriListesi.value
                _bolumlerListesi.value = _bolumlerListesi.value.sortedBy {bolum->
                    bolum.bolumId!!
                }
                basariliMi(true)
            } else {
                basariliMi(false)
            }
        }
    }

    fun yeniBolumEkle(yayinlanacakMi:Boolean, basariliMi: (Boolean) -> Unit) {
        setBekleniyor(true)
        val yeniBolum = HikayeBolum(
            hikaye = _hikayeDetayState.value.yeniBolumHikayesi,
            bolumId = bolumlerListesi.value.last().bolumId!!.toInt() + 1,
            yayinlandiMi = yayinlanacakMi
        )

        firebaseRepository.hikayeBolumuKaydet(_hikayeDetayState.value.hikaye.id!!, yeniBolum) {basariDurumu, mesaj ->
            if (basariDurumu) {
                setToastMesaj(mesaj)
                basariliMi(true)
            } else {
                basariliMi(false)
            }
        }
    }

    fun hikayeyiYayinla(basariliMi: (Boolean) -> Unit) {
        firebaseRepository.hikayeyiYayinla(_hikayeDetayState.value.hikaye.id!!) {
            if (it) {
                basariliMi(true)
            } else {
                basariliMi(false)
            }
        }
    }

    fun hikayeyiFinalleYaDaDevamEttir(durum: Boolean,basariliMi: (Boolean) -> Unit) {
        firebaseRepository.hikayeyiFinalleYaDaDevamEttir(durum,_hikayeDetayState.value.hikaye.id!!) {
            if (it) {
                basariliMi(true)
            } else {
                basariliMi(false)
            }
        }
    }

    fun hikayeyiSil(basariliMi: (Boolean) -> Unit) {
        listenerlaBaglantiKes()
        firebaseRepository.hikayeyiSil(_hikayeDetayState.value.hikaye.id!!) {
            if (it) {
                basariliMi(true)
            } else {
                basariliMi(false)
            }
        }
    }

    fun listenerlaBaglantiKes() {
        firebaseRepository.bolumlerListenerTakip?.remove()
    }

    fun bolumSil(basariliMi: (Boolean) -> Unit) {
        firebaseRepository.bolumSil(_hikayeDetayState.value.hikaye.id!!, _hikayeDetayState.value.secilenBolum.bolumId!!.toString()) {
            if (it) {
                basariliMi(true)
            } else {
                basariliMi(false)
            }
        }
    }

    fun hepsiniYayinla(basariliMi: (Boolean) -> Unit) {
        val liste = bolumlerListesi.value.filter {
            !it.yayinlandiMi!!
        }
        firebaseRepository.bolumleriYayinla(_hikayeDetayState.value.hikaye.id!!, liste) {
            if (it) {
                basariliMi(true)
            } else {
                basariliMi(false)
            }
        }
    }

    fun bolumYayinla(basariliMi: (Boolean) -> Unit) {
        firebaseRepository.bolumleriYayinla(_hikayeDetayState.value.hikaye.id!!, listOf(_hikayeDetayState.value.secilenBolum)) {
            if (it) {
                basariliMi(true)
            } else {
                basariliMi(false)
            }
        }
    }

    fun bolumuDuzenle(basariliMi: (Boolean) -> Unit) {
        val hikayeId = _hikayeDetayState.value.hikaye.id!!
        val bolumId = _hikayeDetayState.value.secilenBolum.bolumId!!.toString()
        val hikayeMetni = _hikayeDetayState.value.duzenlenenHikaye.trim()
        val baslik = if (_bolumlerListesi.value.indexOf(_hikayeDetayState.value.secilenBolum) == 0) {
            _hikayeDetayState.value.baslik.trim()
        } else {
            null
        }
        firebaseRepository.bolumuDuzenle(hikayeId,bolumId,hikayeMetni, baslik) {
            if (it) {
                basariliMi(true)
            } else {
                basariliMi(false)
            }
        }
    }

    fun yayinlanacakVarMi() : Boolean {
        return bolumlerListesi.value.any {
            !it.yayinlandiMi!!
        }
    }

    fun bolumunIcerigiDoluMu(): Boolean {
        return if (_bolumlerListesi.value.indexOf(_hikayeDetayState.value.secilenBolum) == 0) {
            _hikayeDetayState.value.duzenlenenHikaye.any {
                it.isLetter()
            } && _hikayeDetayState.value.baslik.any() {
                it.isLetter()
            }
        } else {
            if (_hikayeDetayState.value.duzenleModuAcikMi) {
                _hikayeDetayState.value.duzenlenenHikaye.any {
                    it.isLetter()
                }
            } else {
                _hikayeDetayState.value.yeniBolumHikayesi.any {
                    it.isLetter()
                }
            }
        }

    }

    fun setSecilenBolum(secilenBolum: HikayeBolum) {
        _hikayeDetayState.value = _hikayeDetayState.value.copy(secilenBolum = secilenBolum)
        setDuzenlenenHikaye(_hikayeDetayState.value.secilenBolum.hikaye!!)

    }

    fun setToastMesaj(toastMesaj: String) {
        _hikayeDetayState.value = _hikayeDetayState.value.copy(toastMesaj = toastMesaj)
    }

    fun setBekleniyor(durum: Boolean) {
        _hikayeDetayState.value = _hikayeDetayState.value.copy(bekleniyor = durum)
    }

    fun setVerilerAlindiMi(durum: Boolean) {
        _hikayeDetayState.value = _hikayeDetayState.value.copy(verilerAlindiMi = durum)
    }

    fun setDropdownKontrol(durum: Boolean) {
        _hikayeDetayState.value = _hikayeDetayState.value.copy(dropdownKontrol = durum)
    }

    fun setDropdownDuzenleKontrol(durum: Boolean) {
        _hikayeDetayState.value = _hikayeDetayState.value.copy(dropdownDuzenleKontrol = durum)
    }

    fun setDuzenleModuAcikMi(durum: Boolean) {
        _hikayeDetayState.value = _hikayeDetayState.value.copy(duzenleModuAcikMi = durum)
    }

    fun setDialogKontrol(durum: Boolean) {
        _hikayeDetayState.value = _hikayeDetayState.value.copy(dialogKontrol = durum)
    }

    fun setAlertKontrol(durum: Boolean) {
        _hikayeDetayState.value = _hikayeDetayState.value.copy(alertKontrol = durum)
    }

    fun setGosterilecekAlert(gosterilecekAlert:String) {
        _hikayeDetayState.value = _hikayeDetayState.value.copy(gosterilecekAlert = gosterilecekAlert)
    }

    fun setYeniBolumHikayesi(yeniBolumHikayesi:String) {
        _hikayeDetayState.value = _hikayeDetayState.value.copy(yeniBolumHikayesi = yeniBolumHikayesi)
    }

    fun setDuzenlenenHikaye(it:String) {
        _hikayeDetayState.value = _hikayeDetayState.value.copy(duzenlenenHikaye = it)
    }

    fun setSecilenBolumIndex(index:Int) {
        _hikayeDetayState.value = _hikayeDetayState.value.copy(secilenBolumIndex = index)
    }

    fun setBaslik(baslik:String) {
        _hikayeDetayState.value = _hikayeDetayState.value.copy(baslik = baslik)
    }

    fun setHikaye(hikaye:Hikaye) {
        _hikayeDetayState.value = _hikayeDetayState.value.copy(hikaye = hikaye)
    }

    fun setVeriEklenipSilinmeKontrolu(boyut:Int) {
        _hikayeDetayState.value = _hikayeDetayState.value.copy(veriEklenipSilinmeKontrolu = boyut)
    }


}