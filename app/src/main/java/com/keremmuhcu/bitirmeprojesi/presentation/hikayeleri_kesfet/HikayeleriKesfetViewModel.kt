package com.keremmuhcu.bitirmeprojesi.presentation.hikayeleri_kesfet

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.keremmuhcu.bitirmeprojesi.data.FirebaseRepository
import com.keremmuhcu.bitirmeprojesi.data.models.Hikaye
import com.keremmuhcu.bitirmeprojesi.data.models.HikayeBolum

class HikayeleriKesfetViewModel:ViewModel() {
    private val firebaseRepository = FirebaseRepository()
    private val _hikayeleriKesfetState = mutableStateOf(HikayeleriKesfetState())
    val hikayeleriKesfetState:State<HikayeleriKesfetState> = _hikayeleriKesfetState

    private val _hikayelerListesi = mutableStateOf<List<Hikaye>>(emptyList())
    val hikayelerListesi:State<List<Hikaye>> = _hikayelerListesi

    private val _girisBolumuListesi = mutableStateOf<List<HikayeBolum>>(emptyList())
    val girisBolumuListesi:State<List<HikayeBolum>> = _girisBolumuListesi

    fun rastgeleHikayelerGetir() {
        setBekleniyor(true)
        firebaseRepository.rastgeleHikayeleriGetir { basariDurumu, hikayeler, girisler ->
            if (basariDurumu) {
                _hikayelerListesi.value = hikayeler
                _girisBolumuListesi.value = girisler
            }
            setBekleniyor(false)

        }
    }

    fun setBekleniyor(durum:Boolean) {
        _hikayeleriKesfetState.value = _hikayeleriKesfetState.value.copy(bekleniyor = durum)
    }

    fun setToastMesaj(toastMesaj : String) {
        _hikayeleriKesfetState.value = _hikayeleriKesfetState.value.copy(toastMesaj = toastMesaj)
    }
}