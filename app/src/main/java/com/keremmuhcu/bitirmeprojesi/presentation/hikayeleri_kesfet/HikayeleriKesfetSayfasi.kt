package com.keremmuhcu.bitirmeprojesi.presentation.hikayeleri_kesfet

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenk
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenkKoyu
import com.keremmuhcu.bitirmeprojesi.ui.theme.BittiYesil
import com.keremmuhcu.bitirmeprojesi.ui.theme.DevamEdiyorMavi
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily
import com.keremmuhcu.bitirmeprojesi.util.InternetKontrol

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HikayeleriKesfetSayfasi(
    geriDon:() -> Unit,
    hikayeOkuSayfasinaGit:(String,String) -> Unit
) {
    val hikayeleriKesfetViewModel: HikayeleriKesfetViewModel = viewModel()
    val hikayeleriKesfetState by hikayeleriKesfetViewModel.hikayeleriKesfetState
    val hikayelerListesi by hikayeleriKesfetViewModel.hikayelerListesi
    val girisBolumuListesi by hikayeleriKesfetViewModel.girisBolumuListesi
    val context = LocalContext.current

    if (hikayeleriKesfetState.toastMesaj != "") {
        Toast.makeText(context, hikayeleriKesfetState.toastMesaj, Toast.LENGTH_SHORT).show()
        hikayeleriKesfetViewModel.setToastMesaj("")
    }
    
    LaunchedEffect(Unit) {
        hikayeleriKesfetViewModel.rastgeleHikayelerGetir()
    }
    
    if (!hikayeleriKesfetState.bekleniyor) {
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
                                    tint = AcikGri

                                )
                            }

                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = "Hikayeleri Keşfet",
                                fontFamily = nunitoFontFamily,
                                color = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                if (InternetKontrol.internetVarMi(context)) {
                                    hikayeleriKesfetViewModel.rastgeleHikayelerGetir()
                                } else {
                                    hikayeleriKesfetViewModel.setToastMesaj("Bağlantı hatası")
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
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
                if (hikayelerListesi.isEmpty() || girisBolumuListesi.isEmpty()) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(it)) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Hiç hikaye yazılmamış",
                            fontFamily = nunitoFontFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .background(AnaRenk)
                            .fillMaxSize()
                            .padding(it)
                    ) {
                        items(hikayelerListesi) {hikaye->
                            val index = hikayelerListesi.indexOf(hikaye)
                            val giris = girisBolumuListesi[index]
                            val containerColor = if (hikaye.bittiMi!!) {
                                BittiYesil
                            } else {
                                DevamEdiyorMavi
                            }
                            Card(
                                modifier = Modifier
                                    .padding(10.dp),
                                onClick = {
                                    if (InternetKontrol.internetVarMi(context)) {
                                        hikayeOkuSayfasinaGit(hikaye.id!!, hikaye.yazarEmail!!)
                                    } else {
                                        hikayeleriKesfetViewModel.setToastMesaj("Bağlantı hatası")
                                    }
                                },
                                colors = CardDefaults.cardColors(
                                    containerColor = containerColor
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
                                    text = hikaye.baslik!!,
                                    textAlign = TextAlign.Center,
                                    fontFamily = nunitoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp,
                                    color = Color.Black
                                )
                                Text(
                                    modifier = Modifier.padding(15.dp),
                                    text = giris.hikaye!!,
                                    fontFamily = nunitoFontFamily,
                                    fontSize = 16.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    modifier = Modifier.fillMaxWidth().padding(top = 30.dp, bottom = 15.dp, start = 15.dp, end = 15.dp),
                                    text = "@" + hikaye.yazarKullaniciAdi!!,
                                    textAlign = TextAlign.End,
                                    fontFamily = nunitoFontFamily,
                                    fontSize = 16.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            },
            containerColor = AnaRenk
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f)
                .background(AcikGri)
                .clickable(enabled = false) {}
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}