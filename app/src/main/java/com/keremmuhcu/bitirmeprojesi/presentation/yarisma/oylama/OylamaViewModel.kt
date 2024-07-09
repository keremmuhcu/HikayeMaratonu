package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.oylama

import androidx.lifecycle.ViewModel
import com.keremmuhcu.bitirmeprojesi.data.FirebaseRepository
import com.keremmuhcu.bitirmeprojesi.data.models.KullaniciYarismaHikayesi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OylamaViewModel:ViewModel() {
    private val firebaseRepository = FirebaseRepository()
    //var onaylanmisYarismaHikayeleri: StateFlow<List<KullaniciYarismaHikayesi>> = firebaseRepository.onaylanmisKullaniciYarismaHikayeleri
    private val _onaylanmisKullaniciYarismaHikayeleri =  MutableStateFlow<List<KullaniciYarismaHikayesi>>(emptyList())
    private val _onaylanmisYarismaHikayeleriSiralamaYedek =  MutableStateFlow<List<KullaniciYarismaHikayesi>>(emptyList())
    val onaylanmisYarismaHikayeleri: StateFlow<List<KullaniciYarismaHikayesi>> = _onaylanmisKullaniciYarismaHikayeleri

    private val _oylamaState = MutableStateFlow(OylamaState())
    val oylamaState:StateFlow<OylamaState> = _oylamaState

    init {
        onaylanmisYarismaHikayeleriniGetir()
    }

    fun onaylanmisYarismaHikayeleriniGetir() {
        setBekleniyor(true)
        firebaseRepository.onaylanmisKullaniciYarismaHikayeleriniGetir("son"){hata,liste ->
            if (hata) {
                setToastMesaj("İnternetinizi Kontrol Ediniz")
            } else {
                _onaylanmisKullaniciYarismaHikayeleri.value = liste
                _onaylanmisYarismaHikayeleriSiralamaYedek.value = liste
                yarismalariSirala("azalan")
                if (liste.count() > 3) {
                    setKatilimSaglanmisMi(true)
                } else {
                    setKatilimSaglanmisMi(false)
                }
                yazarOyVermisMi()
            }
            setBekleniyor(false)
        }
    }


    fun hikayeYazarinMi(email: String) {
        if (firebaseRepository.aktifKullanici?.email == email) {
            setHikayeYazarinMi(true)
        } else {
            setHikayeYazarinMi(false)
        }
    }

    fun yarismalariSirala(yon:String) {
        _onaylanmisKullaniciYarismaHikayeleri.value = _onaylanmisYarismaHikayeleriSiralamaYedek.value
        when(yon) {
            "artan" -> {
                _onaylanmisKullaniciYarismaHikayeleri.value = onaylanmisYarismaHikayeleri.value.sortedWith(
                    compareBy <KullaniciYarismaHikayesi> {
                        it.oylar
                    }.thenByDescending {
                        it.olusturmaTarihi
                    }
                )

            }
            "azalan" -> {
                _onaylanmisKullaniciYarismaHikayeleri.value = onaylanmisYarismaHikayeleri.value.sortedWith(
                    compareByDescending <KullaniciYarismaHikayesi> {
                        it.oylar
                    }.thenBy {
                        it.olusturmaTarihi
                    }
                )
            }
            "hikayem" -> {
                onaylanmisYarismaHikayeleri.value.map {

                    if (it.yazarEmail == firebaseRepository.aktifKullanici?.email) {
                        _onaylanmisKullaniciYarismaHikayeleri.value = listOf(it)
                    }
                }

                if (onaylanmisYarismaHikayeleri.value.count() != 1) {
                    setToastMesaj("Hikaye yazmamışsınız.")
                }

            }
            else -> {
                if (_oylamaState.value.yazarOyVermisMi) {
                    onaylanmisYarismaHikayeleri.value.map {
                        if (it.yazarEmail == _oylamaState.value.verilenOy) {
                            _onaylanmisKullaniciYarismaHikayeleri.value = listOf(it)
                        }
                    }
                }
            }
        }
    }

    fun yarismayiSil(bittiMi:(Boolean) -> Unit) {
        firebaseRepository.yarismayiSil {basariliMi->
            if (!basariliMi) {
                setToastMesaj("Yarışma silinemedi. Bağlantı hatası!")
            }
            bittiMi(true)

        }
    }

    fun yarismayaOyVer(kullaniciYarismaHikayesi: KullaniciYarismaHikayesi) {
        firebaseRepository.yarismayaOyVer(kullaniciYarismaHikayesi) {

        }
    }

    fun yazarOyVermisMi() {
        firebaseRepository.yazarOyVermisMi {
            println("işlem sonucu: ${it}")
            if (it == false) {
                setYazarOyVermisMi(false)
            } else if (it == "Hata") {
                //setYazarOyVermisMi(false)
            } else {
                setVerilenOy(it.toString())
                println("Verilen Oy: $it")
                setYazarOyVermisMi(true)
            }
        }
    }


    fun setDropdownKontrol(durum: Boolean) {
        _oylamaState.value = _oylamaState.value.copy(dropdownKontrol = durum)
    }

    fun setHikayeYazarinMi(durum: Boolean) {
        _oylamaState.value = _oylamaState.value.copy(hikayeYazarinMi = durum)
    }

    fun setToastMesaj(mesaj: String) {
        _oylamaState.value = _oylamaState.value.copy(toastMesaj = mesaj)
    }

    fun setBekleniyor(durum:Boolean) {
        _oylamaState.value = _oylamaState.value.copy(bekleniyor = durum)
    }

    fun setTiklananHikayeKontrol(durum:Boolean) {
        _oylamaState.value = _oylamaState.value.copy(tiklananHikayeKontrol = durum)
    }

    fun setSecilenHikaye(kullaniciYarismaHikayesi: KullaniciYarismaHikayesi) {
        _oylamaState.value = _oylamaState.value.copy(secilenHikaye = kullaniciYarismaHikayesi)
    }

    fun setKatilimSaglanmisMi(durum:Boolean) {
        _oylamaState.value = _oylamaState.value.copy(katilimSaglanmisMi = durum)
    }

    fun setYazarOyVermisMi(durum: Boolean) {
        _oylamaState.value = _oylamaState.value.copy(yazarOyVermisMi = durum)
    }

    fun setInfoButonunaTiklandiMi(durum: Boolean) {
        _oylamaState.value = _oylamaState.value.copy(infoButonunaTiklandiMi = durum)
    }


    fun setVerilenOy(verilenOy: String) {
        _oylamaState.value = _oylamaState.value.copy(verilenOy = verilenOy)
    }
}