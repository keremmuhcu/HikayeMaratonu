package com.keremmuhcu.bitirmeprojesi.presentation.hikaye.bilesenler

import android.content.Context
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.HikayelerimState
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.HikayelerimViewModel
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily
import com.keremmuhcu.bitirmeprojesi.util.InternetKontrol

@Composable
fun DropdownBileseni(
    hikayelerimState: HikayelerimState,
    hikayelerimViewModel: HikayelerimViewModel,
    context:Context
) {
    DropdownMenu(
        expanded = hikayelerimState.dropdownKontrol,
        onDismissRequest = {
            hikayelerimViewModel.setDropdownKontrol(false)
        }
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = "Kaydet",
                    fontFamily = nunitoFontFamily
                )
            },
            onClick = {
                hikayelerimViewModel.setDropdownKontrol(false)
                if (InternetKontrol.internetVarMi(context)) {
                    if (hikayelerimViewModel.yeniHikayeninAlanlariDoldurulduMu()) {
                        hikayelerimViewModel.setGosterilecekAlert("kaydet")
                        hikayelerimViewModel.setAlertKontrol(true)
                    } else {
                        hikayelerimViewModel.setToastMesaj("Lütfen tüm değerleri giriniz")
                    }
                } else {
                    hikayelerimViewModel.setToastMesaj("Bağlantı hatası")
                }
            }
        )

        DropdownMenuItem(
            text = {
                Text(
                    text = "Yayınla",
                    fontFamily = nunitoFontFamily
                )
            },
            onClick = {
                hikayelerimViewModel.setDropdownKontrol(false)
                if (InternetKontrol.internetVarMi(context)) {
                    if (hikayelerimViewModel.yeniHikayeninAlanlariDoldurulduMu()) {
                        hikayelerimViewModel.setGosterilecekAlert("yayinla")
                        hikayelerimViewModel.setAlertKontrol(true)
                    } else {
                        hikayelerimViewModel.setToastMesaj("Lütfen tüm değerleri giriniz")
                    }
                } else {
                    hikayelerimViewModel.setToastMesaj("Bağlantı hatası")
                }
            }
        )
    }
}