package com.keremmuhcu.bitirmeprojesi.presentation.biten_yarismalar.biten_yarisma_detay.bilesenler


import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.keremmuhcu.bitirmeprojesi.presentation.biten_yarismalar.biten_yarisma_detay.BitenYarismaDetayState
import com.keremmuhcu.bitirmeprojesi.presentation.biten_yarismalar.biten_yarisma_detay.BitenYarismaDetayViewModel
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenkKoyu
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Hikaye(
    anaHikaye:String,
    bitenYarismaDetayViewModel: BitenYarismaDetayViewModel,
    bitenYarismaDetayState: BitenYarismaDetayState
    //kullaniciYarismaHikayesi: KullaniciYarismaHikayesi
) {
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
                            text = bitenYarismaDetayState.secilenHikaye.yazarKullaniciAdi!!,
                            fontFamily = nunitoFontFamily,
                            color = AcikGri
                        )
                    },
                    actions = {
                        IconButton(onClick = {
                            bitenYarismaDetayViewModel.setHikayeDialogKontrol(false)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
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
                BackHandler {
                    bitenYarismaDetayViewModel.setHikayeDialogKontrol(false)
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AcikGri)
                        .padding(it)
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Column(modifier = Modifier.align(Alignment.TopCenter),) {
                        Text(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                            text = bitenYarismaDetayState.secilenHikaye.kullaniciYarismaHikayeBaslik!!,
                            fontFamily = nunitoFontFamily,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Text(
                            text = anaHikaye,
                            fontFamily = nunitoFontFamily,
                            color = Color.Black
                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 30.dp)
                                .height(1.dp)
                                .background(Color.Black)

                        )
                        Text(
                            text = bitenYarismaDetayState.secilenHikaye.kullaniciYarismaHikayesi!!,
                            fontFamily = nunitoFontFamily,
                            color = Color.Black
                        )
                    }
                }
            }
        )
    }
}