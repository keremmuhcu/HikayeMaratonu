package com.keremmuhcu.bitirmeprojesi.presentation.giris

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keremmuhcu.bitirmeprojesi.R
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenkKoyu
import com.keremmuhcu.bitirmeprojesi.ui.theme.Purple40
import com.keremmuhcu.bitirmeprojesi.ui.theme.nunitoFontFamily
import com.keremmuhcu.bitirmeprojesi.util.InternetKontrol

@Composable
fun GirisSayfasi(
    kayitSayfasinaGit:() -> Unit,
    bottomSayfasinaGit:() -> Unit
) {
    val girisViewModel: GirisViewModel = viewModel()
    val girisState by girisViewModel.girisState.collectAsState()
    var sifreyiGoster by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(girisState.toastMesaj) {
        if (girisState.toastMesaj != "") {
            Toast.makeText(context,girisState.toastMesaj, Toast.LENGTH_SHORT).show()
            girisViewModel.setToastMesaj("")
        }
    }

    Column(
        modifier = Modifier
            .background(AcikGri)
            .fillMaxSize()
            .padding(top = 10.dp, bottom = 30.dp, start = 30.dp, end = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Image(
            painter = painterResource(id = R.drawable.logo_png),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Hikaye Maratonu",
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            fontFamily = nunitoFontFamily,
            fontWeight = FontWeight.Bold,
            color = AnaRenkKoyu
        )
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .height(1.dp)
            .background(AnaRenkKoyu)
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "\"Hikaye yaz, interaktif yarışmalara katıl ve daha fazlası\"",
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontFamily = nunitoFontFamily,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )


        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(top = 100.dp),
            value = girisState.kullaniciAdiVeyaEmail,
            onValueChange = {
                girisViewModel.setKullaniciAdiVeyaEmail(it)
            },
            placeholder = {
                Text(text = "Email")
            },
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = girisState.sifre,
            onValueChange = {
                girisViewModel.setSifre(it)
            },
            placeholder = {
                Text(text = "Şifre")
            },
            colors = OutlinedTextFieldDefaults.colors(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation =
            if (sifreyiGoster) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                Icon(
                    if (sifreyiGoster) {
                        painterResource(id = R.drawable.visibility)
                    } else {
                        painterResource(id = R.drawable.visibility_off)
                    },
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        sifreyiGoster = !sifreyiGoster
                    }
                )
            }
        )

        Button(
            onClick = {
                if (InternetKontrol.internetVarMi(context)) {
                    girisViewModel.setBekleniyor(true)
                    girisViewModel.girisYap { basarili->
                        if (basarili) {
                            bottomSayfasinaGit()
                        }
                        girisViewModel.setBekleniyor(false)

                    }
                } else {
                    girisViewModel.setToastMesaj("Bağlantı hatası.")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
        ) {
            Text(text = "Giriş")
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .padding(bottom = 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Hesabın yok mu? ")
            TextButton(onClick = kayitSayfasinaGit) {
                Text(text = "Kayıt Ol")
            }
        }
    }

    if (girisState.bekleniyor) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f)
                .background(AcikGri)
                .clickable(enabled = false) {}
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }

}

@Composable
fun Baslik(){
    Text(
        text = "Hikaye Maratonu",
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = Purple40
    )
    Text(
        text = "Giriş",
        fontSize = 20.sp
    )
}
