package com.keremmuhcu.bitirmeprojesi.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.keremmuhcu.bitirmeprojesi.presentation.BottomBarSayfalari
import com.keremmuhcu.bitirmeprojesi.presentation.ana.AnaSayfasi
import com.keremmuhcu.bitirmeprojesi.presentation.arama.AramaSayfasi
import com.keremmuhcu.bitirmeprojesi.presentation.biten_yarismalar.BitenYarismalar
import com.keremmuhcu.bitirmeprojesi.presentation.biten_yarismalar.biten_yarisma_detay.BitenYarismaDetay
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.HikayelerimSayfasi
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.hikaye_detay.HikayeDetaySayfasi
import com.keremmuhcu.bitirmeprojesi.presentation.hikaye.hikaye_oku.HikayeOkuSayfasi
import com.keremmuhcu.bitirmeprojesi.presentation.hikayeleri_kesfet.HikayeleriKesfetSayfasi
import com.keremmuhcu.bitirmeprojesi.presentation.kutuphane.KutuphaneSayfasi
import com.keremmuhcu.bitirmeprojesi.presentation.profil.ProfilSayfasi
import com.keremmuhcu.bitirmeprojesi.presentation.profil.katilinan_yarismalar.KatilinanYarismalarSayfasi
import com.keremmuhcu.bitirmeprojesi.presentation.profil.profil_ziyaret.ProfilZiyaretSayfasi
import com.keremmuhcu.bitirmeprojesi.presentation.timer.TimerViewModel
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.YarismaSayfasi
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.kullanici_yar_hik_olusturma.KullaniciYarismaHikayesiOlusturmaSayfasi
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.onaylama.OnaylamaSayfasi
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.oylama.OylamaSayfasi
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.sonuc.SonucSayfasi
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.yarisma_hikayesi.YarismaHikayesiSayfasi
import com.keremmuhcu.bitirmeprojesi.presentation.yarisma.yarisma_olusturma.YarismaOlusturmaSayfasi

