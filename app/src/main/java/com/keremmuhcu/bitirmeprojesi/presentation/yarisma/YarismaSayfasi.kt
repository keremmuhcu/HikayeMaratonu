package com.keremmuhcu.bitirmeprojesi.presentation.yarisma

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keremmuhcu.bitirmeprojesi.presentation.timer.TimerState
import com.keremmuhcu.bitirmeprojesi.presentation.timer.TimerViewModel
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenkKoyu
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YarismaSayfasi(
    anaSayfasinaGit: () -> Unit,
    yarismaHikayesiSayfasinaGit: (String, String) -> Unit,
    yarismaOlusturmaSayfasinaGit: () -> Unit,
    sonucSayfasinaGit:(String, String) -> Unit,
    oylamaSayfasinaGit: (String,String) -> Unit,
    bitenYarismalarSayfasinaGit: () -> Unit,
    timerViewModel: TimerViewModel,
    timerState:TimerState
) {

    val yarismaViewModel:YarismaViewModel = viewModel()
    val yarismaState by yarismaViewModel.yarismaState.collectAsState()
    val yazar by yarismaViewModel.yazar.collectAsState()
    val yarisma by yarismaViewModel.yarisma.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(yarismaState.toastMesaj) {
        if (yarismaState.toastMesaj.isNotEmpty()) {
            yarismaViewModel.toastMesajGoster(context)
        }
    }

    if (!yarismaState.bekleniyor) {
        LaunchedEffect(Unit) {
            if (yarisma.anaHikaye != null && !yarisma.bittiMi!!) {
                timerViewModel.kalanSureyiHesapla()
            }
        }
        if (yarisma.anaHikaye != null && !yarisma.bittiMi!!) {
            when(timerState.asama) {
                "yarisma" -> {
                    timerViewModel.setAsama("")
                    yarismaHikayesiSayfasinaGit(yazar.kullaniciAdi,yazar.rol)
                }
                "oylama" -> {
                    timerViewModel.setAsama("")
                    oylamaSayfasinaGit(yarisma.anaHikaye!!, yazar.rol)
                }
                "sonuc" -> {
                    timerViewModel.setAsama("")
                    sonucSayfasinaGit(yarisma.anaHikaye!!, yazar.rol)
                }
            }
        } else if (yarisma.bittiMi!!) {
            YarismaYok(anaSayfasinaGit,bitenYarismalarSayfasinaGit, yarismaOlusturmaSayfasinaGit, yazar.rol)
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }


}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YarismaYok(
    anaSayfasinaGit: () -> Unit,
    bitenYarismalarSayfasinaGit: () -> Unit,
    yarismaOlusturmaSayfasinaGit: () -> Unit,
    yazarRol:String
) {
    if (yazarRol != "admin") {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        IconButton(onClick = anaSayfasinaGit) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "",
                                tint = AcikGri
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = AnaRenkKoyu
                    )
                )
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxSize()
                        ) {
                            Column(
                                modifier = Modifier.align(Alignment.TopStart)
                            ) {
                                Text(
                                    text = "Şu anda yarışma yok.",
                                    fontFamily = nunitoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp
                                )
                                Text(
                                    text = "İsterseniz daha önce yapılan yarışmalara göz atabilirsiniz.",
                                    fontFamily = nunitoFontFamily,
                                    fontWeight = FontWeight.Light,
                                    fontSize = 18.sp,
                                )
                            }

                            Button(
                                modifier = Modifier.align(Alignment.BottomEnd),
                                onClick = {
                                    bitenYarismalarSayfasinaGit()
                                }
                            ) {
                                Text(text = "Gözat")
                            }
                        }
                    }
                }
            }
        )
    } else {
        yarismaOlusturmaSayfasinaGit()
    }
}

