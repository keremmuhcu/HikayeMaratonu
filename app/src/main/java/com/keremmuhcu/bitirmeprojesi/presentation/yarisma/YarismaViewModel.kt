package com.keremmuhcu.bitirmeprojesi.presentation.yarisma

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.keremmuhcu.bitirmeprojesi.data.FirebaseRepository
import com.keremmuhcu.bitirmeprojesi.data.models.YarismaHikayesi
import com.keremmuhcu.bitirmeprojesi.data.models.Yazar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class YarismaViewModel:ViewModel() {
    private val firebaseRepository = FirebaseRepository()

    private val _yarismaState = MutableStateFlow(YarismaState())
    val yarismaState: StateFlow<YarismaState> = _yarismaState

    private val _yazar = MutableStateFlow(Yazar())
    val yazar: StateFlow<Yazar> = _yazar

    private val _yarisma = MutableStateFlow(YarismaHikayesi())
    val yarisma: StateFlow<YarismaHikayesi> = _yarisma

    init {
        yazarBilgileri()
    }

    fun yazarBilgileri() {
        firebaseRepository.aktifYazarBilgileriniAl {
            if (it.rol != "") {
                println("Yazar bilgisi alındı.")
                _yazar.value = it

                firebaseRepository.sonYarismayiGetir { yarismaHikayesi ->
                    if (yarismaHikayesi.anaHikaye != null) {
                        println("Yarışma bilgisi alındı")
                        _yarisma.value = yarismaHikayesi
                        setBekleniyor(false)
                        //_yarismaState.value = _yarismaState.value.copy(bekleniyor = false)
                    } else {
                        _yarismaState.value = _yarismaState.value.copy(toastMesaj = "Yarışma bilgisi alınamadı.")
                        _yarisma.value = _yarisma.value.copy(anaHikaye = null)
                    }
                }
            } else {
                println("ViewModel yazar bilgisi alınamadı.")
                setBekleniyor(false)
                _yarismaState.value = _yarismaState.value.copy(toastMesaj = "İnternetinizi Kontrol Ediniz")
            }


        }
    }

    fun toastMesajGoster(context: Context) {
        Toast.makeText(context,_yarismaState.value.toastMesaj, Toast.LENGTH_LONG).show()
        _yarismaState.value = _yarismaState.value.copy(toastMesaj = "")
    }

    fun setBekleniyor(durum:Boolean) {
        _yarismaState.value = _yarismaState.value.copy(bekleniyor = durum)

    }

    fun setHikayesiAsamasiAlindiMi(durum:Boolean) {
        _yarismaState.value = _yarismaState.value.copy(hikayeAsamasiAlindiMi = durum)
    }

    fun setBaslangicBilgileriAlindiMi(durum:Boolean) {
        _yarismaState.value = _yarismaState.value.copy(baslangicBilgileriAlindiMi = durum)
    }


}