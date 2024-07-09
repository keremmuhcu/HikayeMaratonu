package com.keremmuhcu.bitirmeprojesi.presentation.timer

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import com.keremmuhcu.bitirmeprojesi.data.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

class TimerViewModel(application: Application): AndroidViewModel(application) {
    val firebaseRepository = FirebaseRepository()
    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState

    var timer : CountDownTimer? = null

    fun startTimer() {

        timer = object : CountDownTimer(_timerState.value.kalanZaman, 10) {
            override fun onTick(millisUntilFinished: Long) {
                _timerState.value = _timerState.value.copy(kalanZaman = millisUntilFinished)
                formatla()
            }

            override fun onFinish() {
                _timerState.value = _timerState.value.copy(kalanZaman = 0L)
                oylamaSuresiniHesapla()
            }
        }.start()
    }

    fun kalanSureyiHesapla() {
        firebaseRepository.kalanSureyiHesapla {kalanSure->
            println("Kalan Süre: $kalanSure")
            if (kalanSure != "yarismaYok") {
                val yarismaninKalanSuresi = kalanSure.toString().toLong()

                if (yarismaninKalanSuresi > 0L) {
                    if (timer != null) {
                        timer?.cancel()
                        timer = null
                    }
                    setAsama("yarisma")
                    println("Kalan Zaman 0'dan büyük")
                    _timerState.value = _timerState.value.copy(kalanZaman = yarismaninKalanSuresi)
                    startTimer()
                } else  {
                    println("Kalan Zaman 0'dan küçük")
                    oylamaSuresiniHesapla()
                }
            } else if(kalanSure == "yarismaYok") {
                setAsama("yarismaYok")
            } else {
                setAsama("internetHatasi")
            }

        }
    }

    fun oylamaSuresiniHesapla() {
        if (timer != null) {
            timer?.cancel()
            timer = null
        }

        firebaseRepository.oylamaSuresiniHesapla {
            if (it > 0L) {
                println("Oylama Aşaması True")
                _timerState.value = _timerState.value.copy(kalanZaman = it)
                startTimer()
                setAsama("oylama")
            } else {
                println("Oylama Aşaması False")
                setAsama("sonuc")
            }
        }
    }


    fun formatla() {
        _timerState.value = _timerState.value.copy(gun = _timerState.value.kalanZaman / (24 * 3600 * 1000))
        _timerState.value = _timerState.value.copy(saat = (_timerState.value.kalanZaman / (3600 * 1000)) % 24)
        _timerState.value = _timerState.value.copy(dakika = (_timerState.value.kalanZaman / (60 * 1000)) % 60)
        _timerState.value = _timerState.value.copy(saniye = (_timerState.value.kalanZaman / 1000) % 60)
        _timerState.value = _timerState.value.copy(gosterim = "${_timerState.value.gun} : ${_timerState.value.saat} : ${_timerState.value.dakika} : ${_timerState.value.saniye}")
    }

    fun setKalanZaman() {
        timer?.cancel()
        timer = null
        val randomLong = Random.nextLong(100000L, 200000000L + 1)
        _timerState.value = _timerState.value.copy(kalanZaman = randomLong)
        startTimer()

    }

    fun setBekleniyor(durum:Boolean) {
        _timerState.value = _timerState.value.copy(bekleniyor = durum)

    }

    fun setAsama(asama:String) {
        _timerState.value = _timerState.value.copy(asama = asama)
    }

}
