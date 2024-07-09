package com.keremmuhcu.bitirmeprojesi.presentation.biten_yarismalar

import androidx.lifecycle.ViewModel
import com.keremmuhcu.bitirmeprojesi.data.FirebaseRepository
import com.keremmuhcu.bitirmeprojesi.data.models.YarismaHikayesi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BitenYarismalarViewModel:ViewModel() {
    private val firebaseRepository = FirebaseRepository()
    private val _bitenYarismalarListesi = MutableStateFlow<List<YarismaHikayesi>>(emptyList())
    val bitenYarismalarListesi: StateFlow<List<YarismaHikayesi>> = _bitenYarismalarListesi

    private val _bitenYarismalarState = MutableStateFlow(BitenYarismalarState())
    val bitenYarismalarState:StateFlow<BitenYarismalarState> = _bitenYarismalarState

    init {
        bitenYarismalariGetir()
    }

    fun bitenYarismalariGetir() {
        setBekleniyor(true)
        firebaseRepository.bitenYarismalariGetir {basariliMi, liste->
            if (basariliMi) {
                _bitenYarismalarListesi.value = liste
            } else {
                setToastMesaj("İnternetinizi kontrol ediniz.")
            }
            setBekleniyor(false)
        }
    }

    fun setToastMesaj(toastMesaj: String) {
        _bitenYarismalarState.value = _bitenYarismalarState.value.copy(toastMesaj = toastMesaj)
    }

    fun setBekleniyor(durum: Boolean) {
        _bitenYarismalarState.value = _bitenYarismalarState.value.copy(bekleniyor = durum)
    }

    fun setVerilerBasariylaAlindiMi(durum: Boolean) {
        _bitenYarismalarState.value = _bitenYarismalarState.value.copy(verilerBasariylaAlindiMi = durum)
    }

    fun setFormatliTarih(tarih: String) {
        _bitenYarismalarState.value = _bitenYarismalarState.value.copy(formatliTarih = tarih)
    }


}