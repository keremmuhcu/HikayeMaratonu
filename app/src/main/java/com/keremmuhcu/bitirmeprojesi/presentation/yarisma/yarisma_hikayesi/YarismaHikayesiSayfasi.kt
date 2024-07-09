package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.yarisma_hikayesi

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keremmuhcu.bitirmeprojesi.R
import com.keremmuhcu.bitirmeprojesi.presentation.timer.TimerState
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.yarisma_hikayesi.bilesenler.DropdownMenuBileseni
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.yarisma_hikayesi.bilesenler.HikayeyiGormeBileseni
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenkKoyu
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YarismaHikayesiSayfasi(
    anaSayfasinaGit: () -> Unit,
    kullaniciYarHikOlusturmaSayfasinaGit: (String) -> Unit,
    onaylamaSayfasinaGit: () -> Unit,
    timerState: TimerState,
    yazarRol:String,
) {

    val yarismaHikayesiViewModel:YarismaHikayesiViewModel = viewModel()
    val yarismaHikayesiState by yarismaHikayesiViewModel.yarismaHikayesiState.collectAsState()
    val context = LocalContext.current

    if (timerState.asama == "oylama") {
        if (yarismaHikayesiState.kullaniciYarHikDurum == "gondermemis") {
            yarismaHikayesiViewModel.taslakTemizle()
        }
        SureBitti(anaSayfasinaGit)
    }

    LaunchedEffect(Unit) {
        yarismaHikayesiViewModel.kullaniciYarismaGondermisMi(yazarRol)
    }

    LaunchedEffect(yarismaHikayesiState.toastMesaj) {
        if (yarismaHikayesiState.toastMesaj != "") {
            Toast.makeText(context,yarismaHikayesiState.toastMesaj,Toast.LENGTH_SHORT).show()
            yarismaHikayesiViewModel.setToastMesaj("")
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterStart),
                            onClick = {
                                if (yarismaHikayesiState.duzenleModu) {
                                    yarismaHikayesiViewModel.setDuzenleModu(false)
                                } else {
                                    anaSayfasinaGit()
                                }
                            }
                        ) {
                            if (yarismaHikayesiState.duzenleModu) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription ="",
                                    tint = AcikGri
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription ="",
                                    tint = AcikGri
                                )
                            }
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
                    if (yazarRol == "admin") {
                        DropdownMenuBileseni(yarismaHikayesiState, yarismaHikayesiViewModel, anaSayfasinaGit)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AnaRenkKoyu
                )
            )
        },
        content = { it ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AcikGri)
                    .padding(it)
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (yazarRol != "admin") {
                    Text(
                        modifier = Modifier
                            .padding(0.dp, 0.dp, 0.dp, 40.dp)
                            .fillMaxWidth()
                            .weight(3f)
                            .verticalScroll(rememberScrollState()),
                        text = yarismaHikayesiState.yarismaHikayesi.anaHikaye!!,
                        fontFamily = nunitoFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                } else {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 0.dp, 0.dp, 40.dp)
                            .weight(3f)
                            .verticalScroll(rememberScrollState()),
                        value = yarismaHikayesiState.anaHikaye,
                        onValueChange = {
                            yarismaHikayesiViewModel.setAnaHikaye(it)
                        },
                        textStyle = TextStyle(
                            fontFamily = nunitoFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp,
                            color = Color.Black
                        ),
                        enabled = yarismaHikayesiState.duzenleModu,

                    )
                }

                if (!yarismaHikayesiState.duzenleModu) {
                    if (yarismaHikayesiState.kullaniciYarHikDurum != "gondermemis") {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row {
                                Text(
                                    modifier = Modifier.padding(end = 15.dp),
                                    text =
                                        if (yazarRol == "yazar") {
                                            when (yarismaHikayesiState.kullaniciYarHikDurum) {
                                                "bekliyor" -> {
                                                    "\"Hikayeniz onay sürecinde.\""
                                                }
                                                "onaylandi" -> {
                                                    "\"Hikayeniz kabul edildi.\""
                                                }
                                                else -> {
                                                    "\"Hikayeniz reddedildi.\""
                                                }
                                            }
                                        } else {
                                            "\"Hikayenizi yazdınız.\""
                                        }
                                    ,
                                    fontFamily = nunitoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Icon(
                                    painter =
                                        if (yazarRol == "yazar") {
                                            when (yarismaHikayesiState.kullaniciYarHikDurum) {
                                                "bekliyor" -> {
                                                    painterResource(id = R.drawable.waiting)
                                                }
                                                "onaylandi" -> {
                                                    painterResource(id = R.drawable.accepted)
                                                }
                                                else -> {
                                                    painterResource(id = R.drawable.declined)
                                                }
                                            }
                                        } else {
                                            painterResource(id = R.drawable.accepted)
                                        }
                                    ,
                                    contentDescription = "",
                                    tint =
                                    if (yazarRol == "yazar") {
                                        when (yarismaHikayesiState.kullaniciYarHikDurum) {
                                            "bekliyor" -> {
                                                Color.Black
                                            }
                                            "onaylandi" -> {
                                                Color.Green
                                            }
                                            else -> {
                                                Color.Red
                                            }
                                        }
                                    } else {
                                        Color.Green
                                    }


                                )
                            }
                            Spacer(modifier = Modifier.height(15.dp))
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    yarismaHikayesiViewModel.kullanicininHikayesiniGetir(yazarRol)
                                }
                            ) {
                                Text(text = "Hikayemi Gör")
                            }
                        }
                    } else {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                kullaniciYarHikOlusturmaSayfasinaGit(yarismaHikayesiState.anaHikaye)
                            }
                        ) {
                            Text(text = "Devam Ettir")
                        }
                    }


                    if (yazarRol != "yazar") {
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                onaylamaSayfasinaGit()
                            }
                        ) {
                            Text(text = "Onay Bekleyenler")
                        }
                    }
                }
            }
        }
    )

    if (yarismaHikayesiState.hikayeyiGormeKontrolu) {
        HikayeyiGormeBileseni(
            yarismaHikayesiViewModel = yarismaHikayesiViewModel,
            yarismaHikayesiState = yarismaHikayesiState
        )
    }

    if (yarismaHikayesiState.bekleniyor) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
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
                Text(text = "Oylama Başladı.")
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
