package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.yarisma_hikayesi

import androidx.lifecycle.ViewModel
import com.keremmuhcu.bitirmeprojesi.data.FirebaseRepository
import com.keremmuhcu.bitirmeprojesi.data.models.KullaniciYarismaHikayesi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class YarismaHikayesiViewModel:ViewModel() {
    private val firebaseRepository = FirebaseRepository()

    private val _yarismaHikayeState = MutableStateFlow(YarismaHikayesiState())
    val yarismaHikayesiState:StateFlow<YarismaHikayesiState> = _yarismaHikayeState

    init {
        yarismayiGetir()
    }

    fun yarismayiGetir() {
        firebaseRepository.sonYarismayiGetir {
            _yarismaHikayeState.value = _yarismaHikayeState.value.copy(yarismaHikayesi = it)
            _yarismaHikayeState.value = _yarismaHikayeState.value.copy(anaHikaye = it.anaHikaye!!)
        }
    }


    fun kullaniciYarismaGondermisMi(yazarRol: String) {
        firebaseRepository.kullaniciYarHikDurumunuGetir(yazarRol) {
            if (it == "islemBasarisiz") {
                // hata mesajı gösterilebilir.
            } else {
                setKullaniciYarHikDurum(it)
            }
        }
    }

    fun yarismayiSil(silindiMi: (Boolean) -> Unit) {
        firebaseRepository.yarismayiSil {
            if (it){
                _yarismaHikayeState.value = _yarismaHikayeState.value.copy(toastMesaj = "Yarışma Silindi")
                silindiMi(true)
            } else {
                _yarismaHikayeState.value = _yarismaHikayeState.value.copy(toastMesaj = "Hata! Silinemedi")
                silindiMi(false)
            }
        }
    }

    fun kullanicininHikayesiniGetir(yazarRol:String) {
        firebaseRepository.kullanicininYarismaHikayesiniGetir(yazarRol) {
            if (it.kullaniciYarismaHikayesi != null) {
                setKullaniciYarismaHikayesiBaslik(it.kullaniciYarismaHikayeBaslik!!)
                setKullaniciYarismaHikayesi(it.kullaniciYarismaHikayesi)
                setHikayeyiGormeKontrolu(true)
            } else {
                setToastMesaj("İnternetinizi Kontrol Edin")
            }
        }
    }

    fun taslakTemizle() {
        setBekleniyor(true)
        firebaseRepository.kullaniciYarismasiniTaslakKaydet(
            "",
            "",
            basariliMi = {
                setBekleniyor(false)
            }
        )
    }

    fun setToastMesaj(toastMesaj: String) {
        _yarismaHikayeState.value = _yarismaHikayeState.value.copy(toastMesaj = toastMesaj)
    }


    fun yarismayiGuncelle() {
        firebaseRepository.yarismayiGuncelle(_yarismaHikayeState.value.anaHikaye)
    }

    fun setAnaHikaye(it:String) {
        _yarismaHikayeState.value = _yarismaHikayeState.value.copy(anaHikaye = it)
    }

    fun setDuzenleModu(it:Boolean) {
        _yarismaHikayeState.value = _yarismaHikayeState.value.copy(duzenleModu = it)
    }


    fun setAlertKontrol(it:Boolean) {
        _yarismaHikayeState.value = _yarismaHikayeState.value.copy(alertKontrol = it)
    }

    fun setDropdownKontrol(it:Boolean) {
        _yarismaHikayeState.value = _yarismaHikayeState.value.copy(dropdownKontrol = it)
    }


    fun setGosterilecekAlert(alert: String) {
        _yarismaHikayeState.value = _yarismaHikayeState.value.copy(gosterilecekAlert = alert)
    }

    fun setKullaniciYarHikDurum(durum:String) {
        _yarismaHikayeState.value = _yarismaHikayeState.value.copy(kullaniciYarHikDurum = durum)
    }

    fun setKullaniciYarismaHikayesiBaslik(baslik: String) {
        _yarismaHikayeState.value = _yarismaHikayeState.value.copy(kullaniciYarismaHikayesiBaslik = baslik)
    }

    fun setKullaniciYarismaHikayesi(baslik: String) {
        _yarismaHikayeState.value = _yarismaHikayeState.value.copy(kullaniciYarismaHikayesi = baslik)
    }

    fun setHikayeyiGormeKontrolu(durum:Boolean) {
        _yarismaHikayeState.value = _yarismaHikayeState.value.copy(hikayeyiGormeKontrolu = durum)
    }

    fun setBekleniyor(durum:Boolean) {
        _yarismaHikayeState.value = _yarismaHikayeState.value.copy(bekleniyor = durum)
    }


}