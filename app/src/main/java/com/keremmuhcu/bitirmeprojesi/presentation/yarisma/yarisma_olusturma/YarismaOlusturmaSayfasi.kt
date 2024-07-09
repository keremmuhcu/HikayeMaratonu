package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.yarisma_olusturma

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenkKoyu
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YarismaOlusturmaSayfasi(
    anaSayfasinaGit:() -> Unit,
    //timerViewModel: TimerViewModel
) {
    val yarismaOlusturmaViewModel:YarismaOlusturmaViewModel = viewModel()
    val yarismaOlusturmaState by yarismaOlusturmaViewModel.yarismaOlusturmaState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(yarismaOlusturmaState.toastMesaj) {
        if (yarismaOlusturmaState.toastMesaj != "") {
            Toast.makeText(context, yarismaOlusturmaState.toastMesaj, Toast.LENGTH_SHORT).show()
            yarismaOlusturmaViewModel.setToastMesaj("")
        }
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
                                yarismaOlusturmaViewModel.setGosterilecekAlert("geri")
                                if (yarismaOlusturmaState.anaHikaye.isNotEmpty())
                                    yarismaOlusturmaViewModel.alertAcKapa(true)
                                else
                                    anaSayfasinaGit()
                            }
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "", tint = AcikGri)
                        }

                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Yarışma Başlat",
                            color = AcikGri
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AnaRenkKoyu
                ),
                actions = {
                    IconButton(
                        onClick = {
                            yarismaOlusturmaViewModel.setGosterilecekAlert("paylas")
                            yarismaOlusturmaViewModel.alertAcKapa(true)
                            //yarismaViewModel.kalanSureyiHesapla()
                        }
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "", tint = AcikGri)
                    }
                }

            )
        },
        content = {contentPadding->
            OutlinedTextField(
                modifier = Modifier
                    .background(AcikGri)
                    .padding(contentPadding)
                    .fillMaxSize()
                    .padding(10.dp, 30.dp),
                value = yarismaOlusturmaState.anaHikaye,
                onValueChange = {
                    yarismaOlusturmaViewModel.setHikaye(it)
                },
                placeholder = {
                    Text(
                        text = "Hikaye yazmaya başla...",
                        fontFamily = nunitoFontFamily,
                        fontWeight = FontWeight.Light
                    )
                },
                textStyle = TextStyle(
                    fontFamily = nunitoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp
                )
            )
        }
    )

    if (yarismaOlusturmaState.alertAcilisKontrol) {
        AlertDialog(
            onDismissRequest = {
                yarismaOlusturmaViewModel.alertAcKapa(false)
            },
            title = {
                if (yarismaOlusturmaState.gosterilecekAlert == "geri"){
                    Text(
                        text = "Yazılanlar kaybolacak",
                        fontFamily = nunitoFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                } else if (yarismaOlusturmaState.gosterilecekAlert == "paylas") {
                    Text(text = "Yarışmayı başlat")
                }
            },
            text = {
                if (yarismaOlusturmaState.gosterilecekAlert == "geri") {
                    Text(
                        text = "Yazılanlar sonrası için kaydedilmeyecektir. Şimdi çıkarsanız yazılanlar kaybolacaktır.",
                        fontFamily = nunitoFontFamily,
                        fontWeight = FontWeight.Light
                    )
                } else if (yarismaOlusturmaState.gosterilecekAlert == "paylas") {
                    TextField(
                        value = yarismaOlusturmaState.kacGunYarisilacak,
                        onValueChange = {
                            yarismaOlusturmaViewModel.setKacGunYarisilacak(it)
                        },
                        label = {
                            Text(text = "Kaç gün sürecek?")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(
                    modifier = Modifier.padding(10.dp),
                    onClick = {
                        yarismaOlusturmaViewModel.alertAcKapa(false)
                        if (yarismaOlusturmaState.gosterilecekAlert == "geri")
                            anaSayfasinaGit()
                        else if (yarismaOlusturmaState.gosterilecekAlert == "paylas") {
                            yarismaOlusturmaViewModel.setBekleniyor(true)
                            yarismaOlusturmaViewModel.yarismaHikayesiOlustur {
                                if (it){
                                    anaSayfasinaGit()
                                    /*timerViewModel.kalanSureyiHesapla("yarismaOlusturma") {basariliMi->
                                        if (basariliMi) {
                                            anaSayfasinaGit()
                                        }
                                    }

                                     */
                                }


                                yarismaOlusturmaViewModel.setBekleniyor(false)

                            }
                        }
                    }
                ) {
                    Text(text = "Tamam")
                }
            },
            dismissButton = {
                TextButton(
                    modifier = Modifier.padding(10.dp),
                    onClick = {
                        yarismaOlusturmaViewModel.alertAcKapa(false)
                        if (yarismaOlusturmaState.gosterilecekAlert == "paylas") {
                            yarismaOlusturmaViewModel.setKacGunYarisilacak("")
                        }
                    }
                ) {
                    Text(text = "İptal")
                }
            }
        )
    }

    if (yarismaOlusturmaState.bekleniyor) {
        Box(modifier = Modifier.fillMaxSize()){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
