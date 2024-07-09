package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.onaylama.bilesenler

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.onaylama.OnaylamaState
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.onaylama.OnaylamaViewModel
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenkKoyu
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnaylamaPenceresi(
    onaylamaViewModel: OnaylamaViewModel,
    onaylamaState: OnaylamaState,
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
                            text = onaylamaState.secilenHikaye.yazarKullaniciAdi!!,
                            fontFamily = nunitoFontFamily,
                            color = AcikGri
                        )
                    },
                    actions = {
                        IconButton(onClick = { onaylamaViewModel.setOnaylamaPencereKontrol(false) }) {
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
                    onaylamaViewModel.setOnaylamaPencereKontrol(false)
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
                        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                        text = onaylamaState.secilenHikaye.kullaniciYarismaHikayeBaslik!!,
                        textAlign = TextAlign.Center,
                        fontFamily = nunitoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Text(
                        text = onaylamaState.secilenHikaye.kullaniciYarismaHikayesi!!,
                        fontFamily = nunitoFontFamily,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(onClick = {
                            onaylamaViewModel.yarismayiReddet(onaylamaState.secilenHikaye)
                            onaylamaViewModel.setOnaylamaPencereKontrol(false)
                        }) {
                            Text(text = "Reddet")
                        }

                        Button(onClick = {
                            onaylamaViewModel.yarismayiOnayla(onaylamaState.secilenHikaye)
                            onaylamaViewModel.setOnaylamaPencereKontrol(false)
                        }) {
                            Text(text = "Onayla")
                        }

                    }

                }
            }

        )
    }
    /*AlertDialog(
        modifier = Modifier.padding(vertical = 20.dp),
        onDismissRequest = { onaylamaViewModel.setOnaylamaPencereKontrol(false) },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = onaylamaState.secilenHikaye.kullaniciYarismaHikayeBaslik!!,
                textAlign = TextAlign.Center,
                fontFamily = nunitoFontFamily,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
                Text(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    text = onaylamaState.secilenHikaye.kullaniciYarismaHikayesi!!,
                    fontFamily = nunitoFontFamily,
                    fontSize = 16.sp,
                    color = Color.Black
                )

        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(onClick = {
                    onaylamaViewModel.yarismayiReddet(onaylamaState.secilenHikaye)
                    onaylamaViewModel.setOnaylamaPencereKontrol(false)
                }) {
                    Text(text = "Reddet")
                }

                Button(onClick = {
                    onaylamaViewModel.yarismayiOnayla(onaylamaState.secilenHikaye)
                    onaylamaViewModel.setOnaylamaPencereKontrol(false)
                }) {
                    Text(text = "Onayla")
                }

            }
        }
    )*/

}