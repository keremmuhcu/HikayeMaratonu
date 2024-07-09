package com.keremmuhcu.bitirmeprojesi.presentation.hikaye.hikaye_oku

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keremmuhcu.bitirmeprojesi.R
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenkKoyu
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily
import com.keremmuhcu.bitirmeprojesi.util.InternetKontrol
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HikayeOkuSayfasi(
    email:String,
    hikayeId:String,
    profilZiyaretSayfasinaGit:() -> Unit
) {
    val hikayeOkuViewModel:HikayeOkuViewModel = viewModel()
    val hikayeOkuState by hikayeOkuViewModel.hikayeOkuState
    val bolumlerListesi by hikayeOkuViewModel.bolumlerListesi
    val context = LocalContext.current

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val textScrollState = rememberScrollState()

    if (hikayeOkuState.toastMesaj != "") {
        Toast.makeText(context, hikayeOkuState.toastMesaj, Toast.LENGTH_SHORT).show()
        hikayeOkuViewModel.setToastMesaj("")
    }

    LaunchedEffect(Unit) {
        println("Hikaye id ve email: ${hikayeId}  ${email}")
        hikayeOkuViewModel.setBekleniyor(true)

        hikayeOkuViewModel.kutuphanedeMi(hikayeId) {
            if (it) {
                if (!hikayeOkuState.kutuphanedeMi) {
                    hikayeOkuViewModel.hikayeyiGetir(hikayeId,email) { hikayeGeldiMi ->
                        if (hikayeGeldiMi) {
                            hikayeOkuViewModel.bolumleriGetir(hikayeId,email) {bolumlerGeldiMi ->
                                if (bolumlerGeldiMi) {
                                    hikayeOkuViewModel.setBekleniyor(false)
                                } else {
                                    hikayeOkuViewModel.setToastMesaj("Başarısız! Yeniden deneyin")
                                }
                            }
                        }
                    }
                } else {
                    hikayeOkuViewModel.setKalinanBolum(hikayeOkuState.hikaye.kalinanBolum)
                    hikayeOkuViewModel.bolumleriGetir(hikayeId,email) {bolumlerGeldiMi ->
                        if (bolumlerGeldiMi) {
                            if (hikayeOkuState.kalinanBolum >= bolumlerListesi.count()) {
                                hikayeOkuViewModel.setKalinanBolum(bolumlerListesi.count() - 1)
                            }
                            hikayeOkuViewModel.setBekleniyor(false)
                        } else {
                            hikayeOkuViewModel.setToastMesaj("Başarısız! Yeniden deneyin")
                        }
                    }
                }
            }

        }

    }

    if (hikayeOkuState.bekleniyor) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AcikGri)
                .clickable(enabled = false) {}
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            IconButton(
                                onClick = {
                                    if (drawerState.isOpen)
                                        scope.launch { drawerState.close() }
                                    else {
                                        scope.launch { drawerState.open() }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "",
                                    tint = AcikGri
                                )
                            }

                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = if (drawerState.isOpen) {
                                    hikayeOkuState.hikaye.baslik!!
                                } else {
                                    if (hikayeOkuState.kalinanBolum == 0) {
                                        "Giriş"
                                    } else if (hikayeOkuState.hikaye.bittiMi!! && bolumlerListesi.lastIndex == hikayeOkuState.kalinanBolum) {
                                        "Final"
                                    } else {
                                        "${hikayeOkuState.kalinanBolum}. Bölüm"

                                    }
                                },
                                fontFamily = nunitoFontFamily,
                                color = AcikGri
                            )
                        }
                    },
                    actions = {
                        if (hikayeOkuViewModel.getAktifKullaniciEmail() != hikayeOkuState.hikaye.yazarEmail) {
                            IconButton(
                                onClick = {
                                    if (InternetKontrol.internetVarMi(context)) {
                                        hikayeOkuViewModel.hikayeyiKutuphaneyeEkleCikar(!hikayeOkuState.kutuphanedeMi) {
                                            if (it) {
                                                if (hikayeOkuState.kutuphanedeMi) {
                                                    hikayeOkuViewModel.setToastMesaj("Hikaye kütüphaneye eklendi.")
                                                } else {
                                                    hikayeOkuViewModel.setToastMesaj("Hikaye kütüphaneden kaldırıldı.")
                                                }
                                            }
                                        }
                                    } else {
                                        hikayeOkuViewModel.setToastMesaj("Bağlantı hatası")
                                    }
                                }
                            ) {
                                Icon(modifier = Modifier.size(24.dp),
                                    painter = painterResource(
                                        id = if (hikayeOkuState.kutuphanedeMi) {
                                            R.drawable.kutuphane_cikar
                                        } else {
                                            R.drawable.kutuphane_ekle
                                        }
                                    ),
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
            content = {
                BackHandler {
                    if (drawerState.isOpen) {
                        scope.launch { drawerState.close() }
                    } else {
                        if (hikayeOkuState.kutuphanedeMi) {
                            hikayeOkuViewModel.kalinanBolumuKaydet {
                                if (it) {
                                    profilZiyaretSayfasinaGit()
                                } else {
                                    hikayeOkuViewModel.setToastMesaj("Bilgiler kaydedilemedi")
                                    profilZiyaretSayfasinaGit()
                                }
                            }
                        } else {
                            profilZiyaretSayfasinaGit()
                        }
                    }
                }

                ModalNavigationDrawer(
                    modifier = Modifier
                        .background(AcikGri)
                        .fillMaxSize()
                        .padding(it),
                    drawerState = drawerState,
                    gesturesEnabled = false,
                    drawerContent = {
                        ModalDrawerSheet(drawerContainerColor = Color.LightGray) {
                            LazyColumn {
                                items(bolumlerListesi) {bolum->
                                    val index =  bolumlerListesi.indexOf(bolum)
                                    NavigationDrawerItem(
                                        label = {
                                            Text(
                                                text = if (bolum.bolumId!! == 0) {
                                                    "Giriş"
                                                } else if (hikayeOkuState.hikaye.bittiMi!! && bolumlerListesi.lastIndex == index) {
                                                    "Final"
                                                } else {
                                                    "${index}. Bölüm"

                                                },
                                                fontFamily = nunitoFontFamily
                                            )
                                        },
                                        selected = hikayeOkuState.kalinanBolum == index,
                                        onClick = {
                                            hikayeOkuViewModel.setKalinanBolum(index)
                                            scope.launch { drawerState.close() }
                                        },
                                        shape = RoundedCornerShape(25)
                                    )
                                    if (index < bolumlerListesi.count() - 1) {
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                }
                            }
                        }
                    },
                    content = {
                        if (bolumlerListesi.isNotEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(AcikGri)
                                    .verticalScroll(textScrollState)
                                    .padding(20.dp)
                            ) {

                                if (hikayeOkuState.kalinanBolum == 0) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 10.dp),
                                        text = hikayeOkuState.hikaye.baslik!!,
                                        textAlign = TextAlign.Center,
                                        fontFamily = nunitoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )

                                }
                                Text(
                                    text = bolumlerListesi[hikayeOkuState.kalinanBolum].hikaye!!,
                                    fontFamily = nunitoFontFamily,
                                    color = Color.Black
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp),
                                ) {
                                    if (hikayeOkuState.kalinanBolum != 0) {
                                        Button(
                                            modifier = Modifier.align(Alignment.TopStart),
                                            onClick = {
                                                scope.launch {textScrollState.scrollTo(0)}
                                                hikayeOkuViewModel.setKalinanBolum(hikayeOkuState.kalinanBolum - 1)
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = AnaRenkKoyu
                                            )
                                        ) {
                                            Text(text = "Önceki", fontFamily = nunitoFontFamily)
                                        }
                                    }

                                    if (hikayeOkuState.kalinanBolum != bolumlerListesi.lastIndex) {
                                        Button(
                                            modifier = Modifier.align(Alignment.TopEnd),
                                            onClick = {
                                                scope.launch {textScrollState.scrollTo(0)}
                                                hikayeOkuViewModel.setKalinanBolum(hikayeOkuState.kalinanBolum + 1)
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = AnaRenkKoyu
                                            )
                                        ) {
                                            Text(text = "Sonraki",fontFamily = nunitoFontFamily)
                                        }
                                    }

                                }

                            }
                        }

                    }
                )
            }
        )
    }
}