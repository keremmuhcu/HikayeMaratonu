package com.keremmuhcu.bitirmeprojesi.presentation.profil

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keremmuhcu.bitirmeprojesi.R
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenk
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenkKoyu
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily
import com.keremmuhcu.bitirmeprojesi.util.InternetKontrol

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilSayfasi(
    girisSayfasinaGit:() -> Unit,
    katilinanYarismalarSayfasinaGit:(String,String) -> Unit,
    hikayelerimSayfasinaGit:(String,String) -> Unit,
    kutuphaneSayfasinaGit:() -> Unit
) {
    val profilViewModel:ProfilViewModel = viewModel()
    val profilState by profilViewModel.profilState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        profilViewModel.yazarBilgileriniAl { basariliMi->
            if (!basariliMi) {
                profilViewModel.setToastMesaj("Lütfen yeniden deneyiniz!")
            }
        }
    }

    LaunchedEffect(profilState.toastMesaj) {
        if (profilState.toastMesaj != "") {
            Toast.makeText(context, profilState.toastMesaj, Toast.LENGTH_SHORT).show()
            profilViewModel.setToastMesaj("")
        }
    }


    if (profilState.bekleniyor) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            fontFamily = nunitoFontFamily,
                            text = if (profilState.yazar.rol != "yazar") {
                                "${profilState.yazar.kullaniciAdi} (${profilState.yazar.rol})"
                            } else {
                                profilState.yazar.kullaniciAdi
                            },
                            color = AcikGri
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                if (InternetKontrol.internetVarMi(context)) {
                                    profilViewModel.cikisYap {
                                        if (it) {
                                            girisSayfasinaGit()
                                        }
                                    }
                                } else {
                                    profilViewModel.setToastMesaj("Çıkış yapılamadı")
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
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
            content = {contentPadding->
                val menuler = listOf(
                    "Katıldığım Yarışmalar",
                    "Hikayelerim",
                    "Kütüphanem"
                )
                Box(
                    modifier = Modifier
                        .background(AnaRenk)
                        .fillMaxSize()
                        .padding(contentPadding)
                        .padding(horizontal = 20.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .align(Alignment.TopCenter),
                        painter = painterResource(id = R.drawable.logo_png),
                        contentDescription = "",
                    )

                    LazyColumn(
                        modifier = Modifier.align(Alignment.Center),
                        verticalArrangement = Arrangement.spacedBy(30.dp)
                    ) {
                        items(menuler) {menu->
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    when(menu) {
                                        "Katıldığım Yarışmalar" -> katilinanYarismalarSayfasinaGit(
                                            profilState.yazar.email,
                                            profilState.yazar.kullaniciAdi
                                        )
                                        "Hikayelerim" -> hikayelerimSayfasinaGit(
                                            profilState.yazar.email,
                                            profilState.yazar.kullaniciAdi
                                        )
                                        "Kütüphanem" -> kutuphaneSayfasinaGit()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AnaRenkKoyu
                                )
                            ) {
                                Text(
                                    modifier = Modifier.padding(vertical = 5.dp),
                                    text = menu,
                                    fontFamily = nunitoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}
