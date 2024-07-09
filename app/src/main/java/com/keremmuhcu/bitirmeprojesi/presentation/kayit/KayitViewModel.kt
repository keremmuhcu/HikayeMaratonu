package com.keremmuhcu.bitirmeprojesi.presentation.kayit

import androidx.lifecycle.ViewModel
import com.keremmuhcu.bitirmeprojesi.data.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class KayitViewModel: ViewModel() {
    private val firebaseRepository = FirebaseRepository()
    private val _kayitState = MutableStateFlow(KayitState())
    val kayitState: StateFlow<KayitState> = _kayitState

    fun kayitOl(
        kayitBasariliMi:(Boolean) -> Unit
    ) {
        if (degerleriKontrolEt()) {
            val kullaniciAdi = _kayitState.value.kullaniciAdi.trim().lowercase()
            val email = _kayitState.value.email.trim().lowercase()
            val sifre = _kayitState.value.sifre.trim()
            firebaseRepository.kayitOl(
                kullaniciAdi,
                email,
                sifre
            ) { kullaniciAdiAlinmis ->
                when(kullaniciAdiAlinmis) {
                    "Kullanıcı Adı Alınmamış" -> {
                        _kayitState.value = _kayitState.value.copy(toastMesaj = "Kayıt Başarılı!")
                        _kayitState.value = _kayitState.value.copy(kayitBasariliMi = true)
                    }

                    "Kullanıcı Adı Alınmış" -> {
                        _kayitState.value = _kayitState.value.copy(toastMesaj = "Kullanıcı adı alınmış.")
                        _kayitState.value = _kayitState.value.copy(kayitBasariliMi = false)
                    }

                    "Email Internet Kontrol" -> {
                        _kayitState.value = _kayitState.value.copy(toastMesaj = "Emaili veya interneti kontrol ediniz.")
                        _kayitState.value = _kayitState.value.copy(kayitBasariliMi = false)

                    }
                }

                kayitBasariliMi(_kayitState.value.kayitBasariliMi)
            }
        } else {
            kayitBasariliMi(_kayitState.value.kayitBasariliMi)
        }

    }

    private fun degerleriKontrolEt(): Boolean {
        _kayitState.value = _kayitState.value.copy(kayitBasariliMi = false)

        if (_kayitState.value.kullaniciAdi.isEmpty() || _kayitState.value.kullaniciAdi.length < 3) {
            _kayitState.value = _kayitState.value.copy(toastMesaj = "Kullanıcı adı en az 3 karakter olmalıdır.")
            return false
        }

        if (_kayitState.value.email.isEmpty() || !_kayitState.value.email.contains("@")) {
            _kayitState.value = _kayitState.value.copy(toastMesaj = "Lütfen geçerli bir email adresi giriniz.")
            return false
        } else {
            _kayitState.value = _kayitState.value.copy(email = _kayitState.value.email.trim())
        }

        if (_kayitState.value.sifre.isEmpty() || _kayitState.value.sifre.length < 8) {
            _kayitState.value = _kayitState.value.copy(toastMesaj = "Şifre en az 8 karakter olmalıdır.")
            return false
        }

        _kayitState.value = _kayitState.value.copy(kayitBasariliMi = true)
        return true
    }

    fun setKullaniciAdi(kullaniciAdi: String) {
        _kayitState.value = _kayitState.value.copy(kullaniciAdi = kullaniciAdi)
    }

    fun setEmail(email: String) {
        _kayitState.value = _kayitState.value.copy(email = email)
    }

    fun setSifre(sifre: String) {
        _kayitState.value = _kayitState.value.copy(sifre = sifre)
    }

    fun setToastMesaj(toastMesaj:String) {
        _kayitState.value = _kayitState.value.copy(toastMesaj = toastMesaj)
    }

    fun setBekleniyor(durum: Boolean) {
        _kayitState.value = _kayitState.value.copy(bekleniyor = durum)
    }
}