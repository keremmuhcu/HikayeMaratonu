package com.keremmuhcu.bitirmeprojesi.presentation.hikaye.bilesenler

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.HikayelerimState
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.HikayelerimViewModel
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily
import com.keremmuhcu.bitirmeprojesi.util.InternetKontrol

@Composable
fun AlertDialogBileseni(
    email:String,
    kullaniciAdi:String,
    hikayelerimViewModel: HikayelerimViewModel,
    hikayelerimState: HikayelerimState,
    context:Context
) {
    AlertDialog(
        onDismissRequest = { hikayelerimViewModel.setAlertKontrol(false) },
        title = {
            Text(
                text = if (hikayelerimState.gosterilecekAlert == "geri") {
                    "Kaydedilmemiş değişiklikler"
                } else if (hikayelerimState.gosterilecekAlert == "kaydet") {
                    "Hikaye kaydedilecek"
                } else {
                    "Hikaye yayınlanacak"
                },
                fontFamily = nunitoFontFamily,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = if (hikayelerimState.gosterilecekAlert == "geri") {
                    "Yaptığınız değişiklikler kaybolacak."
                } else if (hikayelerimState.gosterilecekAlert == "kaydet") {
                    "Hikayenizin diğer yazarlara gösterilmeyecek. Sonrasında düzenleme yapabilir, yeni bölümler ekleyebilir ve yayınlayabilirsiniz."
                } else {
                    "Hikayeniz yayınlanacak. Sonrasında düzenleme yapabilir ve yeni bölümler ekleyebilirsiniz."
                },
                fontFamily = nunitoFontFamily,
            )
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = {
                        hikayelerimViewModel.setAlertKontrol(false)
                    }
                ) {
                    Text(text = "İptal", fontFamily = nunitoFontFamily)
                }

                Button(
                    onClick = {
                        if (InternetKontrol.internetVarMi(context)) {
                            hikayelerimViewModel.setAlertKontrol(false)

                            if (hikayelerimState.gosterilecekAlert == "geri") {
                                hikayelerimViewModel.setDialogKontrol(false)
                                hikayelerimViewModel.yeniHikayeOlusturdakiAlanlariTemizle()
                            } else if (hikayelerimState.gosterilecekAlert == "kaydet") {
                                hikayelerimViewModel.yeniHikayeyiKaydetYaDaYayinla(kullaniciAdi,false) { basariDurumu->
                                    if (basariDurumu) {
                                        hikayelerimViewModel.setDialogKontrol(false)
                                        hikayelerimViewModel.hikayeleriGetir(email)
                                        hikayelerimViewModel.yeniHikayeOlusturdakiAlanlariTemizle()
                                    }
                                }
                            } else {
                                hikayelerimViewModel.yeniHikayeyiKaydetYaDaYayinla(kullaniciAdi,true) { basariDurumu->
                                    if (basariDurumu) {
                                        hikayelerimViewModel.setDialogKontrol(false)
                                        hikayelerimViewModel.hikayeleriGetir(email)
                                        hikayelerimViewModel.yeniHikayeOlusturdakiAlanlariTemizle()
                                    }
                                }
                            }

                        } else {
                            hikayelerimViewModel.setToastMesaj("Bağlantı hatası")
                        }
                    }
                ) {
                    Text(text = "Tamam", fontFamily = nunitoFontFamily)
                }
            }
        }
    )
}