package com.keremmuhcu.bitirmeprojesi.presentation.hikaye.hikaye_detay.bilesenler

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.hikaye_detay.HikayeDetayState
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.hikaye_detay.HikayeDetayViewModel
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily

@Composable
fun DropdownDuzenleBileseni(
    hikayeDetayViewModel: HikayeDetayViewModel,
    hikayeDetayState: HikayeDetayState
) {
    DropdownMenu(
        expanded =hikayeDetayState.dropdownDuzenleKontrol,
        onDismissRequest = { hikayeDetayViewModel.setDropdownDuzenleKontrol(false) }
    ) {
        val liste = hikayeDetayViewModel.bolumlerListesi.value
        val secilenBolum = hikayeDetayState.secilenBolum

        DropdownMenuItem(
            text = { Text(text = "Düzenle", fontFamily = nunitoFontFamily) },
            onClick = {
                hikayeDetayViewModel.setDropdownDuzenleKontrol(false)
                hikayeDetayViewModel.setDuzenleModuAcikMi(true)
            }
        )

        if (hikayeDetayState.hikaye.yayinlandiMi!!) {
            if (!(liste.indexOf(secilenBolum) == 0 || liste.indexOf(secilenBolum) == 1)) {
                if (liste[liste.indexOf(secilenBolum) - 1].yayinlandiMi!! && !secilenBolum.yayinlandiMi!!) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Yayınla",
                                fontFamily = nunitoFontFamily
                            )
                        },
                        onClick = {
                            hikayeDetayViewModel.setDropdownDuzenleKontrol(false)
                            hikayeDetayViewModel.setGosterilecekAlert("bolumYayinla")
                            hikayeDetayViewModel.setAlertKontrol(true)
                        }
                    )
                }
            }
        }

        if (!(liste.indexOf(secilenBolum) == 0 || liste.indexOf(secilenBolum) == 1)) {
            DropdownMenuItem(
                text = { Text(text = "Bölümü sil", fontFamily = nunitoFontFamily, color = Color.Red) },
                onClick = {
                    hikayeDetayViewModel.setDropdownDuzenleKontrol(false)
                    hikayeDetayViewModel.setGosterilecekAlert("bolumSil")
                    hikayeDetayViewModel.setAlertKontrol(true)
                }
            )
        }

    }
}