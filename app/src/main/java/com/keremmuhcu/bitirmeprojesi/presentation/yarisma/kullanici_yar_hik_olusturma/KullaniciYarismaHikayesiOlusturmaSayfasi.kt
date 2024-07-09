package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.kullanici_yar_hik_olusturma

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keremmuhcu.bitirmeprojesi.presentation.timer.TimerState
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.kullanici_yar_hik_olusturma.bilesenler.DropdownMenuBileseni
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenkKoyu
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun KullaniciYarismaHikayesiOlusturmaSayfasi(
    anaSayfasinaGit:() -> Unit,
    yarismaHikayesiSayfasinaGit:() -> Unit,
    anaHikaye:String,
    yazarKullaniciAdi:String,
    yazarRol:String,
    timerState: TimerState
) {
    val kullaniciYarHikOlusturmaViewModel:KullaniciYarHikOlusturmaViewModel = viewModel()
    val kullaniciYarHikOlusturmaState by kullaniciYarHikOlusturmaViewModel.kullaniciYarHikOlusturmaState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(kullaniciYarHikOlusturmaState.toastMesaj) {
        if (kullaniciYarHikOlusturmaState.toastMesaj != "") {
            Toast.makeText(context,kullaniciYarHikOlusturmaState.toastMesaj,Toast.LENGTH_SHORT).show()
            kullaniciYarHikOlusturmaViewModel.setToastMesaj("")
        }
    }

    if (timerState.asama == "oylama") {
        kullaniciYarHikOlusturmaViewModel.taslakTemizle()
        SureBitti(anaSayfasinaGit)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterStart),
                            onClick = {
                                kullaniciYarHikOlusturmaViewModel.setGosterilecekAlert("geriDon")
                                kullaniciYarHikOlusturmaViewModel.setAlertKontrol(true)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "",
                                tint = AcikGri
                            )
                        }

                        if (timerState.asama != "oylama") {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = timerState.gosterim,
                                color = AcikGri
                            )
                        }
                    }
                },
                actions = {
                    DropdownMenuBileseni(
                        anaSayfasinaGit,
                        kullaniciYarHikOlusturmaState = kullaniciYarHikOlusturmaState,
                        kullaniciYarHikOlusturmaViewModel = kullaniciYarHikOlusturmaViewModel,
                        yazarKullaniciAdi = yazarKullaniciAdi,
                        yazarRol = yazarRol
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AnaRenkKoyu
                )
            )
        },
        content = {
            if (kullaniciYarHikOlusturmaState.bekleniyor) {
                Box(
                    modifier = Modifier.fillMaxSize().background(AcikGri)
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else {
                if (kullaniciYarHikOlusturmaState.hata) {
                    anaSayfasinaGit()
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(AcikGri)
                            .padding(it)
                            .padding(25.dp)
                            .verticalScroll(rememberScrollState(), reverseScrolling = true),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = kullaniciYarHikOlusturmaState.baslik,
                            onValueChange = {
                                kullaniciYarHikOlusturmaViewModel.setBaslik(it)
                            },
                            textStyle = TextStyle(
                                fontFamily = nunitoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            ),
                            placeholder = {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Başlık giriniz...",
                                    textAlign = TextAlign.Center,
                                    fontSize = 20.sp
                                )
                            },
                            maxLines = 1,
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent,
                            )
                        )
                        Spacer(modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(Color.Black))

                        Text(
                            modifier = Modifier.padding(0.dp, 40.dp),
                            text = anaHikaye,
                            fontFamily = nunitoFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp,
                            color = Color.Black
                        )

                        BasicTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = kullaniciYarHikOlusturmaState.kullaniciYarismaHikayesi,
                            onValueChange = {
                                kullaniciYarHikOlusturmaViewModel.setHikaye(it)
                            },
                            textStyle = TextStyle(
                                fontFamily = nunitoFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp,
                            ),
                            decorationBox = {
                                if (kullaniciYarHikOlusturmaState.kullaniciYarismaHikayesi.isEmpty()) {
                                    Text(text = "Hikayeyi yazmaya başlayın...")
                                }
                                it()
                            }

                        )
                    }
                }
            }
        }
    )

    if (kullaniciYarHikOlusturmaState.alertKontrol && kullaniciYarHikOlusturmaState.gosterilecekAlert == "geriDon") {
        AlertDialog(
            onDismissRequest = {
                kullaniciYarHikOlusturmaViewModel.setAlertKontrol(false)
            },
            title = {
                Text(text = "Kaydedilmemiş değişiklikler")
            },
            text = {
                Text(text = "Bu işlem geri alınamaz.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        kullaniciYarHikOlusturmaViewModel.setAlertKontrol(false)
                        yarismaHikayesiSayfasinaGit()
                    }
                ) {
                    Text(text = "Tamam")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        kullaniciYarHikOlusturmaViewModel.setAlertKontrol(false)
                    }
                ) {
                    Text(text = "İptal")
                }
            }
        )
    }
}

@Composable
fun SureBitti(
    anaSayfasinaGit: () -> Unit
) {
    var acKapaKontrol by remember {
        mutableStateOf(true)
    }
    if (acKapaKontrol) {
        AlertDialog(
            title = {
                Text(text = "Süre Bitti!")
            },
            text = {
                Text(text = "Yaptığınız değişiklikler kaydedilmeyecek. Yetiştiremediniz.")
            },
            onDismissRequest = {
                acKapaKontrol = false
                anaSayfasinaGit()
            },
            confirmButton = {
                Button(
                    onClick = {
                        acKapaKontrol = false
                        anaSayfasinaGit()
                    }
                ) {
                Text(text = "Tamam")
                }
            }
        )
    }
}
