package com.keremmuhcu.bitirmeprojesi.presentation.hikaye.hikaye_detay

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.hikaye_detay.bilesenler.AlertDialogBileseni
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.hikaye_detay.bilesenler.BolumEkleDialog
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.hikaye_detay.bilesenler.DropdownDuzenleBileseni
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenkKoyu
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HikayeDetaySayfasi(
    hikayelerimSayfasinaGit:() -> Unit,
    hikayeId:String
) {
    val hikayeDetayViewModel:HikayeDetayViewModel = viewModel()
    val hikayeDetayState by hikayeDetayViewModel.hikayeDetayState
    val bolumlerListesi by hikayeDetayViewModel.bolumlerListesi
    val context = LocalContext.current

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val textScrollState = rememberScrollState()
    var kacinciBolum by remember { mutableStateOf("0") }

    if (hikayeDetayState.toastMesaj != "") {
        Toast.makeText(context,hikayeDetayState.toastMesaj,Toast.LENGTH_SHORT).show()
        hikayeDetayViewModel.setToastMesaj("")
    }
    
    LaunchedEffect(Unit) {
        println("Launch çalıştı")
        hikayeDetayViewModel.setVerilerAlindiMi(false)
        hikayeDetayViewModel.hikayeyiGetir(hikayeId) {hikayeBasari->
            if (hikayeBasari) {
                hikayeDetayViewModel.bolumleriGetir("aktif", hikayeId) {bolumlerBasari->
                    if (bolumlerBasari) {
                        hikayeDetayViewModel.setVerilerAlindiMi(true)
                        if (hikayeDetayState.veriEklenipSilinmeKontrolu == 0) {
                            hikayeDetayViewModel.setSecilenBolum(bolumlerListesi[0])
                        } else {

                            when {
                                hikayeDetayState.veriEklenipSilinmeKontrolu > bolumlerListesi.count() -> {
                                    hikayeDetayViewModel.setSecilenBolumIndex(hikayeDetayState.secilenBolumIndex - 1)
                                    hikayeDetayViewModel.setSecilenBolum(bolumlerListesi[hikayeDetayState.secilenBolumIndex])
                                }
                                hikayeDetayState.veriEklenipSilinmeKontrolu < bolumlerListesi.count() -> {
                                    hikayeDetayViewModel.setSecilenBolumIndex(bolumlerListesi.count() - 1)
                                    hikayeDetayViewModel.setSecilenBolum(bolumlerListesi[hikayeDetayState.secilenBolumIndex])
                                }
                                else -> {
                                    hikayeDetayViewModel.setSecilenBolum(bolumlerListesi[hikayeDetayState.secilenBolumIndex])
                                }
                            }
                        }
                        hikayeDetayViewModel.setVeriEklenipSilinmeKontrolu(bolumlerListesi.count())
                        hikayeDetayViewModel.setBekleniyor(false)
                    } else {
                        hikayeDetayViewModel.setToastMesaj("Başarısız! Yeniden deneyiniz")
                    }
                }
            } else {
                hikayeDetayViewModel.setToastMesaj("Başarısız! Hikaye bilgisi alınamadı")
            }
        }
    }
    
    if (hikayeDetayState.verilerAlindiMi) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            if (!hikayeDetayState.duzenleModuAcikMi) {
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
                            }

                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = if (drawerState.isOpen) {
                                    ""
                                } else {
                                    if (hikayeDetayState.secilenBolumIndex == 0) {
                                        "Giriş"
                                    } else if(hikayeDetayState.secilenBolumIndex == bolumlerListesi.count() - 1 && hikayeDetayState.hikaye.bittiMi!!) {
                                        "Final"
                                    } else {
                                        "${bolumlerListesi.indexOf(hikayeDetayState.secilenBolum)}. Bölüm"
                                    }
                                },
                                fontFamily = nunitoFontFamily,
                                color = AcikGri
                            )

                        }

                    },
                    actions = {
                        if (!hikayeDetayState.hikaye.bittiMi!! && drawerState.isClosed) {
                            IconButton(
                                onClick = {
                                    if (hikayeDetayState.duzenleModuAcikMi) {
                                        if (hikayeDetayViewModel.bolumunIcerigiDoluMu()) {
                                            hikayeDetayViewModel.setGosterilecekAlert("bolumDuzenle")
                                            hikayeDetayViewModel.setAlertKontrol(true)
                                        } else {
                                            hikayeDetayViewModel.setToastMesaj("Alanları eksiksiz doldurunuz")
                                        }

                                    } else {
                                        hikayeDetayViewModel.setDropdownDuzenleKontrol(true)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (hikayeDetayState.duzenleModuAcikMi) {
                                        Icons.Default.Done
                                    } else {
                                        Icons.Default.MoreVert
                                    },
                                    contentDescription = "",
                                    tint = AcikGri
                                )

                                if (hikayeDetayState.dropdownDuzenleKontrol) {
                                    DropdownDuzenleBileseni(
                                        hikayeDetayViewModel = hikayeDetayViewModel,
                                        hikayeDetayState = hikayeDetayState
                                    )
                                }
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
                        hikayeDetayViewModel.listenerlaBaglantiKes()
                        hikayelerimSayfasinaGit()
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
                                item {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp),
                                        text =
                                        if (hikayeDetayState.hikaye.bittiMi!!) {
                                            "${hikayeDetayState.hikaye.baslik!!} (Bitti)"
                                        } else {
                                            if (hikayeDetayState.hikaye.yayinlandiMi!!) {
                                                "${hikayeDetayState.hikaye.baslik!!} (Devam ediyor)"
                                            } else {
                                                "${hikayeDetayState.hikaye.baslik!!} (Yayınlanmadı)"
                                            }
                                        },
                                        fontFamily = nunitoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.Center
                                    )
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 30.dp, bottom = 15.dp),
                                        horizontalArrangement = Arrangement.SpaceAround
                                    ) {
                                        if (!hikayeDetayState.hikaye.bittiMi!!) {
                                            Button(
                                                onClick = {
                                                    kacinciBolum = bolumlerListesi.count().toString()
                                                    hikayeDetayViewModel.setDialogKontrol(true)
                                                    scope.launch { drawerState.close() }
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = AnaRenkKoyu
                                                )
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Add,
                                                        contentDescription = ""
                                                    )
                                                    Text(
                                                        text = "  Bölüm",
                                                        fontFamily = nunitoFontFamily
                                                    )

                                                }
                                            }
                                        }
                                        Button(
                                            onClick = {
                                                if (hikayeDetayState.hikaye.bittiMi!!) {
                                                    hikayeDetayViewModel.setGosterilecekAlert("devamEt")

                                                } else {
                                                    if (hikayeDetayState.hikaye.yayinlandiMi!!) {
                                                        hikayeDetayViewModel.setGosterilecekAlert("final")
                                                    } else {
                                                        hikayeDetayViewModel.setGosterilecekAlert("hikayeYayinla")
                                                    }
                                                }
                                                hikayeDetayViewModel.setAlertKontrol(true)
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = AnaRenkKoyu
                                            )
                                        ) {
                                            Text(
                                                text =
                                                if (hikayeDetayState.hikaye.bittiMi!!) {
                                                    "Devam et"
                                                } else {
                                                    if (hikayeDetayState.hikaye.yayinlandiMi!!) {
                                                        "Final yap"
                                                    } else {
                                                        "Yayınla"
                                                    }
                                                },
                                                fontFamily = nunitoFontFamily
                                            )
                                        }

                                        Button(
                                            onClick = {
                                                hikayeDetayViewModel.setGosterilecekAlert("hikayeSil")
                                                hikayeDetayViewModel.setAlertKontrol(true)
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color.Red
                                            )
                                        ) {
                                            Text(
                                                text = "Sil",
                                                fontFamily = nunitoFontFamily
                                            )
                                        }
                                    }

                                    if (hikayeDetayState.hikaye.yayinlandiMi!! && hikayeDetayViewModel.yayinlanacakVarMi()) {
                                        Button(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 10.dp, end = 10.dp, bottom = 30.dp),
                                            onClick = {
                                                hikayeDetayViewModel.setGosterilecekAlert("hepsiniYayinla")
                                                hikayeDetayViewModel.setAlertKontrol(true)
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = AnaRenkKoyu
                                            )
                                        ) {
                                            Text(text = "Hepsini yayınla", fontFamily =  nunitoFontFamily)
                                        }
                                    }
                                }
                                items(bolumlerListesi) { bolum->
                                    val index =  bolumlerListesi.indexOf(bolum)
                                    hikayeDetayViewModel.setSecilenBolumIndex(bolumlerListesi.indexOf(hikayeDetayState.secilenBolum))
                                    NavigationDrawerItem(
                                        label = {
                                            Text(
                                                text = if (bolum.bolumId!! == 0) {
                                                    "Giriş"
                                                } else {
                                                    if (bolum.yayinlandiMi!!) {
                                                        if (hikayeDetayState.hikaye.bittiMi!! && bolumlerListesi.lastIndex == index) {
                                                            "Final"
                                                        } else {
                                                            "${index}. Bölüm"
                                                        }
                                                    } else {
                                                        "${index}. Bölüm (yayınlanmadı)"
                                                    }
                                                },
                                                fontFamily = nunitoFontFamily
                                            )
                                        },
                                        selected = hikayeDetayState.secilenBolumIndex == index,
                                        onClick = {
                                            hikayeDetayViewModel.setSecilenBolum(bolum)
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

                        if (bolumlerListesi.indexOf(hikayeDetayState.secilenBolum) == 0) {
                            Column(
                                modifier = Modifier
                                    .background(AcikGri)
                                    .fillMaxSize()
                                    .padding(horizontal = 10.dp)
                                    .verticalScroll(textScrollState),
                            ) {
                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = hikayeDetayState.baslik,
                                    onValueChange = {
                                        hikayeDetayViewModel.setBaslik(it)
                                    },
                                    enabled = hikayeDetayState.duzenleModuAcikMi,
                                    textStyle = TextStyle(
                                        fontFamily = nunitoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.Center,
                                        color = Color.Black
                                    ),
                                    placeholder = {
                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = "Başlık giriniz",
                                            textAlign = TextAlign.Center,
                                            fontSize = 20.sp,
                                        )
                                    },
                                    maxLines = 1,
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedBorderColor = Color.Transparent,
                                        focusedBorderColor = Color.Transparent,
                                        disabledBorderColor = Color.Transparent
                                    )
                                )
                                Spacer(modifier = Modifier
                                    .height(1.dp)
                                    .fillMaxWidth()
                                    .padding(horizontal = 30.dp)
                                    .background(Color.Black)
                                )


                                OutlinedTextField(
                                    modifier = Modifier
                                        .padding(top = 15.dp)
                                        .fillMaxSize(),
                                    value = hikayeDetayState.duzenlenenHikaye,
                                    onValueChange = { deger->
                                        hikayeDetayViewModel.setDuzenlenenHikaye(deger)
                                    },
                                    enabled = hikayeDetayState.duzenleModuAcikMi,
                                    textStyle = TextStyle.Default.copy(
                                        fontFamily = nunitoFontFamily,
                                        color = Color.Black,
                                        fontSize = 18.sp
                                    ),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent,
                                        disabledBorderColor = Color.Transparent
                                    )
                                )
                            }

                        } else {
                            Box(modifier = Modifier
                                .background(AcikGri)
                                .fillMaxSize()
                            ) {
                                OutlinedTextField(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(textScrollState),
                                    value = hikayeDetayState.duzenlenenHikaye,
                                    onValueChange = { deger->
                                        hikayeDetayViewModel.setDuzenlenenHikaye(deger)
                                    },
                                    enabled = hikayeDetayState.duzenleModuAcikMi,
                                    textStyle = TextStyle.Default.copy(
                                        fontFamily = nunitoFontFamily,
                                        color = Color.Black,
                                        fontSize = 18.sp
                                    ),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent
                                    )
                                )
                            }
                        }

                    }
                )
            }
        )
    }

    if (hikayeDetayState.bekleniyor) {
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

    if (hikayeDetayState.dialogKontrol) {
        BolumEkleDialog(
            hikayeDetayState = hikayeDetayState,
            hikayeDetayViewModel = hikayeDetayViewModel,
            kacinciBolum =kacinciBolum,
            context = context
        )
    }

    if (hikayeDetayState.alertKontrol) {
        AlertDialogBileseni(
            hikayeDetayViewModel = hikayeDetayViewModel,
            hikayeDetayState = hikayeDetayState,
            context = context,
            hikayelerimSayfasinaGit = hikayelerimSayfasinaGit
        )
    }
}