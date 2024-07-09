package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.kullanici_yar_hik_olusturma.bilesenler

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.keremmuhcu.bitirmeprojesi.data.models.KullaniciYarismaHikayesi
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.kullanici_yar_hik_olusturma.KullaniciYarHikOlusturmaState
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.kullanici_yar_hik_olusturma.KullaniciYarHikOlusturmaViewModel
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri

@Composable
fun DropdownMenuBileseni(
    anaSayfasinaGit: () -> Unit,
    kullaniciYarHikOlusturmaState: KullaniciYarHikOlusturmaState,
    kullaniciYarHikOlusturmaViewModel: KullaniciYarHikOlusturmaViewModel,
    yazarKullaniciAdi: String,
    yazarRol: String
) {
    val context = LocalContext.current

    IconButton(
        onClick = {
            if (!kullaniciYarHikOlusturmaState.dropdownKontrol) {
                kullaniciYarHikOlusturmaViewModel.setDropdownKontrol(true)
            } else  {
                kullaniciYarHikOlusturmaViewModel.setDropdownKontrol(false)
            }
        }
    ) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "",
            tint = AcikGri
        )
    }



    DropdownMenu(expanded = kullaniciYarHikOlusturmaState.dropdownKontrol, onDismissRequest = { kullaniciYarHikOlusturmaViewModel.setDropdownKontrol(!kullaniciYarHikOlusturmaState.dropdownKontrol) }) {
        DropdownMenuItem(
            text = { Text(text = "Kaydet") },
            onClick = {
                kullaniciYarHikOlusturmaViewModel.setGosterilecekAlert("dropdownEylemi")
                if (kullaniciYarHikOlusturmaState.baslik.isEmpty() || kullaniciYarHikOlusturmaState.kullaniciYarismaHikayesi.isEmpty()) {
                    kullaniciYarHikOlusturmaViewModel.setToastMesaj("Lütfen Değerleri Eksiksiz Giriniz!")
                } else {
                    kullaniciYarHikOlusturmaViewModel.kullaniciHikayeyiTaslakKaydet()
                }
                kullaniciYarHikOlusturmaViewModel.setDropdownKontrol(false)
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = "Paylaş")
            },
            onClick = {
                kullaniciYarHikOlusturmaViewModel.setGosterilecekAlert("dropdownEylemi")
                kullaniciYarHikOlusturmaViewModel.setAlertKontrol(true)
                kullaniciYarHikOlusturmaViewModel.setDropdownKontrol(false)
            }
        )
    }

    if (kullaniciYarHikOlusturmaState.alertKontrol && kullaniciYarHikOlusturmaState.gosterilecekAlert == "dropdownEylemi") {
        AlertDialog(
            onDismissRequest = {
               kullaniciYarHikOlusturmaViewModel.setAlertKontrol(false)
            },
            title = {
                Text(text = "Hikaye gönderilecek")
            },
            text = {
                Text(text = "Sonrasında tekrardan düzeneleme yapamazsınız.")

            },
            confirmButton = {
                Button(
                    onClick = {
                        val degerlerDogruMu = kullaniciYarHikOlusturmaState.baslik.any {
                            it.isLetter()
                        } && kullaniciYarHikOlusturmaState.kullaniciYarismaHikayesi.any {
                            it.isLetter()
                        }
                        if (!degerlerDogruMu) {
                            kullaniciYarHikOlusturmaViewModel.setToastMesaj("Değerleri Eksiksiz Giriniz")
                        } else {
                            val yazarYarHik = KullaniciYarismaHikayesi(
                                kullaniciYarHikOlusturmaState.baslik.trim(),
                                kullaniciYarHikOlusturmaState.kullaniciYarismaHikayesi.trim(),
                                yazarKullaniciAdi
                            )
                            if (yazarRol == "yazar") {
                                kullaniciYarHikOlusturmaViewModel.kullaniciYarismaHikayesiniYoneticilereGonder(yazarYarHik)
                                if (kullaniciYarHikOlusturmaState.hata) {
                                    anaSayfasinaGit()
                                } else {
                                    Toast.makeText(context, "Başarılı", Toast.LENGTH_SHORT).show()
                                    anaSayfasinaGit()
                                }
                            } else {
                                kullaniciYarHikOlusturmaViewModel.adminYaDaModHikayesiniOnayla(yazarYarHik)
                                if (kullaniciYarHikOlusturmaState.hata) {
                                    anaSayfasinaGit()
                                } else {
                                    Toast.makeText(context, "Başarılı", Toast.LENGTH_SHORT).show()
                                    anaSayfasinaGit()
                                }
                            }
                        }

                        kullaniciYarHikOlusturmaViewModel.setAlertKontrol(false)
                    }
                ) {
                    Text(text = "Tamam")
                }
            },
            dismissButton = {
                Button(onClick = { kullaniciYarHikOlusturmaViewModel.setAlertKontrol(false) }) {
                    Text(text = "İptal")
                }
            }

        )
    }


}