package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.oylama

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keremmuhcu.bitirmeprojesi.R
import com.keremmuhcu.bitirmeprojesi.data.models.KullaniciYarismaHikayesi
import com.keremmuhcu.bitirmeprojesi.presentation.timer.TimerState
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.oylama.bilesenler.DropdownMenuBileseni
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenk
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenkKoyu
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily
import com.keremmuhcu.bitirmeprojesi.util.InternetKontrol

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OylamaSayfasi(
    anaHikaye: String,
    yazarRol:String,
    anaSayfayaGit:() -> Unit,
    bitenYarismalarSayfasinaGit: () -> Unit,
    timerState: TimerState
) {
    val oylamaViewModel:OylamaViewModel = viewModel()
    val oylamaState by oylamaViewModel.oylamaState.collectAsState()
    val onaylanmisYarismaHikayeleri by oylamaViewModel.onaylanmisYarismaHikayeleri.collectAsState()
    val context = LocalContext.current


    if (timerState.kalanZaman == 0L) {
        anaSayfayaGit()
    }

    LaunchedEffect(oylamaState.toastMesaj) {
        if (oylamaState.toastMesaj != "") {
            Toast.makeText(context, oylamaState.toastMesaj, Toast.LENGTH_SHORT).show()
            oylamaViewModel.setToastMesaj("")
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterStart),
                            onClick = {
                                anaSayfayaGit()
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
                            text = timerState.gosterim,
                            color = AcikGri
                        )
                    }
                },
                actions = {
                    if (oylamaState.katilimSaglanmisMi) {
                        IconButton(
                            onClick = { oylamaViewModel.setInfoButonunaTiklandiMi(true) }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
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
                anaSayfayaGit()
            }
            if (!oylamaState.bekleniyor) {
                if (oylamaState.katilimSaglanmisMi) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                            .background(AnaRenk)
                    ){

                        LazyColumn{
                            item {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = AcikGri
                                    ),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 25.dp
                                    ),
                                    shape = RoundedCornerShape(20.dp)
                                ) {
                                    Column {
                                        Image(
                                            modifier = Modifier.fillMaxWidth(),
                                            painter = painterResource(id = R.drawable.yarisma_bitti),
                                            contentDescription = "",
                                            contentScale = ContentScale.Crop,
                                            alpha = 0.6f
                                        )
                                    }
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 15.dp),
                                        text = "Oylama Zamanı!",
                                        fontFamily = nunitoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 30.sp,
                                        textAlign = TextAlign.Center,
                                        color = Color.Black
                                    )

                                }
                                Row (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 20.dp, start = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Hikayeler",
                                        fontFamily = nunitoFontFamily,
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )

                                    IconButton(onClick = {
                                        oylamaViewModel.setDropdownKontrol(true)
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.MoreVert,
                                            contentDescription = ""
                                        )

                                        DropdownMenuBileseni(
                                            oylamaState = oylamaState,
                                            oylamaViewModel = oylamaViewModel
                                        )
                                    }


                                }

                                Spacer(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 25.dp)
                                    .height(1.dp)
                                    .background(Color.Black)
                                )


                            }
                            items(onaylanmisYarismaHikayeleri) {hikaye->
                                HerBirHikaye(
                                    hikaye,
                                    oylamaViewModel
                                )

                            }
                        }

                    }

                } else {
                    YarismaYok(yazarRol ,it, bitenYarismalarSayfasinaGit,anaSayfayaGit,oylamaViewModel)
                }
            } else {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

        }
    )

    if (oylamaState.infoButonunaTiklandiMi) {
        AlertDialog(
            onDismissRequest = { oylamaViewModel.setInfoButonunaTiklandiMi(false) },
            title = {
                Text(
                    text = "Oylama yapabilirsiniz.",
                    color = Color.Black,
                    fontFamily = nunitoFontFamily,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Yalnızca bir oy hakkınız var ve oy verdikten sonra geri alamazsınız.",
                    color = Color.Black,
                    fontFamily = nunitoFontFamily,
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                Button(onClick = { oylamaViewModel.setInfoButonunaTiklandiMi(false) }) {
                    Text(text = "Tamam")
                }
            }
        )
    }


    if(oylamaState.tiklananHikayeKontrol) {
        Dialog(
            onDismissRequest = { /*TODO*/ },
            DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = oylamaState.secilenHikaye.yazarKullaniciAdi!!,
                                fontFamily = nunitoFontFamily,
                                color = AcikGri
                            )
                        },
                        actions = {
                            IconButton(onClick = { oylamaViewModel.setTiklananHikayeKontrol(false) }) {
                                Icon(imageVector = Icons.Filled.Close, contentDescription = "", tint = AcikGri)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = AnaRenkKoyu
                        )
                    )
                },
                content = {
                    BackHandler {
                        oylamaViewModel.setTiklananHikayeKontrol(false)
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(AcikGri)
                            .verticalScroll(rememberScrollState())
                            .padding(it)
                            .padding(20.dp)
                    ) {

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            text = oylamaState.secilenHikaye.kullaniciYarismaHikayeBaslik!!,
                            textAlign = TextAlign.Center,
                            fontFamily = nunitoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )

                        Text(
                            text = anaHikaye,
                            fontFamily = nunitoFontFamily
                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 30.dp)
                                .height(1.dp)
                                .background(Color.Black)

                        )
                        Text(text = oylamaState.secilenHikaye.kullaniciYarismaHikayesi!!,
                            fontFamily = nunitoFontFamily,
                        )
                        Spacer(modifier = Modifier.weight(1f))

                        if (!oylamaState.yazarOyVermisMi && !oylamaState.hikayeYazarinMi) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 25.dp),
                            ) {
                                Button(
                                    modifier = Modifier.align(Alignment.CenterEnd),
                                    onClick = {
                                        if (InternetKontrol.internetVarMi(context)) {
                                            oylamaViewModel.yarismayaOyVer(oylamaState.secilenHikaye)
                                            oylamaViewModel.onaylanmisYarismaHikayeleriniGetir()
                                            oylamaViewModel.setTiklananHikayeKontrol(false)
                                        }
                                    }
                                ) {
                                    Text(text = "Oyla +", fontFamily = nunitoFontFamily)
                                }
                            }
                        }

                    }
                }

            )

        }
    }
}

