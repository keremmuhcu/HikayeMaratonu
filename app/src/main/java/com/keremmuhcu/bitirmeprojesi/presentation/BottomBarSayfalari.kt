package com.keremmuhcu.bitirmeprojesi.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarSayfalari(
    val rota: String,
    val baslik: String? = null,
    val parametresizIsim:String? = null,
    val seciliykenIcon: ImageVector? = null,
    val seciliDegilkenIcon: ImageVector? = null
) {
    object Ana : BottomBarSayfalari(
        rota = "ana",
        baslik = "Ana",
        seciliykenIcon = Icons.Filled.Home,
        seciliDegilkenIcon = Icons.Outlined.Home
    )

    object Arama : BottomBarSayfalari(
        rota = "arama",
        baslik = "Arama",
        seciliykenIcon = Icons.Filled.Search,
        seciliDegilkenIcon = Icons.Outlined.Search
    )

    object Profil : BottomBarSayfalari(
        rota = "profil",
        baslik = "Profil",
        seciliykenIcon = Icons.Filled.Person,
        seciliDegilkenIcon = Icons.Outlined.Person
    )

    object Yarisma : BottomBarSayfalari(
        rota = "yarisma"
    )


    object BitenYarismalar: BottomBarSayfalari(
        rota = "bitenYarismalar",
    )

    object HikayeleriKesfet:BottomBarSayfalari(
        rota = "hikayeleriKesfet",
    )

    object YarismaOlusturma: BottomBarSayfalari (
        rota = "yarismaOlusturma"
    )

    object YarismaHikayesi: BottomBarSayfalari(
        rota = "yarismaHikayesi/{kullaniciAdi}/{rol}",
        parametresizIsim = "yarismaHikayesi"
    )

    object KullaniciYarismaHikayesiOlusturma: BottomBarSayfalari(
        rota = "kullaniciYarismaHikayesiOlusturma/{anaHikaye}/{yazarKullaniciAdi}/{yazarRol}",
        parametresizIsim = "kullaniciYarismaHikayesiOlusturma"
    )

    object Onaylama: BottomBarSayfalari(
        rota = "onaylama"
    )

    object Oylama: BottomBarSayfalari(
        rota = "oylama/{anaHikaye}/{yazarRol}",
        parametresizIsim = "oylama"
    )

    object Sonuc: BottomBarSayfalari(
        rota = "sonuc/{anaHikaye}/{yazarRol}",
        parametresizIsim = "sonuc"
    )

    object BitenYarismaDetay: BottomBarSayfalari(
        rota = "bitenYarismaDetay/{id}/{anaHikaye}/{tarih}",
        parametresizIsim = "bitenYarismaDetay"
    )

    object ProfilZiyaret:BottomBarSayfalari(
        rota = "profilZiyaret/{email}",
        parametresizIsim = "profilZiyaret"
    )

    object HikayeDetay:BottomBarSayfalari(
        rota = "hikayeDetay/{hikayeId}",
        parametresizIsim = "hikayeDetay"
    )

    object KatilinanYarismalar:BottomBarSayfalari(
        rota = "katilinanYarismalar/{email}/{kullaniciAdi}",
        parametresizIsim = "katilinanYarismalar"
    )

    object Hikayelerim:BottomBarSayfalari(
        rota = "hikayelerim/{email}/{kullaniciAdi}",
        parametresizIsim = "hikayelerim"
    )

    object HikayeOku:BottomBarSayfalari(
        rota = "hikayeOku/{hikayeId}/{email}",
        parametresizIsim = "hikayeOku"
    )

    object Kutuphane:BottomBarSayfalari(
        rota = "kutuphane",
    )
}