package com.keremmuhcu.bitirmeprojesi.presentation.biten_yarismalar

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenk
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenkKoyu
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily
import com.keremmuhcu.bitirmeprojesi.util.InternetKontrol
import com.keremmuhcu.bitirmeprojesi.util.Tarih

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BitenYarismalar(
    anaSayfasinaGit:() -> Unit,
    bitenYarismaDetaySayfasinaGit:(String,String,String) -> Unit,
    hikayeleriKesfetSayfasinaGit:() -> Unit
) {
    val bitenYarismalarViewModel:BitenYarismalarViewModel = viewModel()
    val bitenYarismalarState by bitenYarismalarViewModel.bitenYarismalarState.collectAsState()
    val bitenYarismalarListesi by bitenYarismalarViewModel.bitenYarismalarListesi.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(bitenYarismalarState.toastMesaj) {
        if (bitenYarismalarState.toastMesaj != "") {
            Toast.makeText(context, bitenYarismalarState.toastMesaj, Toast.LENGTH_SHORT).show()
            bitenYarismalarViewModel.setToastMesaj("")
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
                                anaSayfasinaGit()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "",
                                tint = Color.White

                            )
                        }

                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Biten Yarışmalar",
                            fontFamily = nunitoFontFamily,
                            color = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AnaRenkKoyu//MaterialTheme.colorScheme.primaryContainer
                )

            )
        },
        content = {
            if (bitenYarismalarState.bekleniyor) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(AnaRenk)) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }  else {
                if (bitenYarismalarListesi.isEmpty()) {
                    BosListe(hikayeleriKesfetSayfasinaGit)
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .background(AnaRenk)
                            .fillMaxSize()
                            .padding(it)
                    ) {
                        items(bitenYarismalarListesi) {bitenYarisma->

                            Card(
                                modifier = Modifier
                                    .padding(10.dp),
                                onClick = {
                                    if (InternetKontrol.internetVarMi(context)) {
                                        bitenYarismaDetaySayfasinaGit(bitenYarisma.id!!, bitenYarisma.anaHikaye!!, Tarih.tarihiFormatla(bitenYarisma.baslamaTarihi!!))
                                    } else {
                                        bitenYarismalarViewModel.setToastMesaj("Bağlantı hatası.")
                                    }
                                },
                                colors = CardDefaults.cardColors(
                                    containerColor = AcikGri
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 25.dp
                                ),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(top = 15.dp)
                                        .fillMaxWidth(),
                                    text = Tarih.tarihiFormatla(bitenYarisma.baslamaTarihi!!),
                                    textAlign = TextAlign.Center,
                                    fontFamily = nunitoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp,
                                    color = Color.Black
                                )
                                Text(
                                    modifier = Modifier.padding(15.dp),
                                    text = bitenYarisma.anaHikaye!!,
                                    fontFamily = nunitoFontFamily,
                                    fontSize = 16.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}


@Composable
fun BosListe(
    hikayeleriKesfetSayfasinaGit: () -> Unit
) {
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
                        text = "Daha önce yarışma düzenlenmemiş.",
                        fontFamily = nunitoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    Text(
                        text = "İsterseniz kullanıcıların kendi hikayelerine göz atabilirsiniz..",
                        fontFamily = nunitoFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 18.sp,
                    )
                }

                Button(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    onClick = {
                        hikayeleriKesfetSayfasinaGit()
                    }
                ) {
                    Text(text = "Gözat")
                }
            }
        }
    }
}