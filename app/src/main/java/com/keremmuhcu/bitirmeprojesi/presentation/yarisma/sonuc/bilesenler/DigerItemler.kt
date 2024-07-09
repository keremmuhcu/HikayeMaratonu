package com.keremmuhcu.bitirmeprojesi.presentation.yarisma.sonuc.bilesenler

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keremmuhcu.bitirmeprojesi.data.models.KullaniciYarismaHikayesi
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.sonuc.SonucViewModel
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenkKoyu
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily

@Composable
fun DigerItemler(
    sonucViewModel: SonucViewModel,
    kullaniciYarismaHikayesi: KullaniciYarismaHikayesi,
    sira:Int,
    sonMu:Boolean
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                sonucViewModel.setSecilenHikaye(kullaniciYarismaHikayesi)
                sonucViewModel.setHikayeDialogKontrol(true)
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(end = 5.dp),
                text = "${sira}.",
                fontFamily = nunitoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black
            )

            Text(
                modifier  = Modifier.weight(1f),
                text = "${kullaniciYarismaHikayesi.yazarKullaniciAdi}",
                textAlign = TextAlign.Center,
                fontFamily = nunitoFontFamily,
                fontSize = 20.sp,
                color = Color.Black
            )

            Text(
                text = "↑${kullaniciYarismaHikayesi.oylar}",
                fontFamily = nunitoFontFamily,
                fontSize = 20.sp,
                color = Color.Black
            )

        }

        if (!sonMu) {
            Spacer(modifier = Modifier
                .padding(top = 20.dp)
                .height(1.dp)
                .fillMaxWidth()
                .background(AnaRenkKoyu)
            )
        } else {
            Spacer(modifier = Modifier
                .padding(bottom = 40.dp)
            )
        }
    }
}