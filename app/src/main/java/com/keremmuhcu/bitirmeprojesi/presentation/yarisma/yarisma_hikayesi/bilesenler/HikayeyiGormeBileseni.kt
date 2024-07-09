package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.yarisma_hikayesi.bilesenler

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.yarisma_hikayesi.YarismaHikayesiState
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.yarisma_hikayesi.YarismaHikayesiViewModel
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenkKoyu
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HikayeyiGormeBileseni(
    yarismaHikayesiViewModel: YarismaHikayesiViewModel,
    yarismaHikayesiState: YarismaHikayesiState
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = yarismaHikayesiState.kullaniciYarismaHikayesiBaslik,
                                fontFamily = nunitoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = AcikGri
                            )
                            IconButton(onClick = { yarismaHikayesiViewModel.setHikayeyiGormeKontrolu(false) }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
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
                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AcikGri)
                        .padding(it)
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    text = yarismaHikayesiState.kullaniciYarismaHikayesi,
                    fontFamily = nunitoFontFamily,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        )
    }
}