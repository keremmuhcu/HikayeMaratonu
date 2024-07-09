package com.keremmuhcu.bitirmeprojesi.presentation.profil

import androidx.lifecycle.ViewModel
import com.keremmuhcu.bitirmeprojesi.data.FirebaseRepository
import com.keremmuhcu.bitirmeprojesi.data.models.KullaniciYarismaHikayesi
import com.keremmuhcu.bitirmeprojesi.data.models.Yazar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfilViewModel:ViewModel() {
    private val firebaseRepository = FirebaseRepository()
    private val _profilState = MutableStateFlow(ProfilState())
    val profilState: StateFlow<ProfilState> = _profilState
    private val _yazarinYarismaHikayleriListesi = MutableStateFlow<List<KullaniciYarismaHikayesi>>(emptyList())
    val yazarinYarismaHikayleriListesi:StateFlow<List<KullaniciYarismaHikayesi>> = _yazarinYarismaHikayleriListesi

    fun cikisYap(
        cikisBasariliMi:(Boolean) -> Unit
    ) {
        firebaseRepository.cikisYap {sonuc ->
            if (sonuc) {
                _profilState.value = profilState.value.copy(toastMesaj = "Çıkış Başarılı")
                cikisBasariliMi(true)
            } else {
                _profilState.value = profilState.value.copy(toastMesaj = "Çıkış Başarısız")
                cikisBasariliMi(false)
            }

        }
    }
    /*fun aktifKullanicininYarismaHikayeleriniAl(basariliMi:(Boolean) -> Unit) {
        firebaseRepository.kullanicininYarismaHikayeleriniAl("aktif") { basariDurumu, hikayeListesi ->
            if (basariDurumu) {
                _yazarinYarismaHikayleriListesi.value = hikayeListesi
                basariliMi(true)
            } else {
                basariliMi(false)
            }
        }
    }*/

    fun yazarBilgileriniAl(basariliMi:(Boolean) -> Unit) {
        setBekleniyor(true)
        firebaseRepository.aktifYazarBilgileriniAl { yazar ->
            if (yazar.email.isEmpty()) {
                basariliMi(false)
            } else {
                setYazar(yazar)
                basariliMi(true)
                setBekleniyor(false)
            }
        }
    }

    fun setToastMesaj(toastMesaj:String) {
        _profilState.value = _profilState.value.copy(toastMesaj = toastMesaj)
    }

    fun setYazar(yazar: Yazar) {
        _profilState.value = _profilState.value.copy(yazar = yazar)
    }

    fun setBekleniyor(durum: Boolean) {
        _profilState.value = _profilState.value.copy(bekleniyor = durum)
    }
}