package com.keremmuhcu.bitirmeprojesi.presentation.arama

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keremmuhcu.bitirmeprojesi.util.InternetKontrol

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AramaSayfasi(
    profilZiyaretSayfasinaGit:(String) -> Unit,
    profilSayfasinaGit:() -> Unit
) {
    val aramaViewModel: AramaViewModel = viewModel()
    val aramaState by aramaViewModel.aramaState
    val context = LocalContext.current

    LaunchedEffect(aramaState.toastMesaj) {
        if (aramaState.toastMesaj != "") {
            Toast.makeText(context,aramaState.toastMesaj, Toast.LENGTH_LONG).show()
            aramaViewModel.setToastMesaj("")
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top

    ) {

        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            query = aramaState.sorgu,
            onQueryChange = {
                aramaViewModel.setSorgu(it)
            },
            onSearch = {
                if (InternetKontrol.internetVarMi(context)) {
                    aramaViewModel.kullaniciAdiVarMi { varMi, aktifKullaniciMi->
                        if (varMi) {
                            if (aktifKullaniciMi) {
                                profilSayfasinaGit()
                            } else {
                                val email = aramaState.email
                                profilZiyaretSayfasinaGit(email)
                            }
                        } else {
                            aramaViewModel.setToastMesaj("Eşleşme bulunamadı")
                        }
                    }
                } else {
                    aramaViewModel.setToastMesaj("Bağlantı hatası")
                }
            },
            active = aramaState.aktiflik,
            onActiveChange = {
                aramaViewModel.setAktiflik(it)
            },
            placeholder = {
                Text(text = "Yazar ara...")
            },
            shape = RoundedCornerShape(10.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "")
            }
        ) {

        }
    }
}