@Composable
fun HerBirHikaye(
    hikaye: KullaniciYarismaHikayesi,
    oylamaViewModel: OylamaViewModel,
) {

    Card(
        modifier = Modifier
            .padding(10.dp),
        onClick = {
            oylamaViewModel.setSecilenHikaye(hikaye)
            oylamaViewModel.setTiklananHikayeKontrol(true)
            oylamaViewModel.hikayeYazarinMi(hikaye.yazarEmail!!)
        },
        colors = CardDefaults.cardColors(
            containerColor = AcikGri
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                text = hikaye.kullaniciYarismaHikayeBaslik!!,
                textAlign = TextAlign.Center,
                fontFamily = nunitoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.Black
            )

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.oy),
                    contentDescription = "",
                    tint = Color.Black
                )
                Text(
                    text = hikaye.oylar.toString(),
                    fontSize = 22.sp,
                    color = Color.Black
                )
            }
        }
        Text(
            modifier = Modifier.padding(15.dp),
            text = hikaye.kullaniciYarismaHikayesi!!,
            fontFamily = nunitoFontFamily,
            fontSize = 16.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )

        Text(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth(),
            text = "@${hikaye.yazarKullaniciAdi}",
            textAlign = TextAlign.End,
            fontFamily = nunitoFontFamily,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}

@Composable
fun YarismaYok(
    yazarRol: String,
    paddingValues: PaddingValues,
    bitenYarismalarSayfasinaGit: () -> Unit,
    anaSayfayaGit: () -> Unit,
    oylamaViewModel: OylamaViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
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
                        text = "Yarışma iptal edildi.",
                        fontFamily = nunitoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "Yeterli katılım olmadığından dolayı yarışma iptal edildi. Sonraki yarışmaları bekleyiniz.",
                        fontFamily = nunitoFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 18.sp,
                        color = Color.Black

                    )
                }

                Button(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    onClick = {
                        if (yazarRol != "admin") {
                            bitenYarismalarSayfasinaGit()
                        } else {
                            oylamaViewModel.yarismayiSil {
                                anaSayfayaGit()
                            }
                        }
                    }
                ) {
                    Text(text = if (yazarRol != "admin") {
                        "Bitenlere gözat"
                    } else {
                        "Bitir"
                    })
                }
            }
        }
    }
}
