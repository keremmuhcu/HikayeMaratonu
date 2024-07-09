package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.onaylama

import androidx.lifecycle.ViewModel
import com.keremmuhcu.bitirmeprojesi.data.FirebaseRepository
import com.keremmuhcu.bitirmeprojesi.data.models.KullaniciYarismaHikayesi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OnaylamaViewModel:ViewModel() {
    private val firebaseRepository = FirebaseRepository()

    private val _onaylamaState = MutableStateFlow(OnaylamaState())
    val onaylamaState:StateFlow<OnaylamaState> = _onaylamaState

    var bekleyenYarismaHikayeleriListesi: StateFlow<List<KullaniciYarismaHikayesi>> = firebaseRepository.bekleyenYarismaHikayeleriListesi


    init {
        setBekleniyor(true)
        firebaseRepository.onaylanacakYarismaHikayeleriniGetir{hata ->
            if (hata) {
                setToastMesaj("İnternetinizi Kontrol Ediniz")
            } else {
                bekleyenYarismaHikayeleriListesi = firebaseRepository.bekleyenYarismaHikayeleriListesi
            }
            setBekleniyor(false)
        }
    }

    fun yarismayiOnayla(yazarYarismaHikayesi: KullaniciYarismaHikayesi) {
        setBekleniyor(true)
        firebaseRepository.yarismayiOnayla(yazarYarismaHikayesi) {hataDurumu->
            if (!hataDurumu) {
                setToastMesaj("Başarılı.")
            } else {
                setToastMesaj("Hata!")
            }
            setBekleniyor(false)
        }
    }

    fun yarismayiReddet(yazarYarismaHikayesi: KullaniciYarismaHikayesi) {
        setBekleniyor(true)
        firebaseRepository.yarismayiReddet(yazarYarismaHikayesi) {hataDurumu->
            if (!hataDurumu) {
                setToastMesaj("Başarılı.")
            } else {
                setToastMesaj("Hata!")
            }
            setBekleniyor(false)
        }
    }

    fun setBekleniyor(durum: Boolean) {
        _onaylamaState.value = _onaylamaState.value.copy(bekleniyor = durum)
    }

    fun setToastMesaj(mesaj: String) {
        _onaylamaState.value = _onaylamaState.value.copy(toastMesaj = mesaj)
    }

    fun setOnaylamaPencereKontrol(durum: Boolean) {
        _onaylamaState.value = _onaylamaState.value.copy(onaylamaPencereKontrol = durum)
    }

    fun setSecilenHikaye(kullaniciYarismaHikayesi: KullaniciYarismaHikayesi) {
        _onaylamaState.value = _onaylamaState.value.copy(secilenHikaye = kullaniciYarismaHikayesi)
    }
}