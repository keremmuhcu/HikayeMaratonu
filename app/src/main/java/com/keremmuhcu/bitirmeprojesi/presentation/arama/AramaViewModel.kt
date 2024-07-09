package com.keremmuhcu.bitirmeprojesi.presentation.arama

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.keremmuhcu.bitirmeprojesi.data.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AramaViewModel:ViewModel() {
    private val firebaseRepository = FirebaseRepository()
    private val _aramaState = mutableStateOf(AramaState())
    val aramaState: State<AramaState> = _aramaState

    fun kullaniciAdiVarMi(varMiveAktifKullaniciMi:(Boolean,Boolean) -> Unit) {
        firebaseRepository.kullaniciAdiAlinmisMi(_aramaState.value.sorgu) {alinmisMi,email->
            if (alinmisMi) {
                setEmail(email!!)
                if (firebaseRepository.aktifKullanici?.email == _aramaState.value.email) {
                    varMiveAktifKullaniciMi(true, true)
                } else {
                    varMiveAktifKullaniciMi(true, false)
                }
            } else {
                varMiveAktifKullaniciMi(false, false)
                setToastMesaj("Eşleşme bulunamadı.")
            }

        }
    }

    fun setSorgu(sorgu:String) {
        _aramaState.value = _aramaState.value.copy(sorgu = sorgu)
    }

    fun setAktiflik(durum:Boolean) {
        _aramaState.value = _aramaState.value.copy(aktiflik = durum)
    }

    fun setBekleniyor(durum:Boolean) {
        _aramaState.value = _aramaState.value.copy(bekleniyor = durum)
    }

    fun setToastMesaj(toastMesaj:String) {
        _aramaState.value = _aramaState.value.copy(toastMesaj = toastMesaj)
    }

    fun setEmail(email:String) {
        _aramaState.value = _aramaState.value.copy(email = email)
    }
}