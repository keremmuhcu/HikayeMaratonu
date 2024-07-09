package com.keremmuhcu.bitirmeprojesi.presentation.biten_yarismalar.biten_yarisma_detay.bilesenler

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.keremmuhcu.bitirmeprojesi.R
import com.keremmuhcu.bitirmeprojesi.data.models.KullaniciYarismaHikayesi
import com.keremmuhcu.bitirmeprojesi.presentation.biten_yarismalar.biten_yarisma_detay.BitenYarismaDetayViewModel
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IlkUcItem(
    bitenYarismaDetayViewModel: BitenYarismaDetayViewModel,
    kullaniciYarismaHikayesi: KullaniciYarismaHikayesi,
    gorsel: Painter,
    yukseklik: Int,
    birinciMi:Boolean
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column{
        if (birinciMi) {
            Box(modifier = Modifier.width(125.dp)) {
                Image(
                    modifier = Modifier.align(Alignment.TopCenter).size(75.dp),
                    painter = painterResource(id = R.drawable.tac),
                    contentDescription = "",
                )
            }
        }

        Box(modifier = Modifier
            .height(yukseklik.dp)
            .width(125.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                bitenYarismaDetayViewModel.setSecilenHikaye(kullaniciYarismaHikayesi)
                bitenYarismaDetayViewModel.setHikayeDialogKontrol(true)
            },
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(1f)
                    .basicMarquee(animationMode = MarqueeAnimationMode.Immediately),
                text = kullaniciYarismaHikayesi.yazarKullaniciAdi!!,
                maxLines = 1,
                fontSize = 14.sp,
                fontFamily = nunitoFontFamily,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Image(
                modifier = Modifier
                    .fillMaxHeight(),
                painter = gorsel,
                contentDescription = "",
                contentScale = ContentScale.Crop
            )

            Text(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .zIndex(1f),
                text = "↑ ${kullaniciYarismaHikayesi.oylar}",
                fontSize = 14.sp,
                fontFamily = nunitoFontFamily,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }





        /*Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .height(75.dp)
                .width(75.dp),
            painter = gorsel,
            contentDescription = ""
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp),
            onClick = {
                bitenYarismaDetayViewModel.setSecilenHikaye(kullaniciYarismaHikayesi)
                bitenYarismaDetayViewModel.setHikayeDialogKontrol(true)
            },
            colors = CardDefaults.cardColors(
                containerColor = AcikGri
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 5.dp),
                    text = kullaniciYarismaHikayesi.kullaniciYarismaHikayeBaslik!!,
                    fontFamily = nunitoFontFamily,
                    fontSize = 20.sp,
                    color = Color.Black
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 5.dp),
                    text = "@${kullaniciYarismaHikayesi.yazarKullaniciAdi}",
                    fontSize = 12.sp,
                    fontFamily = nunitoFontFamily
                )
            }
        }
    }*/
}