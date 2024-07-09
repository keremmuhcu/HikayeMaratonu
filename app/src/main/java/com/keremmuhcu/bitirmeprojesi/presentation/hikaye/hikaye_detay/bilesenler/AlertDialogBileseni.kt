package com.keremmuhcu.bitirmeprojesi.presentation.hikaye.hikaye_detay.bilesenler

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
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.hikaye_detay.HikayeDetayState
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.hikaye_detay.HikayeDetayViewModel
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily
import com.keremmuhcu.bitirmeprojesi.util.InternetKontrol

@Composable
fun AlertDialogBileseni(
    hikayeDetayViewModel: HikayeDetayViewModel,
    hikayeDetayState: HikayeDetayState,
    context: Context,
    hikayelerimSayfasinaGit:() -> Unit
) {
    AlertDialog(
        onDismissRequest = { hikayeDetayViewModel.setAlertKontrol(false) },
        title = {
            Text(
                text = when (hikayeDetayState.gosterilecekAlert) {
                    "geri" -> {
                        "Kaydedilmemiş değişiklikler"
                    }
                    "kaydet" -> {
                        "Bölüm kaydedilecek"
                    }
                    "devamEt" -> {
                        "Hikayeye devam edilecek"
                    }
                    "final" -> {
                        "Final yapılacak"
                    }
                    "hikayeYayinla" -> {
                        "Hikaye yayınlanacak"
                    }
                    "hepsiniYayinla" -> {
                        "Tüm bölümler yayınlanacak"
                    }
                    "hikayeSil" -> {
                        "Hikaye silinecek"
                    }
                    "bolumDuzenle" -> {
                        "Bölüm düzenlenecek"
                    }
                    "bolumSil" -> {
                        "Bölüm silinecek"
                    }
                    "bolumYayinla" -> {
                        "Bölüm yayınlanacak"
                    }
                    else -> {
                        "Bölüm yayınlanacak"
                    }
                },
                fontFamily = nunitoFontFamily,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = when (hikayeDetayState.gosterilecekAlert) {
                    "geri" -> {
                        "Yaptığınız değişiklikler kaybolacak."
                    }
                    "kaydet" -> {
                        "Bölüm diğer yazarlara gösterilmeyecek. Sonrasında düzenleme yapabilir ve yayınlayabilirsiniz."
                    }
                    "devamEt" -> {
                        "Final yapmış hikayeniz, devam ettirilecek."
                    }
                    "final" -> {
                        "Hikaye sonlandırılacak. Yayınlanmamış bölümler de yayınlanacak."
                    }
                    "hikayeYayinla" -> {
                        "Hikayeniz yayınlanacak. Sonrasında düzenleme yapabilir ve yeni bölümler ekleyebilirsiniz."
                    }
                    "hepsiniYayinla" -> {
                        "Yayınlanmamış tüm bölümleriniz yayınlanacak"
                    }
                    "hikayeSil" -> {
                        "Bu işlem geri alınamaz"
                    }
                    "bolumDuzenle" -> {
                        "Değişiklikler kaydedilecek. Bu işlem geri alınamaz."
                    }
                    "bolumSil" -> {
                        "Bu işlem geri alınamaz."
                    }
                    "bolumYayinla" -> {
                        "Bölüm yayınlanacak. Sonrasında düzenleme yapabilirsiniz"
                    }
                    else -> {
                        "Bölüm yayınlanacak. Sonrasında düzenleme yapabilirsiniz"
                    }
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
                        if (hikayeDetayState.gosterilecekAlert == "bolumDuzenle") {
                            hikayeDetayViewModel.setDuzenleModuAcikMi(false)
                        }
                        hikayeDetayViewModel.setAlertKontrol(false)
                    }
                ) {
                    Text(text = "İptal", fontFamily = nunitoFontFamily)
                }

                Button(
                    onClick = {
                        if (InternetKontrol.internetVarMi(context)) {
                            hikayeDetayViewModel.setAlertKontrol(false)

                            when (hikayeDetayState.gosterilecekAlert) {
                                "geri" -> {
                                    hikayeDetayViewModel.setDialogKontrol(false)
                                    hikayeDetayViewModel.setYeniBolumHikayesi("")
                                }
                                "kaydet" -> {
                                    hikayeDetayViewModel.setBekleniyor(true)
                                    hikayeDetayViewModel.yeniBolumEkle(false) {
                                        if (it) {
                                            hikayeDetayViewModel.setDialogKontrol(false)
                                            hikayeDetayViewModel.setBekleniyor(false)
                                            hikayeDetayViewModel.setYeniBolumHikayesi("")
                                        } else {
                                            hikayeDetayViewModel.setToastMesaj("Başarısız")
                                        }
                                    }
                                }
                                "devamEt" -> {
                                    hikayeDetayViewModel.setBekleniyor(true)
                                    hikayeDetayViewModel.hikayeyiFinalleYaDaDevamEttir(false) {
                                        if (it) {
                                            hikayeDetayViewModel.hikayeyiGetir(hikayeDetayState.hikaye.id!!) {
                                                hikayeDetayViewModel.setBekleniyor(false)
                                            }
                                        } else {
                                            hikayeDetayViewModel.setToastMesaj("Başarısız")
                                        }
                                    }
                                }
                                "final" -> {
                                    hikayeDetayViewModel.setBekleniyor(true)
                                    hikayeDetayViewModel.hikayeyiFinalleYaDaDevamEttir(true) {
                                        if (it) {
                                            if (hikayeDetayViewModel.yayinlanacakVarMi()) {
                                                hikayeDetayViewModel.hepsiniYayinla {
                                                    hikayeDetayViewModel.hikayeyiGetir(hikayeDetayState.hikaye.id!!) {
                                                        hikayeDetayViewModel.setBekleniyor(false)
                                                    }
                                                }
                                            } else {
                                                hikayeDetayViewModel.hikayeyiGetir(hikayeDetayState.hikaye.id!!) {
                                                    hikayeDetayViewModel.setBekleniyor(false)
                                                }
                                            }

                                        } else {
                                            hikayeDetayViewModel.setToastMesaj("Başarısız")
                                        }
                                    }
                                }
                                "hikayeYayinla" -> {
                                    hikayeDetayViewModel.setBekleniyor(true)
                                    hikayeDetayViewModel.hikayeyiYayinla {
                                        if (it) {
                                            hikayeDetayViewModel.hepsiniYayinla {
                                                hikayeDetayViewModel.hikayeyiGetir(hikayeDetayState.hikaye.id!!) {
                                                    hikayeDetayViewModel.setBekleniyor(false)
                                                }
                                            }
                                        } else {
                                            hikayeDetayViewModel.setToastMesaj("Başarısız")
                                        }
                                    }

                                }
                                "hepsiniYayinla" -> {
                                    hikayeDetayViewModel.hepsiniYayinla {
                                        if (!it) {
                                            hikayeDetayViewModel.setToastMesaj("Başarısız")
                                        }
                                        hikayeDetayViewModel.setBekleniyor(false)
                                    }

                                }
                                "hikayeSil" -> {
                                    hikayeDetayViewModel.setBekleniyor(true)
                                    hikayeDetayViewModel.hikayeyiSil {
                                        if (it) {
                                            hikayeDetayViewModel.setToastMesaj("Hikaye başarıyla silindi")
                                            hikayelerimSayfasinaGit()
                                        } else {
                                            hikayeDetayViewModel.setToastMesaj("Başarısız! Silinemedi")
                                        }
                                        hikayeDetayViewModel.setBekleniyor(false)
                                    }
                                }
                                "bolumDuzenle" -> {
                                    hikayeDetayViewModel.setDuzenleModuAcikMi(false)
                                    hikayeDetayViewModel.setBekleniyor(true)
                                    hikayeDetayViewModel.bolumuDuzenle {
                                        if (!it) {
                                            hikayeDetayViewModel.setToastMesaj("Başarısız! Düzenlenemedi")
                                            hikayeDetayViewModel.setBekleniyor(false)
                                        } else {
                                            if (hikayeDetayState.secilenBolum.bolumId == 0) {
                                                hikayeDetayViewModel.hikayeyiGetir(hikayeDetayState.hikaye.id!!) {
                                                    hikayeDetayViewModel.setBekleniyor(false)
                                                }
                                            } else {
                                                hikayeDetayViewModel.setBekleniyor(false)
                                            }
                                        }
                                    }
                                }
                                "bolumSil" -> {
                                    hikayeDetayViewModel.setBekleniyor(true)
                                    hikayeDetayViewModel.bolumSil {
                                        if (!it) {
                                            hikayeDetayViewModel.setToastMesaj("Başarısız")
                                        }
                                        hikayeDetayViewModel.setBekleniyor(false)
                                    }
                                }
                                "bolumYayinla" -> {
                                    hikayeDetayViewModel.bolumYayinla {
                                        if (!it) {
                                            hikayeDetayViewModel.setToastMesaj("Başarısız")
                                        }
                                        hikayeDetayViewModel.setBekleniyor(false)
                                    }
                                }
                                else -> {
                                    hikayeDetayViewModel.setBekleniyor(true)
                                    hikayeDetayViewModel.yeniBolumEkle(true) {
                                        if (it) {
                                            hikayeDetayViewModel.setSecilenBolumIndex(hikayeDetayState.secilenBolumIndex + 1)
                                            hikayeDetayViewModel.setDialogKontrol(false)
                                            hikayeDetayViewModel.setBekleniyor(false)
                                            hikayeDetayViewModel.setYeniBolumHikayesi("")
                                        } else {
                                            hikayeDetayViewModel.setToastMesaj("Başarısız")
                                        }
                                    }
                                }
                            }

                        } else {
                            hikayeDetayViewModel.setToastMesaj("Bağlantı hatası")
                        }
                    }
                ) {
                    Text(text = "Tamam", fontFamily = nunitoFontFamily)
                }
            }
        }
    )
}