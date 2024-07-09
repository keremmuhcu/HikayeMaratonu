package com.keremmuhcu.bitirmeprojesi.presentation.kutuphane

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.keremmuhcu.bitirmeprojesi.data.FirebaseRepository
import com.keremmuhcu.bitirmeprojesi.data.models.Hikaye

class KutuphaneViewModel:ViewModel() {
    private val firebaseRepository = FirebaseRepository()
    private val _kutuphaneState = mutableStateOf(KutuphaneState())
    val kutuphaneState:State<KutuphaneState> = _kutuphaneState

    private val _hikayeListesi = mutableStateOf<List<Hikaye>>(emptyList())
    val hikayeListesi:State<List<Hikaye>> = _hikayeListesi

    fun hikayelerimiGetir() {
        setBekleniyor(true)
        firebaseRepository.kutuphanedekiHikayeleriGetir { basariDurumu, liste ->
            if (basariDurumu) {
                _hikayeListesi.value = liste.sortedByDescending {
                    it.okunmaTarihi
                }
            }
            setBekleniyor(false)
        }
    }

    fun setBekleniyor(durum:Boolean) {
        _kutuphaneState.value = _kutuphaneState.value.copy(bekleniyor = durum)
    }

    fun setToastMesaj(toastMesaj:String) {
        _kutuphaneState.value = _kutuphaneState.value.copy(toastMesaj = toastMesaj)
    }

}