package com.keremmuhcu.bitirmeprojesi.presentation.giris

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.keremmuhcu.bitirmeprojesi.data.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GirisViewModel: ViewModel() {
    private val firebaseRepository = FirebaseRepository()
    private val _girisState = MutableStateFlow(GirisState())
    val girisState: StateFlow<GirisState> = _girisState

    fun girisYap(
        girisBasariliMi:(Boolean) -> Unit
    ) {
        val kullaniciAdiVeyaEmail = _girisState.value.kullaniciAdiVeyaEmail.trim()
        val sifre = _girisState.value.sifre

        if (kullaniciAdiVeyaEmail.isNotEmpty() && sifre.isNotEmpty()) {
            firebaseRepository.girisYap(kullaniciAdiVeyaEmail, sifre) {
                if (it) {
                    _girisState.value = _girisState.value.copy(toastMesaj = "Giriş Başarılı")
                    _girisState.value = _girisState.value.copy(girisBasariliMi = true)
                } else {
                    _girisState.value = _girisState.value.copy(toastMesaj = "Giriş Başarısız")
                    _girisState.value = _girisState.value.copy(girisBasariliMi = false)
                }
                girisBasariliMi(_girisState.value.girisBasariliMi)

            }
        } else {
            _girisState.value = _girisState.value.copy(toastMesaj = "Değerleri Eksiksiz Giriniz!")
            _girisState.value = _girisState.value.copy(girisBasariliMi = false)
            girisBasariliMi(_girisState.value.girisBasariliMi)
        }
    }

    fun setKullaniciAdiVeyaEmail(email: String) {
        _girisState.value = _girisState.value.copy(kullaniciAdiVeyaEmail = email)
    }

    fun setSifre(sifre: String) {
        _girisState.value = _girisState.value.copy(sifre = sifre)
    }

    fun setToastMesaj(toastMesaj: String) {
        _girisState.value = _girisState.value.copy(toastMesaj = toastMesaj)
    }

    fun setBekleniyor(durum: Boolean) {
        _girisState.value = _girisState.value.copy(bekleniyor = durum)
    }
}