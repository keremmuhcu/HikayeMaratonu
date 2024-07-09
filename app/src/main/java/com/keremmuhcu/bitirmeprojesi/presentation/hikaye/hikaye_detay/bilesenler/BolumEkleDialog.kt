package com.keremmuhcu.bitirmeprojesi.presentation.hikaye.hikaye_detay.bilesenler

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.hikaye_detay.HikayeDetayState
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.hikaye_detay.HikayeDetayViewModel
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenkKoyu
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BolumEkleDialog(
    hikayeDetayState: HikayeDetayState,
    hikayeDetayViewModel: HikayeDetayViewModel,
    kacinciBolum:String,
    context: Context
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterStart),
                            onClick = {
                                if (!hikayeDetayViewModel.bolumunIcerigiDoluMu()) {
                                    hikayeDetayViewModel.setDialogKontrol(false)
                                    hikayeDetayViewModel.setYeniBolumHikayesi("")
                                } else {
                                    hikayeDetayViewModel.setGosterilecekAlert("geri")
                                    hikayeDetayViewModel.setAlertKontrol(true)
                                }
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
                            text = "${kacinciBolum}. Bölümü Oluştur",
                            fontFamily = nunitoFontFamily,
                            color = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            hikayeDetayViewModel.setDropdownKontrol(true)
                            /*hikayeDetayViewModel.yeniBolumEkle(false) {basariDurumu->
                                if (basariDurumu) {
                                    hikayeDetayViewModel.bolumleriGetir {
                                        if (it) {
                                            hikayeDetayViewModel.setDialogKontrol(false)
                                            hikayeDetayViewModel.setYeniBolumHikayesi("")
                                        } else {
                                            hikayeDetayViewModel.setBekleniyor(false)
                                        }
                                    }
                                } else {

                                }
                            }*/
                        }
                    ) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "", tint = AcikGri)
                        if (hikayeDetayState.dropdownKontrol) {
                            DropdownBileseni(
                                hikayeDetayState = hikayeDetayState,
                                hikayeDetayViewModel = hikayeDetayViewModel,
                                context = context
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
            BackHandler {
                hikayeDetayViewModel.setGosterilecekAlert("geri")
                hikayeDetayViewModel.setAlertKontrol(true)
            }
            Column(
                modifier = Modifier
                    .background(AcikGri)
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(horizontal = 10.dp),
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxSize(),
                    value = hikayeDetayState.yeniBolumHikayesi,
                    onValueChange = {
                        hikayeDetayViewModel.setYeniBolumHikayesi(it)
                    },
                    textStyle = TextStyle(
                        fontFamily = nunitoFontFamily,
                        fontSize = 18.sp,
                    ),
                    placeholder = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Hikayenizin yeni bölümünü yazmaya başlayın.",
                            fontFamily = nunitoFontFamily
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                    )
                )

            }
        }
    )
}