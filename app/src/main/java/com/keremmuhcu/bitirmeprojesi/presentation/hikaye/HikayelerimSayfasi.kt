package com.keremmuhcu.bitirmeprojesi.presentation.hikaye

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.bilesenler.AlertDialogBileseni
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.bilesenler.DropdownSiralaBileseni
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.bilesenler.HikayeOlusturDialog
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenk
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenkKoyu
import com.keremmuhcu.bitirmeprojesi.ui.theme.BittiYesil
import com.keremmuhcu.bitirmeprojesi.ui.theme.DevamEdiyorMavi
import com.keremmuhcu.bitirmeprojesi.ui.theme.YayinlanmadiPembe
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily
import com.keremmuhcu.bitirmeprojesi.util.InternetKontrol

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HikayelerimSayfasi(
    email:String,
    kullaniciAdi:String,
    profilSayfasinaGit:() -> Unit,
    hikayeDetaySayfasinaGit:(String) -> Unit,
    hikayeOkuSayfasinaGit:(String,String) -> Unit
) {
    val hikayelerimViewModel:HikayelerimViewModel = viewModel()
    val hikayelerimState by hikayelerimViewModel.hikayelerimState
    val hikayelerListesi by hikayelerimViewModel.hikayelerimListesi.collectAsState()
    val context = LocalContext.current

    if (hikayelerimState.toastMesaj != "") {
        Toast.makeText(context,hikayelerimState.toastMesaj,Toast.LENGTH_SHORT).show()
        hikayelerimViewModel.setToastMesaj("")
    }
    
    LaunchedEffect(Unit) {
        hikayelerimViewModel.setZiyaretciMi(email)
        hikayelerimViewModel.hikayeleriGetir(email)
    }

    if (hikayelerimState.verilerAlindiMi) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            IconButton(
                                modifier = Modifier.align(Alignment.CenterStart),
                                onClick = {
                                    profilSayfasinaGit()
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
                                text = if (hikayelerimState.ziyaretciMi) {
                                    "${kullaniciAdi}: Hikayeleri"
                                } else {
                                    "Hikayelerim"
                                },
                                fontFamily = nunitoFontFamily,
                                color = Color.White
                            )
                        }
                    },
                    actions = {
                        // dropdown menu olacak.
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = AnaRenkKoyu//MaterialTheme.colorScheme.primaryContainer
                    )
                )
            },
            content = {
                LazyColumn(
                    modifier = Modifier
                        .background(AnaRenk)
                        .fillMaxSize()
                        .padding(it)
                ){
                    item {
                        if (!hikayelerimState.ziyaretciMi) {
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .background(AcikGri)
                                .clickable {
                                    hikayelerimViewModel.setDropdownSiralaKontrol(false)
                                    hikayelerimViewModel.setDialogKontrol(true)
                                }
                            ) {
                                Row(
                                    modifier = Modifier.align(Alignment.Center),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        modifier = Modifier.padding(end = 15.dp),
                                        imageVector = Icons.Default.Add,
                                        contentDescription = ""
                                    )

                                    Text(
                                        text = "Yeni bir hikaye oluştur",
                                        fontFamily = nunitoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        fontSize = 18.sp,
                                    )
                                }
                            }
                        }

                        if (hikayelerListesi.isEmpty()) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 40.dp),
                                text = "Henüz bir hikaye bulunmuyor.",
                                fontFamily = nunitoFontFamily,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    modifier = Modifier.padding(bottom = 5.dp),
                                    text = "Hikayeler",
                                    fontFamily = nunitoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )

                                IconButton(
                                    onClick = {
                                        hikayelerimViewModel.setDropdownSiralaKontrol(true)
                                    }
                                ) {
                                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "")
                                    DropdownSiralaBileseni(
                                        hikayelerimState = hikayelerimState,
                                        hikayelerimViewModel = hikayelerimViewModel,
                                        context = context
                                    )
                                }
                            }

                            Spacer(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .background(Color.Black)
                                    .height(1.dp)
                                    .fillMaxWidth()

                            )
                        }
                    }
                    
                    if (hikayelerListesi.isNotEmpty()) {
                        items(hikayelerListesi) { hikaye->
                            val durum:String
                            val fonRengi:Color

                            if (!hikaye.yayinlandiMi!!) {
                                durum = "(yayınlanmadı)"
                                fonRengi = YayinlanmadiPembe
                            } else if (hikaye.bittiMi!!) {
                                durum ="(bitti)"
                                fonRengi = BittiYesil
                            } else {
                                durum = "(devam ediyor)"
                                fonRengi = DevamEdiyorMavi
                            }

                            Row(
                                modifier = Modifier
                                    .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 5.dp)
                                    .background(fonRengi)
                                    .fillMaxWidth()
                                    .clickable {
                                        if (InternetKontrol.internetVarMi(context)) {
                                            if (!hikayelerimState.ziyaretciMi) {
                                                hikayeDetaySayfasinaGit(hikaye.id!!)
                                            } else {
                                                hikayeOkuSayfasinaGit(hikaye.id!!, hikaye.yazarEmail!!)
                                            }
                                        } else {
                                            hikayelerimViewModel.setToastMesaj("Bağlantı hatası")
                                        }

                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.padding(20.dp),
                                    text = hikaye.baslik!!,
                                    fontFamily = nunitoFontFamily
                                )
                                Text(
                                    text = durum,
                                    fontFamily = nunitoFontFamily
                                )
                            }
                        }
                    }
                }
            }
        )
    }

    if (hikayelerimState.dialogKontrol) {
        HikayeOlusturDialog(
            hikayelerimState = hikayelerimState,
            hikayelerimViewModel = hikayelerimViewModel,
            context = context
        )
    }

    if (hikayelerimState.alertKontrol) {
        AlertDialogBileseni(
            hikayelerimViewModel = hikayelerimViewModel,
            hikayelerimState = hikayelerimState,
            context = context,
            email = email,
            kullaniciAdi = kullaniciAdi
        )
    }

    if (hikayelerimState.bekleniyor) {
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
    }
}