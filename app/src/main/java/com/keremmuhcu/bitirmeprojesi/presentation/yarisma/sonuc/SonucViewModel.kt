package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.sonuc

import androidx.lifecycle.ViewModel
import com.keremmuhcu.bitirmeprojesi.data.FirebaseRepository
import com.keremmuhcu.bitirmeprojesi.data.models.KullaniciYarismaHikayesi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapNotNull

class SonucViewModel:ViewModel() {
    private val firebaseRepository = FirebaseRepository()
    //var onaylanmisYarismaHikayeleri: StateFlow<List<KullaniciYarismaHikayesi>> = firebaseRepository.onaylanmisKullaniciYarismaHikayeleri

    private val _onaylanmisKullaniciYarismaHikayeleri =  MutableStateFlow<List<KullaniciYarismaHikayesi>>(emptyList())
    val onaylanmisYarismaHikayeleri: StateFlow<List<KullaniciYarismaHikayesi>> = _onaylanmisKullaniciYarismaHikayeleri

    private val _sonucState = MutableStateFlow(SonucState())
    val sonucState:StateFlow<SonucState> = _sonucState


    init {
        onaylanmisYarismaHikayeleriniGetir()
    }

    fun onaylanmisYarismaHikayeleriniGetir() {
        setBekleniyor(true)
        firebaseRepository.onaylanmisKullaniciYarismaHikayeleriniGetir("son"){hata,liste ->
            if (hata) {
                setToastMesaj("İnternetinizi Kontrol Ediniz")
            } else {
                _onaylanmisKullaniciYarismaHikayeleri.value = liste.sortedWith(
                    compareByDescending <KullaniciYarismaHikayesi> {
                        it.oylar
                    }.thenBy {
                        it.olusturmaTarihi
                    }
                )
                if (liste.count() > 3) {
                    setKatilimSaglanmisMi(true)
                    sirayiGetir()
                } else {
                    setKatilimSaglanmisMi(false)
                }
            }
            setBekleniyor(false)
        }
    }

    fun yarismayiBitir(basariliMi:(Boolean) -> Unit) {
        firebaseRepository.yarismayiBitir {
            if (it) {
                basariliMi(true)
            } else {
                //setToastMesaj("İnternetinizi Kontrol Ediniz.")
                basariliMi(false)
            }
        }
    }

    fun yarismayiSil(bittiMi:(Boolean) -> Unit) {
        firebaseRepository.yarismayiSil {basariliMi->
            if (!basariliMi) {
                setToastMesaj("Bağlantı hatası!")
            }
            bittiMi(true)

        }
    }


    fun setToastMesaj(mesaj:String) {
        _sonucState.value = _sonucState.value.copy(toastMesaj = mesaj)
    }

    fun setBekleniyor(durum:Boolean) {
        _sonucState.value = _sonucState.value.copy(bekleniyor = durum)
    }

    fun setHikayeDialogKontrol(durum:Boolean) {
        _sonucState.value = _sonucState.value.copy(hikayeDialogKontrol = durum)
    }

    fun setDigerleriniGoster(durum: Boolean) {
        _sonucState.value = _sonucState.value.copy(digerleriniGoster = durum)
    }

    fun setGosterilecekAlert(gosterilecekAlert: String) {
        _sonucState.value = _sonucState.value.copy(gosterilecekAlert = gosterilecekAlert)
    }

    fun setAlertKontrol(durum: Boolean) {
        _sonucState.value = _sonucState.value.copy(alertKontrol = durum)
    }

    fun setSecilenHikaye(kullaniciYarismaHikayesi: KullaniciYarismaHikayesi) {
        _sonucState.value = _sonucState.value.copy(secilenHikaye = kullaniciYarismaHikayesi)
    }


    fun setKatilimSaglanmisMi(durum:Boolean) {
        _sonucState.value = _sonucState.value.copy(katilimSaglanmisMi = durum)
    }

    fun bilgileriGir(basariliMi: (Boolean) -> Unit) {
        setBekleniyor(true)
        firebaseRepository.yarismaBittiktenSonraBilgileriGir(onaylanmisYarismaHikayeleri.value) {
            if (it) {
                basariliMi(true)
            } else {
                setToastMesaj("İnternetiniz Kontrol Ediniz.")
                basariliMi(false)
            }
            setBekleniyor(false)
        }
    }

    fun sirayiGetir() {
        for (i in 0..<_onaylanmisKullaniciYarismaHikayeleri.value.count()) {
            if (_onaylanmisKullaniciYarismaHikayeleri.value[i].yazarEmail == firebaseRepository.aktifKullanici?.email) {
                setKacinciOlundu(i + 1)
                return
            }
        }

        setKacinciOlundu(-1)
    }

    fun setKacinciOlundu(sira:Int) {
        _sonucState.value = _sonucState.value.copy(kacinciOlundu = sira)
    }

    /*fun setYazarRol(rol:String) {
        println("Sonuc Ekranında setYazarRol'e girdi")
        _sonucState.value = _sonucState.value.copy(yazarRol = rol)
    }

    fun setAnaHikaye(anaHikaye:String) {
        println("Sonuc Ekranında setAnaHikaye'ye girdi")
        _sonucState.value = _sonucState.value.copy(yazarRol = anaHikaye)
    }*/
}