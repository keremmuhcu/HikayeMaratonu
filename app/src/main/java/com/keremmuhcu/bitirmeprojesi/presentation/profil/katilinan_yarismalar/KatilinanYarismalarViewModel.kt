package com.keremmuhcu.bitirmeprojesi.presentation.profil.katilinan_yarismalar

import androidx.lifecycle.ViewModel
import com.keremmuhcu.bitirmeprojesi.data.FirebaseRepository
import com.keremmuhcu.bitirmeprojesi.data.models.KullaniciYarismaHikayesi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class KatilinanYarismalarViewModel:ViewModel() {
    private val firebaseRepository = FirebaseRepository()

    private val _katilinanYarismalarState = MutableStateFlow(KatilinanYarismalarState())
    val katilinanYarismalarState:StateFlow<KatilinanYarismalarState> = _katilinanYarismalarState

    private val _yazarinYarismaHikayleriListesi = MutableStateFlow<List<KullaniciYarismaHikayesi>>(emptyList())
    private val _yazarinYarismaHikayleriListesiYedek = MutableStateFlow<List<KullaniciYarismaHikayesi>>(emptyList())
    val yazarinYarismaHikayleriListesi:StateFlow<List<KullaniciYarismaHikayesi>> = _yazarinYarismaHikayleriListesi

    fun yazarinKatildigiYarismalariGetir(email:String,basariliMi:(Boolean) -> Unit) {
        setBekleniyor(true)
        firebaseRepository.kullanicininYarismaHikayeleriniAl(email) { basariDurumu, hikayeListesi ->
            if (basariDurumu) {
                _yazarinYarismaHikayleriListesi.value = hikayeListesi
                _yazarinYarismaHikayleriListesiYedek.value = hikayeListesi
                sirala()
                basariliMi(true)
                setBekleniyor(false)
            } else {
                basariliMi(false)
            }
        }
    }

    fun tiklananHikayeninAnaHikayesiniGetir(basariliMi: (Boolean) -> Unit) {
        firebaseRepository
            .tiklananHikayeninAnaHikayesiniGetir(
                _katilinanYarismalarState.value.secilenHikaye
            ) { basariDurumu,anaHikaye->
                if (basariDurumu) {
                    setTiklananYarismaAnaHikaye(anaHikaye)
                    basariliMi(true)
                } else {
                    basariliMi(false)
                }
            }
    }

    fun sirala() {
        println("Siralama Yönü: ${_katilinanYarismalarState.value.siralamaYonu} ve Siralama Kriteri: ${_katilinanYarismalarState.value.siralamaKriteri}")
        _yazarinYarismaHikayleriListesi.value = _yazarinYarismaHikayleriListesiYedek.value
        _yazarinYarismaHikayleriListesi.value = _yazarinYarismaHikayleriListesi.value.sortedWith(
            if (_katilinanYarismalarState.value.siralamaYonu == "artan") {
                if (_katilinanYarismalarState.value.siralamaKriteri == "derece") {
                    compareByDescending <KullaniciYarismaHikayesi> {
                            it.derece

                    }.thenBy {
                        it.yarismaTarihi
                    }
                } else {
                    compareBy <KullaniciYarismaHikayesi> {
                        when(_katilinanYarismalarState.value.siralamaKriteri) {
                            "oy" -> it.oylar
                            else -> it.yarismaTarihi
                        }

                    }.thenBy {
                        it.yarismaTarihi
                    }
                }

            } else {
                if (_katilinanYarismalarState.value.siralamaKriteri == "derece") {
                    compareBy <KullaniciYarismaHikayesi> {
                        it.derece

                    }.thenByDescending {
                        it.yarismaTarihi
                    }
                } else {
                    compareByDescending <KullaniciYarismaHikayesi> {
                        when(_katilinanYarismalarState.value.siralamaKriteri) {
                            "oy" -> it.oylar
                            else -> it.yarismaTarihi
                        }

                    }.thenBy {
                        it.yarismaTarihi
                    }
                }
            }
        )
    }

    fun setBekleniyor(durum:Boolean) {
        _katilinanYarismalarState.value = _katilinanYarismalarState.value.copy(bekleniyor = durum)
    }

    fun setToastMesaj(toastMesaj: String) {
        _katilinanYarismalarState.value = _katilinanYarismalarState.value.copy(toastMesaj = toastMesaj)
    }

    fun setDropdownGoster(durum: Boolean) {
        _katilinanYarismalarState.value = _katilinanYarismalarState.value.copy(dropdownGoster = durum)
    }

    fun setSiralamaYonu(yon: String) {
        _katilinanYarismalarState.value = _katilinanYarismalarState.value.copy(siralamaYonu = yon)
    }

    fun setSiralamaKriteri(kriter: String) {
        _katilinanYarismalarState.value = _katilinanYarismalarState.value.copy(siralamaKriteri = kriter)
    }

    fun setDialogKontrol(durum: Boolean) {
        _katilinanYarismalarState.value = _katilinanYarismalarState.value.copy(dialogKontrol = durum)
    }

    fun setSecilenHikaye(hikaye: KullaniciYarismaHikayesi) {
        _katilinanYarismalarState.value = _katilinanYarismalarState.value.copy(secilenHikaye = hikaye)
    }

    fun setTiklananYarismaAnaHikaye(anaHikaye:String) {
        _katilinanYarismalarState.value = _katilinanYarismalarState.value.copy(tiklananYarismaAnaHikaye = anaHikaye)
    }
}
