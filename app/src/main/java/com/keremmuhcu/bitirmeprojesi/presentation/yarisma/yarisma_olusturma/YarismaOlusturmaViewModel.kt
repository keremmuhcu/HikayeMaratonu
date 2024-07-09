package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.yarisma_olusturma

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.keremmuhcu.bitirmeprojesi.data.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class YarismaOlusturmaViewModel:ViewModel() {
    private val firebaseRepository = FirebaseRepository()

    private val _yarismaOlusturmaState = MutableStateFlow(YarismaOlusturmaState())
    val yarismaOlusturmaState: StateFlow<YarismaOlusturmaState> = _yarismaOlusturmaState

    fun yarismaHikayesiOlustur(
        yarismaOlusturulduMu:(Boolean) -> Unit
    ) {
        if (degerleriKontrolEt()) {
            firebaseRepository.yarismaHikayesiOlustur(
                _yarismaOlusturmaState.value.anaHikaye,
                _yarismaOlusturmaState.value.kacGunYarisilacak.toInt()
            ) {
                if (it) {
                    _yarismaOlusturmaState.value = _yarismaOlusturmaState.value.copy(toastMesaj = "Yarışma Başladı.")
                    yarismaOlusturulduMu(true)
                } else {
                    _yarismaOlusturmaState.value = _yarismaOlusturmaState.value.copy(toastMesaj = "Hata!")
                    yarismaOlusturulduMu(false)
                }
            }
        } else {
            _yarismaOlusturmaState.value = _yarismaOlusturmaState.value.copy(toastMesaj = "Değerleri Eksiksiz Giriniz.")
            yarismaOlusturulduMu(false)
        }
    }

    fun setHikaye(hikaye: String) {
        _yarismaOlusturmaState.value = _yarismaOlusturmaState.value.copy(anaHikaye = hikaye)
    }

    private fun degerleriKontrolEt(): Boolean {
        val hikaye = _yarismaOlusturmaState.value.anaHikaye
        val kacGunYarisilicak = _yarismaOlusturmaState.value.kacGunYarisilacak

        if (hikaye.isNotEmpty() && kacGunYarisilicak.isNotEmpty()) {
            return true
        }
        else {
            return false
        }

    }

    fun alertAcKapa(kontrol: Boolean) {
        _yarismaOlusturmaState.value = _yarismaOlusturmaState.value.copy(alertAcilisKontrol = kontrol)
    }

    fun setKacGunYarisilacak(it: String) {
        if (it.length < 2 && it != "0")
            _yarismaOlusturmaState.value = _yarismaOlusturmaState.value.copy(kacGunYarisilacak = it)
    }

    fun setGosterilecekAlert(alert: String) {
        _yarismaOlusturmaState.value = _yarismaOlusturmaState.value.copy(gosterilecekAlert = alert)
    }

    fun setBekleniyor(durum: Boolean) {
        _yarismaOlusturmaState.value = _yarismaOlusturmaState.value.copy(bekleniyor = durum)
    }

    fun setToastMesaj(mesaj:String) {
        _yarismaOlusturmaState.value = _yarismaOlusturmaState.value.copy(toastMesaj = mesaj)
    }
}