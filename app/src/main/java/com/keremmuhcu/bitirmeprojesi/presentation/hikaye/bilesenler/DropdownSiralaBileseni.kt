package com.keremmuhcu.bitirmeprojesi.presentation.hikaye.bilesenler

import android.content.Context
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.HikayelerimState
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.HikayelerimViewModel
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily

@Composable
fun DropdownSiralaBileseni(
    hikayelerimState: HikayelerimState,
    hikayelerimViewModel: HikayelerimViewModel,
    context: Context
) {
    DropdownMenu(
        expanded = hikayelerimState.dropdownSiralaKontrol,
        onDismissRequest = {
            hikayelerimViewModel.setDropdownSiralaKontrol(false)
        }
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = "Hepsi",
                    fontFamily = nunitoFontFamily,
                    fontWeight = FontWeight.Bold
                )
            },
            onClick = {
                hikayelerimViewModel.setDropdownSiralaKontrol(false)
                hikayelerimViewModel.sirala("hepsi")
            }
        )

        DropdownMenuItem(
            text = {
                Text(
                    text = if (hikayelerimState.ziyaretciMi) {
                        "Devam Edenler"
                    } else {
                        "Yayınlananlar"
                    },
                    fontFamily = nunitoFontFamily
                )
            },
            onClick = {
                hikayelerimViewModel.setDropdownSiralaKontrol(false)
                hikayelerimViewModel.sirala("yayinlananlar")
            }
        )

        if (!hikayelerimState.ziyaretciMi) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Yayınlanmayanlar",
                        fontFamily = nunitoFontFamily
                    )
                },
                onClick = {
                    hikayelerimViewModel.setDropdownSiralaKontrol(false)
                    hikayelerimViewModel.sirala("yayinlanmayanlar")
                }
            )
        }



        DropdownMenuItem(
            text = {
                Text(
                    text = "Bitenler",
                    fontFamily = nunitoFontFamily
                )
            },
            onClick = {
                hikayelerimViewModel.setDropdownSiralaKontrol(false)
                hikayelerimViewModel.sirala("bitenler")
            }
        )
    }
}