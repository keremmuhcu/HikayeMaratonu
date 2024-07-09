package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.oylama.bilesenler

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.oylama.OylamaState
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.oylama.OylamaViewModel

@Composable
fun DropdownMenuBileseni(
    oylamaState: OylamaState,
    oylamaViewModel: OylamaViewModel
) {
    if (oylamaState.dropdownKontrol) {
        DropdownMenu(
            expanded = oylamaState.dropdownKontrol,
            onDismissRequest = { oylamaViewModel.setDropdownKontrol(false) }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = "Oy - Artan")
                },
                onClick = {
                    oylamaViewModel.setDropdownKontrol(false)
                    oylamaViewModel.yarismalariSirala("artan")
                }
            )

            DropdownMenuItem(
                text = {
                    Text(text = "Oy - Azalan")
                },
                onClick = {
                    oylamaViewModel.setDropdownKontrol(false)
                    oylamaViewModel.yarismalariSirala("azalan")

                }
            )

            DropdownMenuItem(
                text = {
                    Text(text = "Hikayem")
                },
                onClick = {
                    oylamaViewModel.setDropdownKontrol(false)
                    oylamaViewModel.yarismalariSirala("hikayem")
                }
            )

            DropdownMenuItem(
                text = {
                    Text(text = "Oy verdiğim")
                },
                onClick = {
                    oylamaViewModel.setDropdownKontrol(false)
                    if (oylamaState.yazarOyVermisMi) {
                        oylamaViewModel.yarismalariSirala(oylamaState.verilenOy)
                    } else {
                        oylamaViewModel.setToastMesaj("Henüz oy kullanmadınız.")
                    }

                }
            )
        }
    }
}