@Composable
fun AnaNavigationGraph(innerPadding: PaddingValues, navController: NavHostController) {

    val timerViewModel: TimerViewModel = viewModel()
    val timerState by timerViewModel.timerState.collectAsState()
    NavHost(
        modifier = Modifier.padding(innerPadding),
        navController = navController,
        route = Graph.ANA,
        startDestination = BottomBarSayfalari.Ana.rota
    ) {
        uyelikNavGraph(navController)
        composable(route = BottomBarSayfalari.Ana.rota) {
            AnaSayfasi(
                yarismaSayfasinaGit = {
                    navController.navigate(BottomBarSayfalari.Yarisma.rota)
                },
                bitenYarismalarSayfasinaGit = {
                    navController.navigate(BottomBarSayfalari.BitenYarismalar.rota)
                },
                hikayeleriKesfetSayfasinaGit = {
                    navController.navigate(BottomBarSayfalari.HikayeleriKesfet.rota)
                }
            )
        }

        composable(BottomBarSayfalari.YarismaOlusturma.rota) {
            YarismaOlusturmaSayfasi(
                anaSayfasinaGit = {
                    navController.navigate(Graph.ANA) {
                        popUpTo(BottomBarSayfalari.Ana.rota) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }

                },
                //timerViewModel = timerViewModel
            )
        }

        composable(BottomBarSayfalari.Yarisma.rota) {

            YarismaSayfasi(
                anaSayfasinaGit = {
                    navController.navigateUp()
                },
                yarismaHikayesiSayfasinaGit = { kullaniciAdi,rol ->
                    navController.navigate("${BottomBarSayfalari.YarismaHikayesi.parametresizIsim}/${kullaniciAdi}/${rol}") {
                        popUpTo(BottomBarSayfalari.Yarisma.rota) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                yarismaOlusturmaSayfasinaGit = {
                    navController.navigate(BottomBarSayfalari.YarismaOlusturma.rota) {
                        popUpTo(BottomBarSayfalari.Yarisma.rota) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                sonucSayfasinaGit = { anaHikaye, rol->
                    navController.navigate("${BottomBarSayfalari.Sonuc.parametresizIsim}/${anaHikaye}/${rol}") {
                        popUpTo(BottomBarSayfalari.Yarisma.rota) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                oylamaSayfasinaGit = { anaHikaye, yazarRol->
                    navController.navigate("${BottomBarSayfalari.Oylama.parametresizIsim}/${anaHikaye}/${yazarRol}")
                },
                bitenYarismalarSayfasinaGit = {
                    navController.navigate(BottomBarSayfalari.BitenYarismalar.rota)
                },
                timerViewModel = timerViewModel,
                timerState = timerState

            )
        }


        composable(BottomBarSayfalari.YarismaOlusturma.rota) {
            YarismaOlusturmaSayfasi(
                anaSayfasinaGit = {
                    navController.navigate(Graph.ANA) {
                        popUpTo(BottomBarSayfalari.YarismaOlusturma.rota) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }

                },
                //timerViewModel = timerViewModel
            )
        }

        composable(
            route = BottomBarSayfalari.YarismaHikayesi.rota,
            arguments = listOf(
                navArgument("kullaniciAdi") {type = NavType.StringType},
                navArgument("rol") { type = NavType.StringType}
            )
        ) {
            val kullaniciAdi = it.arguments?.getString("kullaniciAdi")!!
            val rol = it.arguments?.getString("rol")!!
            YarismaHikayesiSayfasi(
                anaSayfasinaGit = {
                    navController.navigate(Graph.ANA) {
                        popUpTo(BottomBarSayfalari.YarismaHikayesi.rota) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onaylamaSayfasinaGit = {
                    navController.navigate(BottomBarSayfalari.Onaylama.rota)
                },
                kullaniciYarHikOlusturmaSayfasinaGit = {anaHikaye->
                    navController.navigate("${BottomBarSayfalari.KullaniciYarismaHikayesiOlusturma.parametresizIsim}/${anaHikaye}/${kullaniciAdi}/${rol}")
                },
                yazarRol = rol,
                //timerViewModel = timerViewModel,
                timerState = timerState
            )
        }

        composable(
            route = BottomBarSayfalari.KullaniciYarismaHikayesiOlusturma.rota,
            arguments = listOf(
                navArgument("anaHikaye") {type = NavType.StringType},
                navArgument("yazarKullaniciAdi") {type = NavType.StringType},
                navArgument("yazarRol") {type = NavType.StringType}
            )
        ) {
            val anaHikaye = it.arguments?.getString("anaHikaye")
            val yazarKullaniciAdi = it.arguments?.getString("yazarKullaniciAdi")
            val yazarRol = it.arguments?.getString("yazarRol")
            KullaniciYarismaHikayesiOlusturmaSayfasi(
                anaSayfasinaGit = {
                    navController.navigate(Graph.ANA) {
                        popUpTo(Graph.ANA) {
                            inclusive = true
                        }
                        launchSingleTop = true

                    }
                },
                yarismaHikayesiSayfasinaGit = {
                    navController.popBackStack()
                },
                anaHikaye = anaHikaye!!,
                yazarKullaniciAdi = yazarKullaniciAdi!!,
                yazarRol = yazarRol!!,
                //timerViewModel = timerViewModel,
                timerState = timerState
            )
        }

        composable(route = BottomBarSayfalari.Onaylama.rota) {
            OnaylamaSayfasi(
                yarismaHikayesiSayfasinaGit = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = BottomBarSayfalari.Oylama.rota,
            arguments = listOf(
                navArgument("anaHikaye") {type = NavType.StringType},
                navArgument("yazarRol") {type = NavType.StringType}
            )

        ) {
            val anaHikaye = it.arguments?.getString("anaHikaye")
            val yazarRol = it.arguments?.getString("yazarRol")
            OylamaSayfasi(
                anaSayfayaGit = {
                    navController.navigate(Graph.ANA) {
                        popUpTo(Graph.ANA) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                anaHikaye = anaHikaye!!,
                timerState = timerState,
                bitenYarismalarSayfasinaGit = {
                    navController.navigate(BottomBarSayfalari.BitenYarismalar.rota)
                },
                yazarRol = yazarRol!!
                //timerViewModel = timerViewModel,
                //timerState = timerState
            )
        }

        composable(
            route = BottomBarSayfalari.Sonuc.rota,
            arguments = listOf(
                navArgument("anaHikaye") {type = NavType.StringType},
                navArgument("yazarRol") {type = NavType.StringType}
            )
        ) {
            val anaHikaye = it.arguments?.getString("anaHikaye")
            val yazarRol = it.arguments?.getString("yazarRol")
            SonucSayfasi(
                anaSayfasinaGit = {
                    navController.navigate(Graph.ANA) {
                        popUpTo(Graph.ANA) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                anaHikaye = anaHikaye!!,
                yazarRol = yazarRol!!
            )

        }

        composable(route = BottomBarSayfalari.BitenYarismalar.rota) {
            BitenYarismalar(
                anaSayfasinaGit = {
                    navController.navigate(Graph.ANA) {
                        popUpTo(Graph.ANA) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                bitenYarismaDetaySayfasinaGit = {id,anaHikaye,tarih ->
                    navController.navigate("${BottomBarSayfalari.BitenYarismaDetay.parametresizIsim}/${id}/${anaHikaye}/${tarih}")
                },
                hikayeleriKesfetSayfasinaGit = {
                    navController.navigate(BottomBarSayfalari.HikayeleriKesfet.rota)
                }
            )
        }

        composable(
            route = BottomBarSayfalari.BitenYarismaDetay.rota,
            arguments = listOf(
                navArgument("id") {type = NavType.StringType},
                navArgument("anaHikaye") {type = NavType.StringType},
                navArgument("tarih") {type = NavType.StringType},
            )
        ) {
            val id = it.arguments?.getString("id")
            val anaHikaye = it.arguments?.getString("anaHikaye")
            val tarih = it.arguments?.getString("tarih")

            BitenYarismaDetay(
                bitenYarismalarSayfasinaGit = {
                    navController.navigateUp()
                },
                id = id!!,
                anaHikaye = anaHikaye!!,
                tarih = tarih!!
            )
        }

        composable(route = BottomBarSayfalari.Arama.rota) {
            AramaSayfasi(
                profilZiyaretSayfasinaGit = {email->
                    navController.navigate("${BottomBarSayfalari.ProfilZiyaret.parametresizIsim}/${email}")
                },
                profilSayfasinaGit = {
                    navController.navigate(BottomBarSayfalari.Profil.rota) {
                        popUpTo(BottomBarSayfalari.Arama.rota) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = BottomBarSayfalari.Profil.rota) {
            ProfilSayfasi(
                girisSayfasinaGit = {
                    navController.navigate(Graph.UYELIK) {
                        popUpTo(Graph.ANA) {
                            inclusive = true
                        }
                    }
                },
                katilinanYarismalarSayfasinaGit = {email, kullaniciAdi ->
                    navController.navigate("${BottomBarSayfalari.KatilinanYarismalar.parametresizIsim}/${email}/${kullaniciAdi}")
                },
                hikayelerimSayfasinaGit = {email, kullaniciAdi->
                    navController.navigate("${BottomBarSayfalari.Hikayelerim.parametresizIsim}/${email}/${kullaniciAdi}")
                },
                kutuphaneSayfasinaGit = {
                    navController.navigate(BottomBarSayfalari.Kutuphane.rota)
                }
            )
        }

        composable(
            route = BottomBarSayfalari.ProfilZiyaret.rota,
            arguments = listOf(
                navArgument("email") {type = NavType.StringType}
            )
        ) {
            val email = it.arguments?.getString("email")
            ProfilZiyaretSayfasi(
                katilinanYarismalarSayfasinaGit = {email1,kullaniciAdi ->
                    navController.navigate("${BottomBarSayfalari.KatilinanYarismalar.parametresizIsim}/${email1}/${kullaniciAdi}")
                },
                hikayelerimSayfasinaGit = {email1,kullaniciAdi ->
                  navController.navigate("${BottomBarSayfalari.Hikayelerim.parametresizIsim}/${email1}/${kullaniciAdi}")
                },
                email = email!!
            )
        }

        composable(
            route = BottomBarSayfalari.KatilinanYarismalar.rota,
            arguments = listOf(
                navArgument("email") {type = NavType.StringType},
                navArgument("kullaniciAdi") {type = NavType.StringType}
            )
        ) {
            val email = it.arguments?.getString("email")
            val kullaniciAdi = it.arguments?.getString("kullaniciAdi")
            KatilinanYarismalarSayfasi(
                email = email!!,
                kullaniciAdi = kullaniciAdi!!,
                geriDon = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = BottomBarSayfalari.Hikayelerim.rota,
            arguments = listOf(
                navArgument("email"){type = NavType.StringType},
                navArgument("kullaniciAdi"){type = NavType.StringType},
            )
        ) {
            val email = it.arguments?.getString("email")
            val kullaniciAdi = it.arguments?.getString("kullaniciAdi")
            HikayelerimSayfasi(
                profilSayfasinaGit = {
                    navController.navigateUp()
                },
                hikayeDetaySayfasinaGit = {hikayeId->
                    navController.navigate("${BottomBarSayfalari.HikayeDetay.parametresizIsim}/${hikayeId}")
                },
                hikayeOkuSayfasinaGit = { hikayeId, email1->
                    navController.navigate("${BottomBarSayfalari.HikayeOku.parametresizIsim}/${hikayeId}/${email1}")
                },
                email = email!!,
                kullaniciAdi = kullaniciAdi!!
            )
        }

        composable(
            route = BottomBarSayfalari.HikayeDetay.rota,
            arguments = listOf(
                navArgument("hikayeId") {type = NavType.StringType}
            )
        ) {
            val hikayeId = it.arguments?.getString("hikayeId")
            HikayeDetaySayfasi(
                hikayelerimSayfasinaGit = {
                    navController.navigateUp()
                },
                hikayeId = hikayeId!!
            )
        }

        composable(
            route = BottomBarSayfalari.HikayeOku.rota,
            arguments = listOf(
                navArgument("hikayeId") {type = NavType.StringType},
                navArgument("email") {type = NavType.StringType}
            )
        ) {
            val hikayeId = it.arguments?.getString("hikayeId")
            val email = it.arguments?.getString("email")
            HikayeOkuSayfasi(
                hikayeId = hikayeId!!,
                email = email!!,
                profilZiyaretSayfasinaGit = {
                    navController.navigateUp()
                }
            )
        }

        composable(route = BottomBarSayfalari.Kutuphane.rota) {
            KutuphaneSayfasi(
                profilSayfasinaGit = {
                    navController.navigateUp()
                },
                hikayeOkuSayfasinaGit = {hikayeId,email->
                    navController.navigate("${BottomBarSayfalari.HikayeOku.parametresizIsim}/${hikayeId}/${email}")
                },
                hikayeleriKesfetSayfasinaGit = {
                    navController.navigate(BottomBarSayfalari.HikayeleriKesfet.rota)
                }
            )
        }

        composable(route = BottomBarSayfalari.HikayeleriKesfet.rota) {
            HikayeleriKesfetSayfasi(
                geriDon = {
                    navController.navigateUp()
                },
                hikayeOkuSayfasinaGit = {hikayeId,email->
                    navController.navigate("${BottomBarSayfalari.HikayeOku.parametresizIsim}/${hikayeId}/${email}")
                }
            )
        }
    }
}