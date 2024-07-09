package com.keremmuhcu.bitirmeprojesi.presentation.profil.katilinan_yarismalar

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.keremmuhcu.bitirmeprojesi.presentation.profil.katilinan_yarismalar.bilesenler.DropdownBileseni
import com.keremmuhcu.bitirmeprojesi.presentation.profil.katilinan_yarismalar.bilesenler.HikayeyiGormeBileseni
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenk
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenkKoyu
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily
import com.keremmuhcu.bitirmeprojesi.util.InternetKontrol
import com.keremmuhcu.bitirmeprojesi.util.Tarih

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun KatilinanYarismalarSayfasi(
    email:String,
    kullaniciAdi:String,
    geriDon:() -> Unit
) {
    val katilinanYarismalarViewModel:KatilinanYarismalarViewModel = viewModel()
    val katilinanYarismalarState by katilinanYarismalarViewModel.katilinanYarismalarState.collectAsState()
    val yazarinYarismaHikayleriListesi by katilinanYarismalarViewModel.yazarinYarismaHikayleriListesi.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        katilinanYarismalarViewModel.yazarinKatildigiYarismalariGetir(email) { basariliMi->
            if (!basariliMi) {
                katilinanYarismalarViewModel.setToastMesaj("Bağlantı hatası! Yeniden deneyiniz.")
            }
        }
    }

    LaunchedEffect(katilinanYarismalarState.toastMesaj) {
        if (katilinanYarismalarState.toastMesaj != "") {
            Toast.makeText(context,katilinanYarismalarState.toastMesaj, Toast.LENGTH_SHORT).show()
            katilinanYarismalarViewModel.setToastMesaj("")
        }
    }

    if (katilinanYarismalarState.bekleniyor) {
        ProgressBarGostermeBileseni()
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            IconButton(
                                modifier = Modifier.align(Alignment.CenterStart),
                                onClick = {
                                    geriDon()
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
                                text = kullaniciAdi,
                                color = Color.White
                            )
                        }
                    },
                    actions = {
                        DropdownBileseni(
                            katilinanYarismalarState = katilinanYarismalarState,
                            katilinanYarismalarViewModel = katilinanYarismalarViewModel,
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = AnaRenkKoyu//MaterialTheme.colorScheme.primaryContainer
                    )

                )
            },
            content = {
                if (yazarinYarismaHikayleriListesi.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .background(AnaRenk)
                            .fillMaxSize()
                            .padding(it)
                            .padding(top = 15.dp)
                    ) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val gostergeler = listOf("Derece", "Başlık", "Oy")
                                for(gosterge in gostergeler) {
                                    Text(
                                        text = gosterge,
                                        fontFamily = nunitoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                    )
                                }
                            }
                        }
                        items(yazarinYarismaHikayleriListesi) {yarismaHikayesi->
                            Card(
                                modifier = Modifier
                                    .padding(10.dp),
                                onClick = {
                                    if (InternetKontrol.internetVarMi(context)) {
                                        katilinanYarismalarViewModel.setSecilenHikaye(yarismaHikayesi)
                                        katilinanYarismalarViewModel.tiklananHikayeninAnaHikayesiniGetir {basariliMi ->
                                            if (basariliMi) {
                                                katilinanYarismalarViewModel.setDialogKontrol(true)
                                            } else {
                                                katilinanYarismalarViewModel.setToastMesaj("Hata! Yeniden Deneyiniz.")
                                            }
                                        }
                                    } else {
                                        katilinanYarismalarViewModel.setToastMesaj("Bağlantı hatası.")
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

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 15.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        modifier = Modifier.padding(start = 15.dp),
                                        text = "${yarismaHikayesi.derece}.",
                                        textAlign = TextAlign.Center,
                                        fontFamily = nunitoFontFamily
                                    )
                                    Text(

                                        text = yarismaHikayesi.kullaniciYarismaHikayeBaslik!!,
                                        textAlign = TextAlign.Center,
                                        fontSize = 18.sp,
                                        fontFamily = nunitoFontFamily,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        modifier = Modifier.padding(end = 15.dp),
                                        text = "↑${yarismaHikayesi.oylar}",
                                        textAlign = TextAlign.Center,
                                        fontFamily = nunitoFontFamily
                                    )
                                }
                                Text(
                                    modifier = Modifier
                                        .padding(20.dp, 15.dp, 15.dp, 5.dp)
                                        .fillMaxWidth(),
                                    text = Tarih.tarihiFormatla(yarismaHikayesi.yarismaTarihi!!),
                                    fontFamily = nunitoFontFamily,
                                    fontSize = 12.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                    }
                } else {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(it)) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Hiç yarışmaya katılmamışsınız",
                            fontFamily = nunitoFontFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        )
    }

    if (katilinanYarismalarState.dialogKontrol) {
        HikayeyiGormeBileseni(
            katilinanYarismalarViewModel = katilinanYarismalarViewModel,
            katilinanYarismalarState = katilinanYarismalarState,
            anaHikaye = katilinanYarismalarState.tiklananYarismaAnaHikaye
        )
    }
}

@Composable
fun ProgressBarGostermeBileseni() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(AcikGri)) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}
