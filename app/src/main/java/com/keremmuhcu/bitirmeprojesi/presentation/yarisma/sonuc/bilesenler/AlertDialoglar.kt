package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.sonuc.bilesenler

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.sonuc.SonucState
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.sonuc.SonucViewModel
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily
import com.keremmuhcu.bitirmeprojesi.util.InternetKontrol

@Composable
fun AlertDialoglar(
    anaSayfasinaGit:() -> Unit,
    sonucViewModel: SonucViewModel,
    sonucState: SonucState,
    context:Context
) {
    AlertDialog(
        onDismissRequest = {
            sonucViewModel.setAlertKontrol(false)
        },
        title = {
            if (sonucState.gosterilecekAlert == "bitir") {
                Text(
                    text = "Yarışmayı bitir",
                    fontFamily = nunitoFontFamily,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = "Sıralama ölçütleri",
                    fontFamily = nunitoFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            if (sonucState.gosterilecekAlert == "bitir") {
                Text(
                    text = "Yarışma sonlandırılacak. Bu işlem geri alınamaz.",
                    fontFamily = nunitoFontFamily,
                    fontSize = 18.sp
                )
            } else {
                Text(
                    text = "Önce en yüksek oyları alanlar sıralandırılır. Eşitlik durumunda ise hikayesini önce tamamlamış olan önde olur.",
                    fontFamily = nunitoFontFamily,
                    fontSize = 18.sp
                )
            }
        },
        confirmButton = {
            if (sonucState.gosterilecekAlert == "bitir") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { sonucViewModel.setAlertKontrol(false) }) {
                        Text(text = "İptal")
                    }

                    Button(onClick = {
                        sonucViewModel.setAlertKontrol(false)
                        if (InternetKontrol.internetVarMi(context)) {
                            sonucViewModel.bilgileriGir {
                                if (it) {
                                    sonucViewModel.yarismayiBitir {bittiMi->
                                        if (bittiMi) {
                                            anaSayfasinaGit()
                                        }
                                    }
                                }
                            }
                        } else {
                            sonucViewModel.setToastMesaj("Bağlantı hatası")
                        }

                    }
                    ) {
                        Text(text = "Tamam")
                    }

                }
            } else {
                Button(onClick = { sonucViewModel.setAlertKontrol(false) }) {
                    Text(text = "Tamam")
                }
            }
        }
    )
}