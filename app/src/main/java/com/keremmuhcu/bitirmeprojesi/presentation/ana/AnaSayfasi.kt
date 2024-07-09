package com.keremmuhcu.bitirmeprojesi.presentation.ana

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keremmuhcu.bitirmeprojesi.R
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily
import com.keremmuhcu.bitirmeprojesi.util.InternetKontrol

data class Menuler(
    val menuAdi:String = "",
    val gorsel: Painter
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AnaSayfasi(
    yarismaSayfasinaGit:() -> Unit,
    bitenYarismalarSayfasinaGit:() -> Unit,
    hikayeleriKesfetSayfasinaGit:() -> Unit
) {
    val anaViewModel:AnaViewModel = viewModel()
    val anaState by anaViewModel.anaState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (!InternetKontrol.internetVarMi(context)) {
            anaViewModel.setToastMesaj("Bağlantı hatası")
        }
    }

    LaunchedEffect(anaState.toastMesaj) {
        if (anaState.toastMesaj != "") {
            Toast.makeText(context,anaState.toastMesaj, Toast.LENGTH_SHORT).show()
            anaViewModel.setToastMesaj("")
        }
    }

    val menuler = listOf(
        Menuler(
            menuAdi = "HİKAYE YARIŞMASI",
            gorsel = painterResource(id = R.drawable.yarisma_canva)
        ),
        Menuler(
            menuAdi = "BİTEN YARIŞMALAR",
            gorsel = painterResource(id = R.drawable.biten_yarismalar_canva)
        ),
        Menuler(
            menuAdi = "HİKAYELERİ KEŞFET",
            gorsel = painterResource(id = R.drawable.kesfet_canva)
        )
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AcikGri),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "HİKAYE MARATONU",
                    fontFamily = nunitoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
                Image(
                    modifier = Modifier.size(70.dp),
                    painter = painterResource(id = R.drawable.logo_png),
                    contentDescription = "",
                )
            }

            Spacer(modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(3.dp)
                .background(Color.Black)
            )
        }


        LazyColumn(
            modifier = Modifier.padding(top = 40.dp)
        ) {

            items(menuler) {menu ->
                Card(
                    modifier = Modifier
                        .padding(15.dp),
                    onClick = {
                        if (!InternetKontrol.internetVarMi(context)) {
                            anaViewModel.setToastMesaj("Bağlantı hatası")
                        } else {
                            when(menu.menuAdi) {
                                "HİKAYE YARIŞMASI" -> yarismaSayfasinaGit()
                                "BİTEN YARIŞMALAR" -> bitenYarismalarSayfasinaGit()
                                "HİKAYELERİ KEŞFET" -> hikayeleriKesfetSayfasinaGit()
                            }
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                ) {
                    Column {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0x5C6B7687) //0xFF6B7677
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    modifier = Modifier
                                        .height(150.dp)
                                        .width(175.dp),
                                    painter = menu.gorsel,
                                    contentDescription = "",
                                    contentScale = ContentScale.FillBounds,
                                )

                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 20.dp, horizontal = 15.dp),
                                    text = menu.menuAdi,
                                    textAlign = TextAlign.Center,
                                    fontSize = 20.sp,
                                    fontFamily = nunitoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }

                    }
                }

            }
        }

    }
}
