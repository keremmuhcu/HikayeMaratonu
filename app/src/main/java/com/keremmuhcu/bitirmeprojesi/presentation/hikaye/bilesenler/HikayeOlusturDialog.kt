package com.keremmuhcu.bitirmeprojesi.presentation.hikaye.bilesenler

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.HikayelerimState
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.HikayelerimViewModel
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenkKoyu
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HikayeOlusturDialog(
    hikayelerimState: HikayelerimState,
    hikayelerimViewModel: HikayelerimViewModel,
    context:Context
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterStart),
                            onClick = {
                                if (hikayelerimViewModel.yeniHikayeninAlanlariDoldurulduMu()) {
                                    hikayelerimViewModel.setGosterilecekAlert("geri")
                                    hikayelerimViewModel.setAlertKontrol(true)
                                } else {
                                    hikayelerimViewModel.setDialogKontrol(false)
                                    hikayelerimViewModel.yeniHikayeOlusturdakiAlanlariTemizle()
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
                            text = "Hikaye Oluştur",
                            fontFamily = nunitoFontFamily,
                            color = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            hikayelerimViewModel.setDropdownKontrol(true)
                        }
                    ) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "", tint = AcikGri)
                        if (hikayelerimState.dropdownKontrol) {
                            DropdownBileseni(
                                hikayelerimState = hikayelerimState,
                                hikayelerimViewModel = hikayelerimViewModel,
                                context = context
                            )
                        }
                    }
                    /*IconButton(
                        onClick = {
                            if (hikayelerimViewModel.yeniHikayeninAlanlariDoldurulduMu()) {
                                hikayelerimViewModel.setGosterilecekAlert("paylas")
                                hikayelerimViewModel.setAlertKontrol(true)
                            } else {
                                hikayelerimViewModel.setToastMesaj("Alanları eksiksiz doldurun.")
                            }
                        }
                    ) {
                        Icon(painter = painterResource(id = R.drawable.accepted), contentDescription = "", tint = AcikGri)
                    }*/
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AnaRenkKoyu
                )
            )
        },
        content = {contentPadding->
            Column(
                modifier = Modifier
                    .background(AcikGri)
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(horizontal = 10.dp),
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = hikayelerimState.yeniHikayeBaslik,
                    onValueChange = {
                        hikayelerimViewModel.setYeniHikayeBaslik(it)
                    },
                    textStyle = TextStyle(
                        fontFamily = nunitoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    ),
                    placeholder = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Başlık giriniz",
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        )
                    },
                    maxLines = 1,
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                    )
                )
                Spacer(modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .background(Color.Black)
                )

                Text(
                    modifier = Modifier.padding(top = 40.dp),
                    text = "Giriş",
                    fontFamily = nunitoFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(3f),
                    value = hikayelerimState.yeniHikayeGiris,
                    onValueChange = {
                        hikayelerimViewModel.setYeniHikayeGiris(it)
                    },
                    textStyle = TextStyle(
                        fontFamily = nunitoFontFamily,
                        fontSize = 18.sp,
                    ),
                    placeholder = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Hikayeyi tanıtıcı bir yazı giriniz. Konusu, içeriği, türü vb.",
                            fontFamily = nunitoFontFamily,
                            fontSize = 18.sp
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                    )
                )

                Text(
                    modifier = Modifier.padding(top = 25.dp),
                    text = "1.Bölüm",
                    fontFamily = nunitoFontFamily,
                    fontSize = 20.sp, fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(7f),
                    value = hikayelerimState.yeniHikayeIlkBolum,
                    onValueChange = {
                        hikayelerimViewModel.setYeniHikayeIlkBolum(it)
                    },
                    textStyle = TextStyle(
                        fontFamily = nunitoFontFamily,
                        fontSize = 18.sp,
                    ),
                    placeholder = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Hikayeyinizi yazmaya başlayınız. Bu hikayenizin ilk bölümü olacaktır. İsterseniz tek bölümde hikayenizi bitirebilir ya da daha sonrasında bölümler ekleyebilirsiniz.",
                            fontFamily = nunitoFontFamily,
                            fontSize = 18.sp
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