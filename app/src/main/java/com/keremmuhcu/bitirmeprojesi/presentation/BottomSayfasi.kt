package com.keremmuhcu.bitirmeprojesi.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.keremmuhcu.bitirmeprojesi.navigation.AnaNavigationGraph
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenk
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenkKoyu


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BottomSayfasi(navController: NavHostController = rememberNavController()) {

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        },
        content = { contentPadding->
            AnaNavigationGraph(innerPadding = contentPadding, navController = navController)
        }
    )
}

@Composable
fun BottomBar(navController: NavHostController) {
    val bottomSayfalari = listOf(
        BottomBarSayfalari.Ana,
        BottomBarSayfalari.Arama,
        BottomBarSayfalari.Profil,
    )

    val navGeriGelmeStack by navController.currentBackStackEntryAsState()
    val bulunulanSayfa = navGeriGelmeStack?.destination

    val bottomGosterilecekler = listOf(
        BottomBarSayfalari.Ana,
        BottomBarSayfalari.Arama,
        BottomBarSayfalari.Profil,
        BottomBarSayfalari.Yarisma,
        BottomBarSayfalari.YarismaHikayesi,
        BottomBarSayfalari.Onaylama,
        BottomBarSayfalari.Oylama,
        BottomBarSayfalari.Sonuc,
        BottomBarSayfalari.BitenYarismalar,
        BottomBarSayfalari.BitenYarismaDetay,
        BottomBarSayfalari.ProfilZiyaret,
        BottomBarSayfalari.KatilinanYarismalar,
        BottomBarSayfalari.Kutuphane
    )
    val bottomGosterilecekMi = bottomGosterilecekler.any {
        it.rota == bulunulanSayfa?.route

    }

    if (bottomGosterilecekMi) {
        NavigationBar(
            containerColor = AnaRenkKoyu
        ) {
            bottomSayfalari.forEach { sayfa ->
                AddItem(
                    sayfa = sayfa,
                    bulunulanSayfa = bulunulanSayfa,
                    navController = navController
                )
            }
        }
    }


}

@Composable
fun RowScope.AddItem(
    sayfa: BottomBarSayfalari,
    bulunulanSayfa: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        label = {
            Text(text = sayfa.baslik!!)
        },
        alwaysShowLabel = false,
        icon = {
            Icon(
                imageVector = (if (bulunulanSayfa?.hierarchy?.any { it.route == sayfa.rota } == true) {
                    sayfa.seciliykenIcon
                } else {
                    sayfa.seciliDegilkenIcon
                })!!,
                contentDescription = ""
            )
        },
        selected = bulunulanSayfa?.hierarchy?.any {
            it.route == sayfa.rota
        } == true,
        onClick = {
            navController.navigate(sayfa.rota) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
        colors = NavigationBarItemDefaults.colors(
            unselectedIconColor = AcikGri,
            selectedIconColor = AnaRenkKoyu,
            indicatorColor = AnaRenk,
            selectedTextColor = AcikGri
        )
    )
}
