package com.keremmuhcu.bitirmeprojesi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.keremmuhcu.bitirmeprojesi.data.FirebaseRepository
import com.keremmuhcu.bitirmeprojesi.presentation.BottomSayfasi
import com.keremmuhcu.bitirmeprojesi.presentation.ana.AnaSayfasi

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    var baslangicSayfasi = Graph.UYELIK
    val firebaseRepository = FirebaseRepository()

    // Eğer öncesinde giriş yapmış kullanıcı varsa direkt uygulamaya gir.
    if (firebaseRepository.aktifKullanici != null) {
        baslangicSayfasi = Graph.ANA
    }
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = baslangicSayfasi
    ) {
        uyelikNavGraph(navController = navController)

        // Uygulamaya girildiyse Bottom Navigation Bar gözükecek. Ama giriş ve kayıt ol ekranında gözükmez.
        composable(route = Graph.ANA) {
            BottomSayfasi()
        }
    }
}

// graph isimlerinin tanımı yapıldı
object Graph {
    const val ROOT = "rootGraph"
    const val UYELIK = "uyelikGraph"
    const val ANA = "anaGraph"
}