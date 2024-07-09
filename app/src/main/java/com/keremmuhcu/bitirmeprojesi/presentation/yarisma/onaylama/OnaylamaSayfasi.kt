package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.onaylama

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
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
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.onaylama.bilesenler.OnaylamaPenceresi
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenk
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenkKoyu
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OnaylamaSayfasi(
    yarismaHikayesiSayfasinaGit:() -> Unit
) {
    val onaylamaViewModel:OnaylamaViewModel = viewModel()
    val onaylamaState by onaylamaViewModel.onaylamaState.collectAsState()
    val bekleyenYarismaHikayeleri by onaylamaViewModel.bekleyenYarismaHikayeleriListesi.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(onaylamaState.toastMesaj) {
        if (onaylamaState.toastMesaj != "") {
            Toast.makeText(context, onaylamaState.toastMesaj, Toast.LENGTH_SHORT).show()
            onaylamaViewModel.setToastMesaj("")
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
                                yarismaHikayesiSayfasinaGit()
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
                            text = "Onay Bekleyenler",
                            color = AcikGri
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AnaRenkKoyu
                )

            )
        },
        content = {
            if (onaylamaState.bekleniyor) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(AnaRenk)
                    .padding(it)
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else {
                if (bekleyenYarismaHikayeleri.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(AnaRenk)
                            .padding(it)
                            .padding(15.dp)

                    ) {
                        Card(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .height(200.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = AcikGri
                            )
                        ) {
                            Column {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Onaylanacak hikaye yok.",
                                    fontFamily = nunitoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 30.dp),
                                    text = "Yazarlar hikayelerini yazdıkça buraya düşecektir. Kontrol etmeyi unutmayınız.",
                                    fontFamily = nunitoFontFamily,
                                    fontWeight = FontWeight.Light,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                            .background(AnaRenk),
                        content = {
                            items(bekleyenYarismaHikayeleri) {
                                Card(
                                    modifier = Modifier
                                        .padding(10.dp),
                                    onClick = {
                                        onaylamaViewModel.setSecilenHikaye(it)
                                        onaylamaViewModel.setOnaylamaPencereKontrol(true)
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
                                        text = it.kullaniciYarismaHikayeBaslik.toString(),
                                        textAlign = TextAlign.Center,
                                        fontFamily = nunitoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 22.sp,
                                        color = Color.Black
                                    )
                                    Text(
                                        modifier = Modifier.padding(15.dp),
                                        text = it.kullaniciYarismaHikayesi.toString(),
                                        fontFamily = nunitoFontFamily,
                                        fontSize = 16.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Text(
                                        modifier = Modifier
                                            .padding(15.dp)
                                            .fillMaxWidth(),
                                        text = "@${it.yazarKullaniciAdi.toString()}",
                                        textAlign = TextAlign.End,
                                        fontFamily = nunitoFontFamily,
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    )

    if (onaylamaState.onaylamaPencereKontrol) {
        OnaylamaPenceresi(
            onaylamaViewModel = onaylamaViewModel,
            onaylamaState = onaylamaState,
        )
    }
}