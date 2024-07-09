package com.keremmuhcu.bitirmeprojesi.presentation.biten_yarismalar.biten_yarisma_detay

import androidx.lifecycle.ViewModel
import com.keremmuhcu.bitirmeprojesi.data.FirebaseRepository
import com.keremmuhcu.bitirmeprojesi.data.models.KullaniciYarismaHikayesi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BitenYarismaDetayViewModel:ViewModel() {
    private val firebaseRepository = FirebaseRepository()
    //var onaylanmisYarismaHikayeleri: StateFlow<List<KullaniciYarismaHikayesi>> = firebaseRepository.onaylanmisKullaniciYarismaHikayeleri
    private val _onaylanmisYarismaHikayeleri = MutableStateFlow<List<KullaniciYarismaHikayesi>>(
        emptyList()
    )
    var onaylanmisYarismaHikayeleri: StateFlow<List<KullaniciYarismaHikayesi>> = _onaylanmisYarismaHikayeleri


    private val _bitenYarismaDetayState = MutableStateFlow(BitenYarismaDetayState())
    val bitenYarismaDetayState: StateFlow<BitenYarismaDetayState> = _bitenYarismaDetayState

    init {
        /*setBekleniyor(true)
        firebaseRepository.onaylanmisKullaniciYarismaHikayeleriniGetir("22haz2024") {hata ->
            if (hata) {
                setToastMesaj("İnternetinizi Kontrol Ediniz")
            } else {
                firebaseRepository._onaylanmisKullaniciYarismaHikayeleri.value = onaylanmisYarismaHikayeleri.value.sortedWith(
                    compareByDescending <KullaniciYarismaHikayesi> {
                        it.oylar
                    }.thenBy {
                        it.olusturmaTarihi
                    }
                )

                onaylanmisYarismaHikayeleri = firebaseRepository.onaylanmisKullaniciYarismaHikayeleri

            }
            setBekleniyor(false)
        }

         */
    }

    fun bitenYarismalariGetir(id:String) {
        setBekleniyor(true)
        firebaseRepository.onaylanmisKullaniciYarismaHikayeleriniGetir(id) {hata, liste ->
            if (hata) {
                setToastMesaj("İnternetinizi Kontrol Ediniz")
            } else {
                _onaylanmisYarismaHikayeleri.value = liste.sortedWith(
                    compareByDescending <KullaniciYarismaHikayesi> {
                        it.oylar
                    }.thenBy {
                        it.olusturmaTarihi
                    }
                )

                /*firebaseRepository._onaylanmisKullaniciYarismaHikayeleri.value = onaylanmisYarismaHikayeleri.value.sortedWith(
                    compareByDescending <KullaniciYarismaHikayesi> {
                        it.oylar
                    }.thenBy {
                        it.olusturmaTarihi
                    }
                )

                onaylanmisYarismaHikayeleri = firebaseRepository.onaylanmisKullaniciYarismaHikayeleri

                 */

            }
            setBekleniyor(false)

        }

        println("Biten Yarişmaları Getir Çalıştı")
        /*firebaseRepository.onaylanmisKullaniciYarismaHikayeleriniGetir(id) {hata ->
            if (hata) {
                setToastMesaj("İnternetinizi Kontrol Ediniz")
            } else {

                firebaseRepository._onaylanmisKullaniciYarismaHikayeleri.value = onaylanmisYarismaHikayeleri.value.sortedWith(
                    compareByDescending <KullaniciYarismaHikayesi> {
                        it.oylar
                    }.thenBy {
                        it.olusturmaTarihi
                    }
                )

                onaylanmisYarismaHikayeleri = firebaseRepository.onaylanmisKullaniciYarismaHikayeleri

            }
            setBekleniyor(false)
        }*/
    }

    fun setToastMesaj(toastMesaj: String) {
        _bitenYarismaDetayState.value = _bitenYarismaDetayState.value.copy(toastMesaj = toastMesaj)
    }

    fun setBekleniyor(durum: Boolean) {
        _bitenYarismaDetayState.value = _bitenYarismaDetayState.value.copy(bekleniyor = durum)
    }

    fun setSecilenHikaye(kullaniciYarismaHikayesi: KullaniciYarismaHikayesi) {
        _bitenYarismaDetayState.value = _bitenYarismaDetayState.value.copy(secilenHikaye = kullaniciYarismaHikayesi)
    }

    fun setHikayeDialogKontrol(durum:Boolean) {
        _bitenYarismaDetayState.value = _bitenYarismaDetayState.value.copy(hikayeDialogKontrol = durum)
    }

    fun setDigerleriniGoster(durum: Boolean) {
        _bitenYarismaDetayState.value = _bitenYarismaDetayState.value.copy(digerleriniGoster = durum)
    }

}