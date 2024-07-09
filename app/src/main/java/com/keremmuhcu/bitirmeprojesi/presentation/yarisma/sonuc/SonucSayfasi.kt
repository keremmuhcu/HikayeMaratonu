package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.sonuc

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.outlined.Info
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keremmuhcu.bitirmeprojesi.R
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.sonuc.bilesenler.AlertDialoglar
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.sonuc.bilesenler.DigerItemler
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.sonuc.bilesenler.Hikaye
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.sonuc.bilesenler.IlkUcItem
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenk
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenkKoyu
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SonucSayfasi(
    anaSayfasinaGit:() -> Unit,
    yazarRol:String,
    anaHikaye:String
) {
    val sonucViewModel:SonucViewModel = viewModel()
    val sonucState by sonucViewModel.sonucState.collectAsState()
    val onaylanmisYarismaHikayeleri by sonucViewModel.onaylanmisYarismaHikayeleri.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(sonucState.toastMesaj) {
        if (sonucState.toastMesaj != "") {
            Toast.makeText(context, sonucState.toastMesaj, Toast.LENGTH_SHORT).show()
            sonucViewModel.setToastMesaj("")
        }
    }

    if (!sonucState.bekleniyor) {
        if (sonucState.katilimSaglanmisMi) {
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
                                        tint = AcikGri
                                    )
                                }

                                Text(
                                    modifier = Modifier.align(Alignment.Center),
                                    text = "Yarışma Sonuçları",
                                    color = AcikGri,
                                    fontFamily = nunitoFontFamily
                                )
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = {
                                    sonucViewModel.setAlertKontrol(true)
                                    if (yazarRol == "admin") {
                                        sonucViewModel.setGosterilecekAlert("bitir")
                                    } else {
                                        sonucViewModel.setGosterilecekAlert("bilgi")
                                    }
                                }
                            ) {
                                if (yazarRol == "admin") {
                                    Icon(
                                        painter = painterResource(id = R.drawable.accepted),
                                        contentDescription = "",
                                        tint = AcikGri
                                    )
                                } else {
                                    Icon(imageVector = Icons.Outlined.Info,
                                        contentDescription = "",
                                        tint = AcikGri
                                    )
                                }

                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = AnaRenkKoyu
                        )
                    )
                },
                content = {contentPadding->
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
                                    sonucViewModel = sonucViewModel,
                                    gorsel = painterResource(id = R.drawable.ikincilik_derecesi),
                                    kullaniciYarismaHikayesi = onaylanmisYarismaHikayeleri[1],
                                    yukseklik = 150,
                                    birinciMi = false
                                )
                                IlkUcItem(
                                    sonucViewModel = sonucViewModel,
                                    gorsel = painterResource(id = R.drawable.birincilik_derecesi),
                                    kullaniciYarismaHikayesi = onaylanmisYarismaHikayeleri[0],
                                    yukseklik = 200,
                                    birinciMi = true
                                )
                                IlkUcItem(
                                    sonucViewModel = sonucViewModel,
                                    gorsel = painterResource(id = R.drawable.ucunculuk_derecesi),
                                    kullaniciYarismaHikayesi = onaylanmisYarismaHikayeleri[2],
                                    yukseklik = 125,
                                    birinciMi = false
                                )
                            }

                        }

                        item {
                            val interactionSource = remember { MutableInteractionSource() }

                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp, start = 20.dp, end = 20.dp)
                            ) {
                                if (sonucState.kacinciOlundu != -1) {
                                    Card(
                                        modifier = Modifier
                                            .padding(bottom = 30.dp)
                                            .fillMaxWidth()
                                            .height(100.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color(0xFF81D4FA)
                                        )
                                    ) {
                                        if (sonucState.kacinciOlundu == 1 || sonucState.kacinciOlundu == 2 || sonucState.kacinciOlundu == 3) {
                                            Box(modifier = Modifier.fillMaxSize()) {
                                                Text(
                                                    modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                                                    text = if (sonucState.kacinciOlundu == 1) {
                                                        "Tebrikler Şampiyon!"
                                                    } else {
                                                        "Tebrikler Dereceye Girdiniz!"
                                                    },
                                                    textAlign = TextAlign.Center,
                                                    fontFamily = nunitoFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 18.sp
                                                )
                                            }
                                        }  else {
                                            Text(
                                                modifier = Modifier.fillMaxWidth(),
                                                text = "Yarışma Sonucunuz",
                                                textAlign = TextAlign.Center,
                                                fontFamily = nunitoFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                textDecoration = TextDecoration.Underline,
                                                fontSize = 18.sp
                                            )
                                            DigerItemler(
                                                sonucViewModel = sonucViewModel,
                                                kullaniciYarismaHikayesi = onaylanmisYarismaHikayeleri[sonucState.kacinciOlundu - 1],
                                                sira = sonucState.kacinciOlundu,
                                                sonMu = true
                                            )
                                            Spacer(modifier = Modifier.height(20.dp))
                                        }

                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) {
                                            sonucViewModel.setDigerleriniGoster(!sonucState.digerleriniGoster)
                                        },
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = "Diğerlerini Göster",
                                        fontSize = 20.sp,
                                        fontFamily = nunitoFontFamily,
                                        color = Color.Black
                                    )
                                    Icon(
                                        imageVector = if (sonucState.digerleriniGoster) {
                                            Icons.Filled.KeyboardArrowUp
                                        } else {
                                            Icons.Filled.KeyboardArrowDown
                                        },
                                        contentDescription = ""
                                    )
                                }
                                Spacer(modifier = Modifier
                                    .height(1.dp)
                                    .fillMaxWidth()
                                    .background(Color.Black)
                                )
                            }
                        }

                        items(onaylanmisYarismaHikayeleri.count()) {index->
                            if (index != 0 && index != 1 && index != 2 && sonucState.digerleriniGoster) {
                                var sonMu = false
                                if (onaylanmisYarismaHikayeleri.count() - 1 == index) {
                                    sonMu = true
                                }
                                DigerItemler(
                                    sonucViewModel = sonucViewModel,
                                    kullaniciYarismaHikayesi = onaylanmisYarismaHikayeleri[index],
                                    sira = index + 1,
                                    sonMu = sonMu
                                )
                            }

                        }

                    }

                }
            )

            if (sonucState.hikayeDialogKontrol) {
                Hikaye(anaHikaye = anaHikaye, sonucViewModel = sonucViewModel, sonucState = sonucState)
            }

            if (sonucState.alertKontrol) {
                AlertDialoglar(anaSayfasinaGit = anaSayfasinaGit, sonucViewModel = sonucViewModel, sonucState = sonucState, context = context)
            }
        } else {
            sonucViewModel.yarismayiSil {
                if (it) {
                    anaSayfasinaGit()
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
