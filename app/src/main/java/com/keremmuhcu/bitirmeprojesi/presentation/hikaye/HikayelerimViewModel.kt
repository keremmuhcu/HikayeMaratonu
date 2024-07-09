package com.keremmuhcu.bitirmeprojesi.presentation.hikaye

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.keremmuhcu.bitirmeprojesi.data.FirebaseRepository
import com.keremmuhcu.bitirmeprojesi.data.models.Hikaye
import com.keremmuhcu.bitirmeprojesi.data.models.HikayeBolum
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HikayelerimViewModel:ViewModel() {
    private val firebaseRepository = FirebaseRepository()
    private val _hikayelerimState = mutableStateOf(HikayelerimState())
    val hikayelerimState: State<HikayelerimState> = _hikayelerimState

    private val _hikayelerimListesi = MutableStateFlow<List<Hikaye>>(emptyList())
    private val _hikayelerimListesiYedek = MutableStateFlow<List<Hikaye>>(emptyList())
    val hikayelerimListesi: StateFlow<List<Hikaye>> = _hikayelerimListesi

    fun hikayeleriGetir(email:String) {
        setBekleniyor(true)
        firebaseRepository.kullanicininHikayeleriniGetir(email) { basariDurumu, liste ->
            if (basariDurumu) {
                _hikayelerimListesi.value = liste
                _hikayelerimListesiYedek.value = liste
                setVerilerAlindiMi(true)
                sirala("hepsi")
                setBekleniyor(false)
            } else {
                setToastMesaj("Hata! Yeniden deneyin")
            }
        }
    }

    fun yeniHikayeyiKaydetYaDaYayinla(kullaniciAdi:String, yayinlanacakMi:Boolean,basariliMi:(Boolean) -> Unit) {
        setBekleniyor(true)
        val hikaye = Hikaye(
            baslik = _hikayelerimState.value.yeniHikayeBaslik.trim(),
            id = idCamelCaseYap(),
            yayinlandiMi = yayinlanacakMi
        )
        val girisBolum = HikayeBolum(
            hikaye = _hikayelerimState.value.yeniHikayeGiris.trim(),
            bolumId = 0,
            yayinlandiMi = yayinlanacakMi
        )

        val ilkBolum = HikayeBolum(
            hikaye = _hikayelerimState.value.yeniHikayeIlkBolum.trim(),
            bolumId = 1,
            yayinlandiMi = yayinlanacakMi
        )
        firebaseRepository.yeniHikayeyiYayinlaYaDaKaydet(hikaye,girisBolum,ilkBolum, kullaniciAdi) { basariDurumu, mesaj ->
            if (basariDurumu) {
                setToastMesaj(mesaj)
                basariliMi(true)
            } else {
                setToastMesaj(mesaj)
                basariliMi(false)
            }
            setBekleniyor(false)
        }
    }

    fun yeniHikayeninAlanlariDoldurulduMu():Boolean {
        val baslik = _hikayelerimState.value.yeniHikayeBaslik
        val giris = _hikayelerimState.value.yeniHikayeGiris
        val ilkBolum = _hikayelerimState.value.yeniHikayeIlkBolum

        return baslik.any {
            it.isLetter()
        } && giris.any {
            it.isLetter()
        } && ilkBolum.any {
            it.isLetter()
        }
    }

    private fun idCamelCaseYap():String {
        return _hikayelerimState.value.yeniHikayeBaslik.trim()
            .split("\\s+".toRegex()) // Boşluklardan böl
            .mapIndexed { index, word ->
                if (index == 0) {
                    word.lowercase()
                } else {
                    word.capitalize()
                }
            }
            .joinToString("")
    }

    fun sirala(yon:String) {
        _hikayelerimListesi.value = _hikayelerimListesiYedek.value

        when(yon) {
            "hepsi" -> {
                if (_hikayelerimState.value.ziyaretciMi) {
                    _hikayelerimListesi.value = _hikayelerimListesi.value.filter {
                        it.yayinlandiMi!!
                    }.sortedByDescending {
                        it.guncellenmeTarihi
                    }
                } else {
                    _hikayelerimListesi.value = _hikayelerimListesi.value.sortedByDescending {
                        it.guncellenmeTarihi
                    }
                }
            }
            "yayinlananlar" -> {
                _hikayelerimListesi.value = _hikayelerimListesi.value.filter {
                    it.yayinlandiMi == true && it.bittiMi == false
                }.sortedByDescending {
                    it.guncellenmeTarihi
                }

                if (_hikayelerimListesi.value.isEmpty()) {
                    setToastMesaj("Yayınlanan hikayeniz yok")
                    sirala("hepsi")
                }

            }
            "yayinlanmayanlar" -> {
                _hikayelerimListesi.value = _hikayelerimListesi.value.filter {
                    it.yayinlandiMi == false
                }.sortedByDescending {
                    it.guncellenmeTarihi
                }

                if (_hikayelerimListesi.value.isEmpty()) {
                    setToastMesaj("Yayınlanmayan hikayeniz yok")
                    sirala("hepsi")
                }
            }
            else -> {
                _hikayelerimListesi.value = _hikayelerimListesi.value.filter {
                    it.bittiMi == true
                }.sortedByDescending {
                    it.guncellenmeTarihi
                }

                if (_hikayelerimListesi.value.isEmpty()) {
                    setToastMesaj("Biten hikayeniz yok")
                    sirala("hepsi")
                }
            }
        }
    }

    fun setVerilerAlindiMi(durum: Boolean) {
        _hikayelerimState.value = _hikayelerimState.value.copy(verilerAlindiMi = durum)
    }

    fun setBekleniyor(durum:Boolean) {
        _hikayelerimState.value = _hikayelerimState.value.copy(bekleniyor = durum)
    }

    fun setToastMesaj(toastMesaj:String) {
        _hikayelerimState.value = _hikayelerimState.value.copy(toastMesaj = toastMesaj)
    }

    fun setDialogKontrol(durum:Boolean) {
        _hikayelerimState.value = _hikayelerimState.value.copy(dialogKontrol = durum)
    }

    fun setDropdownKontrol(durum:Boolean) {
        _hikayelerimState.value = _hikayelerimState.value.copy(dropdownKontrol = durum)
    }

    fun setDropdownSiralaKontrol(durum:Boolean) {
        _hikayelerimState.value = _hikayelerimState.value.copy(dropdownSiralaKontrol = durum)
    }

    fun setYeniHikayeBaslik(baslik:String) {
        _hikayelerimState.value = _hikayelerimState.value.copy(yeniHikayeBaslik = baslik)
    }

    fun setYeniHikayeGiris(giris:String) {
        _hikayelerimState.value = _hikayelerimState.value.copy(yeniHikayeGiris = giris)
    }

    fun setYeniHikayeIlkBolum(ilkBolum:String) {
        _hikayelerimState.value = _hikayelerimState.value.copy(yeniHikayeIlkBolum = ilkBolum)
    }

    fun setAlertKontrol(durum: Boolean) {
        _hikayelerimState.value = _hikayelerimState.value.copy(alertKontrol = durum)
    }

    fun setGosterilecekAlert(alert:String) {
        _hikayelerimState.value = _hikayelerimState.value.copy(gosterilecekAlert = alert)
    }

    fun yeniHikayeOlusturdakiAlanlariTemizle() {
        _hikayelerimState.value = _hikayelerimState.value.copy(yeniHikayeBaslik = "")
        _hikayelerimState.value = _hikayelerimState.value.copy(yeniHikayeGiris = "")
        _hikayelerimState.value = _hikayelerimState.value.copy(yeniHikayeIlkBolum = "")
    }

    fun setZiyaretciMi(email: String) {
        val durum = if (firebaseRepository.aktifKullanici?.email!! == email) {
            false
        } else {
            true
        }
        _hikayelerimState.value = _hikayelerimState.value.copy(ziyaretciMi = durum)
    }
}