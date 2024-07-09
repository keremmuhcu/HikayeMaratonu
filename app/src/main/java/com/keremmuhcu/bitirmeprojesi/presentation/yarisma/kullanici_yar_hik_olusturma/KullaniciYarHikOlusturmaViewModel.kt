package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.kullanici_yar_hik_olusturma

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.keremmuhcu.bitirmeprojesi.data.FirebaseRepository
import com.keremmuhcu.bitirmeprojesi.data.models.KullaniciYarismaHikayesi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class KullaniciYarHikOlusturmaViewModel:ViewModel() {
    private val firebaseRepository = FirebaseRepository()
    private val _kullaniciYarHikOlusturmaState = MutableStateFlow(KullaniciYarHikOlusturmaState())
    val kullaniciYarHikOlusturmaState: StateFlow<KullaniciYarHikOlusturmaState> = _kullaniciYarHikOlusturmaState

    init {
        setBekleniyor(true)
        firebaseRepository.kullaniciYarismasiniTaslaktanAl {
            if (it.isEmpty() || (it["baslik"] == null) || (it["hikaye"] == null) || (it["baslik"] == "") || (it["hikaye"] == "")) {
                println("buraya girdi.")
                setToastMesaj("Taslak Bulunamadı")
                //_kullaniciYarHikOlusturmaState.value = _kullaniciYarHikOlusturmaState.value.copy(toastMesaj = "Taslak bulunamadı.")
            } else {
                if (it["baslik"] == "hataDurumuİnternetiKontrolEt") {
                    _kullaniciYarHikOlusturmaState.value = _kullaniciYarHikOlusturmaState.value.copy(hata = true)
                    setToastMesaj("İnternetinizi kontrol ediniz.")
                } else {
                    setBaslik(it["baslik"]!!)
                    setHikaye(it["hikaye"]!!)
                    setToastMesaj("Taslak Bulundu")
                }
            }
            setBekleniyor(false)

        }
    }

    fun setDropdownKontrol(it:Boolean) {
        _kullaniciYarHikOlusturmaState.value = _kullaniciYarHikOlusturmaState.value.copy(dropdownKontrol = it)
    }

    fun setBaslik(it: String) {
        _kullaniciYarHikOlusturmaState.value = _kullaniciYarHikOlusturmaState.value.copy(baslik = it)
    }

    fun setHikaye(it: String) {
        _kullaniciYarHikOlusturmaState.value = _kullaniciYarHikOlusturmaState.value.copy(kullaniciYarismaHikayesi = it)
    }

    fun setToastMesaj(it:String) {
        _kullaniciYarHikOlusturmaState.value = _kullaniciYarHikOlusturmaState.value.copy(toastMesaj = it)
    }

    fun kullaniciHikayeyiTaslakKaydet() {
        setBekleniyor(true)
        firebaseRepository.kullaniciYarismasiniTaslakKaydet(
            _kullaniciYarHikOlusturmaState.value.baslik,
            _kullaniciYarHikOlusturmaState.value.kullaniciYarismaHikayesi,
            basariliMi = {
                setBekleniyor(false)
                if (it) {
                    setToastMesaj("Taslak kaydedildi")
                } else {
                    setToastMesaj("Hata! Taslak kaydedilemedi.")
                }
            }
        )
    }

    fun taslakTemizle() {
        setBekleniyor(true)
        firebaseRepository.kullaniciYarismasiniTaslakKaydet(
            "",
            "",
            basariliMi = {
                setBekleniyor(false)
            }
        )
    }

    fun kullaniciYarismaHikayesiniYoneticilereGonder(yazarYarismaHikayesi: KullaniciYarismaHikayesi) {
        setBekleniyor(true)
        firebaseRepository.kullaniciYarismaHikayesiniYoneticilereGonder(
            yazarYarismaHikayesi
        ) { basarili ->
            if (basarili) {
                _kullaniciYarHikOlusturmaState.value = _kullaniciYarHikOlusturmaState.value.copy(hata = false)
            } else {
                _kullaniciYarHikOlusturmaState.value = _kullaniciYarHikOlusturmaState.value.copy(hata = true)
            }
            setBekleniyor(false)
        }
    }

    fun adminYaDaModHikayesiniOnayla(yazarYarismaHikayesi: KullaniciYarismaHikayesi) {
        setBekleniyor(true)
        firebaseRepository.onaylananYarismayiTasi(yazarYarismaHikayesi, "adminYaDaMod") {hataDurumu->
            if (!hataDurumu) {
                _kullaniciYarHikOlusturmaState.value = _kullaniciYarHikOlusturmaState.value.copy(hata = false)
            } else {
                _kullaniciYarHikOlusturmaState.value = _kullaniciYarHikOlusturmaState.value.copy(hata = true)
            }
            setBekleniyor(false)
        }
    }


    fun setBekleniyor(it: Boolean) {
        _kullaniciYarHikOlusturmaState.value = _kullaniciYarHikOlusturmaState.value.copy(bekleniyor = it)

    }
    fun setAlertKontrol(it: Boolean) {
        _kullaniciYarHikOlusturmaState.value = _kullaniciYarHikOlusturmaState.value.copy(alertKontrol = it)
    }

    fun setGosterilecekAlert(gosterilecekAlert: String) {
        _kullaniciYarHikOlusturmaState.value = _kullaniciYarHikOlusturmaState.value.copy(gosterilecekAlert = gosterilecekAlert)
    }
}