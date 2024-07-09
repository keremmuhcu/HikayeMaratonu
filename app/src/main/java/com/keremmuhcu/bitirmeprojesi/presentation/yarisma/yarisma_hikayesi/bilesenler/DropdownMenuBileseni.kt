package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.yarisma_hikayesi.bilesenler

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.yarisma_hikayesi.YarismaHikayesiState
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.yarisma_hikayesi.YarismaHikayesiViewModel
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri

@Composable
fun DropdownMenuBileseni(
    yarismaHikayesiState: YarismaHikayesiState,
    yarismaHikayesiViewModel: YarismaHikayesiViewModel,
    anaSayfasinaGit:() -> Unit
) {

    IconButton(
        onClick = {
            if (!yarismaHikayesiState.dropdownKontrol && !yarismaHikayesiState.duzenleModu) {
                yarismaHikayesiViewModel.setDropdownKontrol(true)
            } else if (yarismaHikayesiState.duzenleModu) {
                yarismaHikayesiViewModel.setAlertKontrol(true)
                yarismaHikayesiViewModel.setGosterilecekAlert("duzenle")
                yarismaHikayesiViewModel.setDuzenleModu(false)
            }
        }
    ) {
        if (yarismaHikayesiState.duzenleModu) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "",
                tint = AcikGri
            )
        } else {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "",
                tint = AcikGri
            )
        }

    }

    if (yarismaHikayesiState.dropdownKontrol) {
        DropdownMenu(expanded = yarismaHikayesiState.dropdownKontrol, onDismissRequest = { yarismaHikayesiViewModel.setDropdownKontrol(!yarismaHikayesiState.dropdownKontrol) }) {
            DropdownMenuItem(
                text = { Text(text = "Düzenle") },
                onClick = {
                    yarismaHikayesiViewModel.setDropdownKontrol(false)
                    yarismaHikayesiViewModel.setDuzenleModu(true)
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Sil",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                },
                onClick = {
                    yarismaHikayesiViewModel.setAlertKontrol(true)
                    yarismaHikayesiViewModel.setGosterilecekAlert("sil")
                    yarismaHikayesiViewModel.setDropdownKontrol(false)
                }
            )
        }
    }

    if (yarismaHikayesiState.alertKontrol) {
        AlertDialog(
            onDismissRequest = {
               yarismaHikayesiViewModel.setAlertKontrol(false)
            },
            title = {
                if (yarismaHikayesiState.gosterilecekAlert == "duzenle") {
                    Text(text = "Hikaye güncellenecek")
                } else if (yarismaHikayesiState.gosterilecekAlert == "sil") {
                    Text(text = "Yarışma silinecek.")
                }
            },
            text = {
                if (yarismaHikayesiState.gosterilecekAlert == "duzenle") {
                    Text(text = "Bu işlem geri alınamaz.")
                } else if (yarismaHikayesiState.gosterilecekAlert == "sil") {
                    Text(text = "Yarışma iptal edilecek. Bu işlem geri alınamaz.")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        yarismaHikayesiViewModel.setAlertKontrol(false)
                        if (yarismaHikayesiState.gosterilecekAlert == "duzenle") {
                            yarismaHikayesiViewModel.yarismayiGuncelle()
                        } else if (yarismaHikayesiState.gosterilecekAlert == "sil") {
                            yarismaHikayesiViewModel.yarismayiSil {
                                if (it) {
                                    anaSayfasinaGit()
                                }
                            }
                        }
                    }
                ) {
                    Text(text = "Tamam")
                }
            },
            dismissButton = {
                Button(onClick = { yarismaHikayesiViewModel.setAlertKontrol(false) }) {
                    Text(text = "İptal")
                }
            }

        )
    }
}