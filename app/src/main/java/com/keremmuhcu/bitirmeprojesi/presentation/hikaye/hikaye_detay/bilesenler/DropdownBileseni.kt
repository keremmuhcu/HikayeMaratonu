package com.keremmuhcu.bitirmeprojesi.presentation.hikaye.hikaye_detay.bilesenler

import android.content.Context
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.hikaye_detay.HikayeDetayState
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.hikaye_detay.HikayeDetayViewModel
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily

@Composable
fun DropdownBileseni(
    hikayeDetayState: HikayeDetayState,
    hikayeDetayViewModel: HikayeDetayViewModel,
    context: Context
) {
    DropdownMenu(
        expanded = hikayeDetayState.dropdownKontrol,
        onDismissRequest = {
            hikayeDetayViewModel.setDropdownKontrol(false)
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
                if (hikayeDetayViewModel.bolumunIcerigiDoluMu()) {
                    hikayeDetayViewModel.setDropdownKontrol(false)
                    hikayeDetayViewModel.setGosterilecekAlert("kaydet")
                    hikayeDetayViewModel.setAlertKontrol(true)
                } else {
                    hikayeDetayViewModel.setToastMesaj("Lütfen bölüm içeriğini yazınız.")
                }
            }
        )

        if (hikayeDetayState.hikaye.yayinlandiMi!! && hikayeDetayViewModel.bolumlerListesi.value.last().yayinlandiMi!!) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Yayınla",
                        fontFamily = nunitoFontFamily
                    )
                },
                onClick = {
                    if (hikayeDetayViewModel.bolumunIcerigiDoluMu()) {
                        hikayeDetayViewModel.setDropdownKontrol(false)
                        hikayeDetayViewModel.setGosterilecekAlert("yeniBolumYayinla")
                        hikayeDetayViewModel.setAlertKontrol(true)
                    } else {
                        hikayeDetayViewModel.setToastMesaj("Lütfen bölüm içeriğini yazınız.")
                    }
                }
            )
        }
    }
}