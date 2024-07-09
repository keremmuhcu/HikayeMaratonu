package com.keremmuhcu.bitirmeprojesi.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.keremmuhcu.bitirmeprojesi.presentation.giris.GirisSayfasi
import com.keremmuhcu.bitirmeprojesi.presentation.kayit.KayitSayfasi

fun NavGraphBuilder.uyelikNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.UYELIK,
        startDestination = UyelikSayfasi.Giris.rota
    ) {
        composable(route = UyelikSayfasi.Giris.rota) {
            GirisSayfasi(
                kayitSayfasinaGit = {
                    navController.navigate(UyelikSayfasi.Kayit.rota) {
                        popUpTo(UyelikSayfasi.Giris.rota) {
                            inclusive = true
                        }
                    }
                },
                bottomSayfasinaGit = {
                    navController.navigate(Graph.ANA) {
                        popUpTo(UyelikSayfasi.Giris.rota) {
                            inclusive = true
                        }
                    }
                }


            )
        }
        composable(route = UyelikSayfasi.Kayit.rota) {
            KayitSayfasi(
                girisSayfasinaGit = {
                    navController.navigate(UyelikSayfasi.Giris.rota) {
                        popUpTo(UyelikSayfasi.Kayit.rota) {
                            inclusive = true
                        }
                    }
                },
                bottomSayfasinaGit = {
                    navController.popBackStack()
                    navController.navigate(Graph.ANA)
                }
            )
        }
    }
}

sealed class UyelikSayfasi(val rota: String) {
    object Giris : UyelikSayfasi(rota = "giris")
    object Kayit : UyelikSayfasi(rota = "kayit")
}

