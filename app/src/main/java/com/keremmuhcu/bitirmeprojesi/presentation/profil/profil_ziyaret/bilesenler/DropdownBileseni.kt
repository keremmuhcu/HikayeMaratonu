package com.keremmuhcu.bitirmeprojesi.presentation.profil.profil_ziyaret.bilesenler

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.keremmuhcu.bitirmeprojesi.presentation.profil.profil_ziyaret.ProfilZiyaretState
import com.keremmuhcu.bitirmeprojesi.presentation.profil.profil_ziyaret.ProfilZiyaretViewModel
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily
import com.keremmuhcu.bitirmeprojesi.util.InternetKontrol

@Composable
fun DropdownBileseni(
    profilZiyaretState: ProfilZiyaretState,
    profilZiyaretViewModel: ProfilZiyaretViewModel,
    context:Context
) {
    IconButton(onClick = { profilZiyaretViewModel.setDropdownGoster(!profilZiyaretState.dropdownGoster) }) {
        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "", tint = AcikGri)

        DropdownMenu(
            expanded = profilZiyaretState.dropdownGoster,
            onDismissRequest = { profilZiyaretViewModel.setDropdownGoster(false) }
        ) {
            DropdownMenuItem(
                text = {
                    if (profilZiyaretState.yazar.rol == "yazar") {
                        Text(text = "Mod yap", fontFamily = nunitoFontFamily)
                    } else {
                        Text(text = "Yazar yap", fontFamily = nunitoFontFamily)
                    }
                },
                onClick = {
                    if (InternetKontrol.internetVarMi(context)) {
                        if (profilZiyaretState.yazar.rol == "yazar") {
                            profilZiyaretViewModel.yazarRoluGuncelle("mod")
                        } else {
                            profilZiyaretViewModel.yazarRoluGuncelle("yazar")
                        }
                    } else {
                        profilZiyaretViewModel.setToastMesaj("Bağlantı hatası")
                    }
                    profilZiyaretViewModel.setDropdownGoster(false)
                }
            )
        }
    }
}