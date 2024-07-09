package com.keremmuhcu.bitirmeprojesi.presentation.biten_yarismalar.biten_yarisma_detay

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keremmuhcu.bitirmeprojesi.R
import com.keremmuhcu.bitirmeprojesi.presentation.biten_yarismalar.biten_yarisma_detay.bilesenler.DigerItemler
import com.keremmuhcu.bitirmeprojesi.presentation.biten_yarismalar.biten_yarisma_detay.bilesenler.Hikaye
import com.keremmuhcu.bitirmeprojesi.presentation.biten_yarismalar.biten_yarisma_detay.bilesenler.IlkUcItem
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenk
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenkKoyu
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BitenYarismaDetay(
    bitenYarismalarSayfasinaGit:() -> Unit,
    id:String,
    anaHikaye:String,
    tarih:String
) {
    val bitenYarismaDetayViewModel: BitenYarismaDetayViewModel = viewModel()
    val bitenYarismaDetayState by bitenYarismaDetayViewModel.bitenYarismaDetayState.collectAsState()
    val onaylanmisYarismaHikayeleri by bitenYarismaDetayViewModel.onaylanmisYarismaHikayeleri.collectAsState()

    val context = LocalContext.current


    // sadece bir kere çalıştırdı burayı ve daha çalıştırmaz.
    LaunchedEffect(Unit) {
        bitenYarismaDetayViewModel.bitenYarismalariGetir(id)
    }

    LaunchedEffect(bitenYarismaDetayState.toastMesaj) {
        if (bitenYarismaDetayState.toastMesaj != "") {
            Toast.makeText(context, bitenYarismaDetayState.toastMesaj, Toast.LENGTH_SHORT).show()
            bitenYarismaDetayViewModel.setToastMesaj("")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterStart),
                            onClick = {
                                bitenYarismalarSayfasinaGit()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "",
                                tint = AcikGri
                            )
                        }

                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = tarih,
                            color = AcikGri
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AnaRenkKoyu
                )
            )
        },
        content = {contentPadding->
            if (bitenYarismaDetayState.bekleniyor) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(AnaRenk)) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .background(AnaRenk)
                        .fillMaxSize()
                        //.padding(contentPadding),
                ) {

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(contentPadding)
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            IlkUcItem(
                                bitenYarismaDetayViewModel = bitenYarismaDetayViewModel,
                                gorsel = painterResource(id = R.drawable.ikincilik_derecesi),
                                kullaniciYarismaHikayesi = onaylanmisYarismaHikayeleri[1],
                                yukseklik = 150,
                                birinciMi = false
                            )
                            IlkUcItem(
                                bitenYarismaDetayViewModel = bitenYarismaDetayViewModel,
                                gorsel = painterResource(id = R.drawable.birincilik_derecesi),
                                kullaniciYarismaHikayesi = onaylanmisYarismaHikayeleri[0],
                                yukseklik = 200,
                                birinciMi = true
                            )
                            IlkUcItem(
                                bitenYarismaDetayViewModel = bitenYarismaDetayViewModel,
                                gorsel = painterResource(id = R.drawable.ucunculuk_derecesi),
                                kullaniciYarismaHikayesi = onaylanmisYarismaHikayeleri[2],
                                yukseklik = 125,
                                birinciMi = false
                            )
                        }

                    }

                    item {
                        val interactionSource = remember { MutableInteractionSource() }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp, start = 20.dp, end = 20.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    bitenYarismaDetayViewModel.setDigerleriniGoster(!bitenYarismaDetayState.digerleriniGoster)
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Diğerlerini Göster",
                                fontSize = 20.sp,
                                fontFamily = nunitoFontFamily,
                                color = Color.Black
                            )
                            Icon(
                                imageVector = if (bitenYarismaDetayState.digerleriniGoster) {
                                    Icons.Filled.KeyboardArrowUp
                                } else {
                                    Icons.Filled.KeyboardArrowDown
                                },
                                contentDescription = ""
                            )
                        }
                        Spacer(modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(Color.Black)
                        )
                    }

                    items(onaylanmisYarismaHikayeleri.count()) {index->
                        if (index != 0 && index != 1 && index != 2 && bitenYarismaDetayState.digerleriniGoster) {
                            var sonMu = false
                            if (onaylanmisYarismaHikayeleri.count() - 1 == index) {
                                sonMu = true
                            }
                            DigerItemler(
                                bitenYarismaDetayViewModel = bitenYarismaDetayViewModel,
                                kullaniciYarismaHikayesi = onaylanmisYarismaHikayeleri[index],
                                sira = index + 1,
                                sonMu = sonMu
                            )
                        }

                    }

                }

            }
        }
    )

    if (bitenYarismaDetayState.hikayeDialogKontrol) {
        Hikaye(anaHikaye = anaHikaye, bitenYarismaDetayViewModel = bitenYarismaDetayViewModel, bitenYarismaDetayState = bitenYarismaDetayState)
    }
}