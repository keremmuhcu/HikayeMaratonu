package com.keremmuhcu.bitirmeprojesi.presentation.profil.profil_ziyaret

import androidx.lifecycle.ViewModel
import com.keremmuhcu.bitirmeprojesi.data.FirebaseRepository
import com.keremmuhcu.bitirmeprojesi.data.models.Yazar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfilZiyaretViewModel:ViewModel() {
    private val firebaseRepository = FirebaseRepository()
    private val _profilZiyaretState = MutableStateFlow(ProfilZiyaretState())
    val profilZiyaretState: StateFlow<ProfilZiyaretState> = _profilZiyaretState

    fun yazarBilgileriniAl(email:String,basariliMi:(Boolean) -> Unit) {
        setBekleniyor(true)
        firebaseRepository.aktifYazarBilgileriniAl {aktifYazar->
            if (aktifYazar.email.isNotEmpty()) {
                setAktifYazar(aktifYazar)
                firebaseRepository.yazarBilgileriniAl(email) {yazar->
                    if (yazar.email.isNotEmpty()) {
                        setYazar(yazar)
                        setBekleniyor(false)
                        basariliMi(true)
                    } else {
                        basariliMi(false)
                    }
                }
            } else {
                basariliMi(false)
            }
        }
    }

    fun yazarRoluGuncelle(rol:String) {
        setBekleniyor(true)
        firebaseRepository.yazarRoluGuncelle(_profilZiyaretState.value.yazar.email, rol) {basariDurumu->
            if (basariDurumu) {
                setToastMesaj("Başarıyla güncellendi")
                firebaseRepository.yazarBilgileriniAl(_profilZiyaretState.value.yazar.email) {yazar->
                    if (yazar.email.isNotEmpty()) {
                        setYazar(yazar)
                    } else {
                        setToastMesaj("Sayfa yüklenemedi.")
                    }
                }
            } else {
                setToastMesaj("Başarısız")
            }
            setBekleniyor(false)
        }
    }

    fun setToastMesaj(toastMesaj:String) {
        _profilZiyaretState.value = _profilZiyaretState.value.copy(toastMesaj = toastMesaj)
    }

    fun setYazar(yazar: Yazar) {
        _profilZiyaretState.value = _profilZiyaretState.value.copy(yazar = yazar)
    }

    fun setAktifYazar(yazar: Yazar) {
        _profilZiyaretState.value = _profilZiyaretState.value.copy(aktifYazar = yazar)
    }

    fun setBekleniyor(durum: Boolean) {
        _profilZiyaretState.value = _profilZiyaretState.value.copy(bekleniyor = durum)
    }

    fun setDropdownGoster(durum: Boolean) {
        _profilZiyaretState.value = _profilZiyaretState.value.copy(dropdownGoster = durum)
    }
}