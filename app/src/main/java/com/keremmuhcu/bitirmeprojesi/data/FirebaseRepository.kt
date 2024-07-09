package com.keremmuhcu.bitirmeprojesi.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.toObject
import com.keremmuhcu.bitirmeprojesi.data.models.Hikaye
import com.keremmuhcu.bitirmeprojesi.data.models.HikayeBolum
import com.keremmuhcu.bitirmeprojesi.data.models.KullaniciYarismaHikayesi
import com.keremmuhcu.bitirmeprojesi.data.models.YarismaHikayesi
import com.keremmuhcu.bitirmeprojesi.data.models.Yazar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FirebaseRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    val aktifKullanici = firebaseAuth.currentUser


    private val firestore = FirebaseFirestore.getInstance()

    private val saatVeTarihCollection = firestore.collection("saatVeTarih")
    private val yarismaHikayeleriCollection = firestore.collection("yarismaHikayeleri")
    private val yazarHikayeleriCollection = firestore.collection("yazarHikayeleri")
    private val yazarlarCollection = firestore.collection("yazarlar")

    private val _bekleyenYarismaHikayeleriListesi =
        MutableStateFlow<List<KullaniciYarismaHikayesi>>(emptyList())
    val bekleyenYarismaHikayeleriListesi: StateFlow<List<KullaniciYarismaHikayesi>> =
        _bekleyenYarismaHikayeleriListesi

    private val _hikayeBolumleriListesi =
        mutableStateOf<List<HikayeBolum>>(emptyList())
    val hikayeBolumleriListesi: State<List<HikayeBolum>> =
        _hikayeBolumleriListesi

    var bolumlerListenerTakip:ListenerRegistration?=null

    fun kayitOl(
        kullaniciAdi: String,
        email: String, sifre: String,
        alinmis: (String) -> Unit
    ) {
        kullaniciAdiAlinmisMi(kullaniciAdi) { mevcutMu, _ ->
            if (!mevcutMu) {
                firebaseAuth
                    .createUserWithEmailAndPassword(email, sifre)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            alinmis("Kullanıcı Adı Alınmamış")
                            val yazar = Yazar(kullaniciAdi, email, "yazar")
                            yazarlaraKaydet(yazar)
                        }
                    }
                    .addOnFailureListener {
                        println("Kullanıcı register edilemedi!")
                        alinmis("Email Internet Kontrol")
                    }


            } else {
                alinmis("Kullanıcı Adı Alınmış")
            }
        }
    }

    private fun yazarlaraKaydet(yazar: Yazar) {

        yazarlarCollection
            .document(yazar.email)
            .set(yazar)
            .addOnSuccessListener {
                println("Firebase Kullanici Kayit: Yazarlar collections'ına başarıyla eklendi.")
            }
            .addOnFailureListener {
                println("Firebase Kullanici Kayit: Yazarlar collections'ına eklenemedi.")
            }
    }

    fun kullaniciAdiAlinmisMi(
        kullaniciAdi: String,
        mevcutMu: (Boolean, String?) -> Unit
    ) {
        yazarlarCollection
            .whereEqualTo("kullaniciAdi", kullaniciAdi)
            .get(Source.SERVER)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val kullaniciAdlari = it.result?.documents
                    if (kullaniciAdlari.isNullOrEmpty()) {
                        mevcutMu(false, null)
                    } else {
                        mevcutMu(true, kullaniciAdlari[0].getString("email"))
                    }

                } else {
                    println("Kullanıcı Adı Alınmış")
                }
            }
            .addOnFailureListener {
                println("Firebase Kullanici Adi Kontrol HATA!")
            }
    }

    fun girisYap(
        kullaniciAdiVeyaEmail: String,
        sifre: String,
        girisBasariliMi: (Boolean) -> Unit
    ) {

        if (!kullaniciAdiVeyaEmail.contains("@")) {
            kullaniciAdiAlinmisMi(kullaniciAdiVeyaEmail) { mevcutMu, email ->

                if (!mevcutMu) {
                    girisBasariliMi(false)
                } else {
                    firebaseAuth.signInWithEmailAndPassword(email!!, sifre)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                println("Giriş Yapıldı")
                                girisBasariliMi(true)
                            } else {
                                println("Giriş Yapılamadı")
                                girisBasariliMi(false)
                            }
                        }
                        .addOnFailureListener {
                            println("Giriş Yapılamıyor. İnterneti Kontrol et.")
                            girisBasariliMi(false)
                        }
                }
            }
        } else {
            firebaseAuth.signInWithEmailAndPassword(kullaniciAdiVeyaEmail, sifre)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        println("Giriş Yapıldı")
                        girisBasariliMi(true)
                    } else {
                        println("Giriş Yapılamadı")
                        girisBasariliMi(false)
                    }
                }
                .addOnFailureListener {
                    println("Giriş Yapılamıyor. İnterneti Kontrol et.")
                    girisBasariliMi(false)
                }
        }
    }

    fun cikisYap(cikisBasariliMi: (Boolean) -> Unit) {
        firebaseAuth.signOut()
        val authStateListener = FirebaseAuth.AuthStateListener {
            if (it.currentUser == null) {
                cikisBasariliMi(true)
            } else {
                cikisBasariliMi(false)
            }

        }
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    fun aktifYazarBilgileriniAl(dondur: (Yazar) -> Unit) {
        yazarlarCollection.document(aktifKullanici?.email!!).get(Source.SERVER)
            .addOnSuccessListener {
                val aktifYazar = it.toObject<Yazar>()
                dondur(aktifYazar!!)
            }
            .addOnFailureListener {
                dondur(Yazar())
                println("Yazar bilgisi alınırken hata yaşandı.")
            }
    }

    fun yazarBilgileriniAl(email: String, dondur: (Yazar) -> Unit) {
        yazarlarCollection.document(email).get(Source.SERVER)
            .addOnSuccessListener {
                val yazar = it.toObject<Yazar>()
                dondur(yazar!!)
            }
            .addOnFailureListener {
                dondur(Yazar())
                println("Yazar bilgisi alınırken hata yaşandı.")
            }
    }

    // tarihi sisteme kaydedip tekrardan çekme işlemi.
    fun anlikSaatVerisiniFirebasedenAl(tarih: (Any?) -> Unit) {
        // anlık saat verisini servera göre kaydedecek
        val anlik = hashMapOf(
            "anlik" to FieldValue.serverTimestamp()
        )

        // saat verisini firestore'a kaydeder
        saatVeTarihCollection.document(aktifKullanici?.email!!).set(anlik)
            .addOnSuccessListener {
                // saat verisi firestore'a kaydedildiğinde bunu çekecek
                saatVeTarihCollection.document(aktifKullanici.email!!).get(Source.SERVER)
                    .addOnSuccessListener {
                        // çektiği saat verisini döndürür.
                        tarih(it.getTimestamp("anlik")?.toDate())
                    }
                    .addOnFailureListener {
                        println("Anlık Tarih Alınamadı.")
                        tarih("")
                    }
            }
            .addOnFailureListener {
                println("Başarısız! Tarih, sisteme yazılamadı.")
                tarih("")
            }


    }

    fun yarismayiGuncelle(yeniHikaye: String) {
        sonYarismayiGetir {yarismaHikayesi->
            if (yarismaHikayesi.anaHikaye != "") {
                yarismaHikayeleriCollection
                    .document(yarismaHikayesi.id!!)
                    .update("anaHikaye", yeniHikaye)
                    .addOnSuccessListener {

                    }
                    .addOnFailureListener {

                    }
            }
        }
    }

    fun yazarRoluGuncelle(email: String,rol:String, basariliMi: (Boolean) -> Unit) {
        yazarlarCollection
            .document(email)
            .update("rol", rol)
            .addOnSuccessListener {
                basariliMi(true)
            }
            .addOnFailureListener {
                basariliMi(false)
            }
    }

    fun yarismayiBitir(basariliMi: (Boolean) -> Unit) {
        sonYarismayiGetir { yarismaHikayesi->
            if (yarismaHikayesi.anaHikaye != "") {
                yarismaHikayeleriCollection
                    .document(yarismaHikayesi.id!!)
                    .update("bittiMi", true)
                    .addOnSuccessListener {
                        basariliMi(true)
                    }
                    .addOnFailureListener {
                        basariliMi(false)
                    }
            }
        }
    }

    fun sonYarismayiGetir(yarisma: (YarismaHikayesi) -> Unit) {
        yarismaHikayeleriCollection
            .orderBy("baslamaTarihi", Query.Direction.DESCENDING)
            .limit(1)
            .get(Source.SERVER)
            .addOnSuccessListener {
                yarisma(it.documents[0].toObject<YarismaHikayesi>()!!)
            }
            .addOnFailureListener {
                yarisma(YarismaHikayesi(null))
            }
    }


    fun yarismaHikayesiOlustur(
        anaHikaye: String,
        kacGunSurecek: Int,
        olusturulduMu: (Boolean) -> Unit
    ) {
        anlikSaatVerisiniFirebasedenAl {
            val tarihVerisi = it.toString()
            if (tarihVerisi.isNotEmpty()) {
                val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
                val documentIdOutputFormat = SimpleDateFormat("ddMMMyyyy", Locale("tr"))
                val date = inputFormat.parse(tarihVerisi)
                val documentIdFormattedStr = documentIdOutputFormat.format(date).lowercase()

                val calendar = Calendar.getInstance()
                val calendar1 = Calendar.getInstance()
                calendar.time = date
                calendar1.time = date
                calendar.add(Calendar.DAY_OF_MONTH, kacGunSurecek)
                calendar1.add(Calendar.DAY_OF_MONTH, kacGunSurecek + 2)

                val yarismaHikayesi = hashMapOf(
                    "anaHikaye" to anaHikaye,
                    "baslamaTarihi" to it,
                    "bitisTarihi" to Timestamp(calendar.time),
                    "oylamaTarihi" to Timestamp(calendar1.time),
                    "bittiMi" to false,
                    "id" to documentIdFormattedStr//documentIdFormattedStr
                )

                yarismaHikayeleriCollection.document(documentIdFormattedStr).set(yarismaHikayesi)
                    .addOnCompleteListener {
                        println("Yarışma hikayesi başarıyla kaydedildi")
                        olusturulduMu(true)
                    }
                    .addOnFailureListener {
                        olusturulduMu(false)
                        println("Yarışma hikayesi kaydedilemedi")
                    }
            } else {
                olusturulduMu(false)
            }
        }
    }

    fun kalanSureyiHesapla(kalanSure: (Any) -> Unit) {
        anlikSaatVerisiniFirebasedenAl { anlikTarih ->

            if (anlikTarih is Date) {
                sonYarismayiGetir { sonYarisma ->
                    if (sonYarisma.anaHikaye != null && sonYarisma.bittiMi == false) {
                        println("Aktif yarışma var.")
                        val bitisTarihi = sonYarisma.bitisTarihi
                        val dateFormat =
                            SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
                        val anlikTarihStr = dateFormat.parse(anlikTarih.toString())
                        val bitisTarihiStr = dateFormat.parse(bitisTarihi.toString())
                        println("Şimdiki zaman: $anlikTarihStr")
                        println("Bitiş zaman: $bitisTarihiStr")
                        val kalanSureMilisaniye = bitisTarihiStr.time - anlikTarihStr.time
                        kalanSure(kalanSureMilisaniye)
                    } else {
                        kalanSure("yarismaYok")
                    }

                }
            } else {
                kalanSure("İnternet")
            }
        }
    }

    fun oylamaSuresiniHesapla(kalanSure: (Long) -> Unit) {
        anlikSaatVerisiniFirebasedenAl { anlikTarih ->
            if (anlikTarih is Date) {
                sonYarismayiGetir { aktifYarisma ->
                    if (aktifYarisma.oylamaTarihi != null) {
                        val oylamaTarihi = aktifYarisma.oylamaTarihi
                        val dateFormat =
                            SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
                        val anlikTarihStr = dateFormat.parse(anlikTarih.toString())
                        val oylamaTarihiStr = dateFormat.parse(oylamaTarihi.toString())
                        println("Şimdiki zaman: $anlikTarihStr")
                        println("Bitiş zaman: $oylamaTarihiStr")
                        val kalanSureMilisaniye = oylamaTarihiStr.time - anlikTarihStr.time
                        kalanSure(kalanSureMilisaniye)
                    } else {
                        kalanSure(-1)
                    }

                }
            } else {
                kalanSure(-1)
            }
        }
    }

    fun yarismayiSil(basariliMi: (Boolean) -> Unit) {
        yarismaHikayeleriCollection.orderBy("baslamaTarihi", Query.Direction.DESCENDING).limit(1)
            .get(Source.SERVER)
            .addOnSuccessListener {
                val bekleyenlerCollectionReference = yarismaHikayeleriCollection.document(it.documents[0].id).collection("bekleyenler")
                val hikayelerCollectionReference = yarismaHikayeleriCollection.document(it.documents[0].id).collection("kullaniciYarismaHikayeleri")
                altKoleksiyonlariSil(listOf(bekleyenlerCollectionReference,hikayelerCollectionReference)) { basariDurumu->
                    if (basariDurumu) {
                        yarismaHikayeleriCollection.document(it.documents[0].id).delete()
                            .addOnSuccessListener {
                                basariliMi(true)
                            }
                            .addOnFailureListener {
                                basariliMi(false)
                            }
                    } else {
                        basariliMi(false)
                    }
                }

            }
            .addOnFailureListener {
                basariliMi(false)

            }
    }

    fun kullaniciYarismasiniTaslakKaydet(
        baslik: String,
        hikaye: String,
        basariliMi: (Boolean) -> Unit
    ) {
        val guncelle = hashMapOf(
            "baslikTaslak" to baslik,
            "hikayeTaslak" to hikaye
        )
        yazarlarCollection
            .document(aktifKullanici?.email!!)
            .collection("yazarinYarismaHikayeleri")
            .document("aktifYarisma")
            .set(guncelle)
            .addOnSuccessListener {
                println("Taslak başarıyla kaydedildi")
                basariliMi(true)
            }
            .addOnFailureListener {
                println("Taslak kaydedilemedi.")
                basariliMi(false)
            }
            .addOnCanceledListener {
                println("Taslak kaydedilemedi.")
            }
    }

    fun kullaniciYarismasiniTaslaktanAl(taslakHash: (HashMap<String, String?>) -> Unit) {
        yazarlarCollection.document(aktifKullanici?.email!!).collection("yazarinYarismaHikayeleri")
            .document("aktifYarisma").get(Source.SERVER)
            .addOnSuccessListener {
                val taslak = hashMapOf(
                    "baslik" to it.getString("baslikTaslak"),
                    "hikaye" to it.getString("hikayeTaslak")
                )
                taslakHash(taslak)
            }
            .addOnFailureListener {
                taslakHash(
                    hashMapOf(
                        "baslik" to "hataDurumuİnternetiKontrolEt"
                    )
                )
            }
    }

    fun kullaniciYarismaHikayesiniYoneticilereGonder(
        kullaniciYarismaHikayesi: KullaniciYarismaHikayesi,
        basariliMi: (Boolean) -> Unit
    ) {
        val veri = hashMapOf(
            "kullaniciYarismaHikayeBaslik" to kullaniciYarismaHikayesi.kullaniciYarismaHikayeBaslik,
            "kullaniciYarismaHikayesi" to kullaniciYarismaHikayesi.kullaniciYarismaHikayesi,
            "yazarKullaniciAdi" to kullaniciYarismaHikayesi.yazarKullaniciAdi,
            "yazarEmail" to aktifKullanici?.email!!,
            "kabulDurumu" to "bekliyor",
            "oylar" to kullaniciYarismaHikayesi.oylar,
            "olusturmaTarihi" to FieldValue.serverTimestamp(),
        )
        sonYarismayiGetir {
            if (it.anaHikaye == "") {
                basariliMi(false)
            } else {
                yarismaHikayeleriCollection
                    .document(it.id!!)
                    .collection("bekleyenler")
                    .document(aktifKullanici.email!!)
                    .set(veri)
                    .addOnSuccessListener {
                        basariliMi(true)
                        val sifirlaTaslak = hashMapOf(
                            "baslikTaslak" to "",
                            "hikayeTaslak" to ""
                        )
                        yazarlarCollection
                            .document(aktifKullanici.email!!)
                            .collection("yazarinYarismaHikayeleri")
                            .document("aktifYarisma")
                            .set(sifirlaTaslak)
                            .addOnSuccessListener {
                                println("Taslak başarıyla boşaltıldı")
                            }
                            .addOnFailureListener {
                                println("Hata! Taslak sıfırlanamadı.")
                            }
                    }
                    .addOnFailureListener {
                        basariliMi(false)
                    }
            }
        }
    }

    fun kullaniciYarHikDurumunuGetir(
        yazarRol: String,
        durum: (String) -> Unit
    ) {
        val collectionAdi = if (yazarRol == "yazar") {
            "bekleyenler"
        } else {
            "kullaniciYarismaHikayeleri"
        }
        sonYarismayiGetir { yarismaHikayesi ->
            yarismaHikayeleriCollection
                .document(yarismaHikayesi.id!!)
                .collection(collectionAdi)
                .document(aktifKullanici?.email!!)
                .get(Source.SERVER)
                .addOnSuccessListener {
                    if (it.exists()) {
                        println(it["kabulDurumu"].toString())
                        durum(it["kabulDurumu"].toString())
                    } else {
                        println("göndermemiş")
                        durum("gondermemis")
                    }

                }
                .addOnFailureListener {
                    durum("islemBasarisiz")
                    println("Göndermemiş. Başarısız")
                }

        }
    }

    fun onaylanacakYarismaHikayeleriniGetir(hataDurumu: (Boolean) -> Unit) {
        sonYarismayiGetir { yarismaHikayesi ->
            if (yarismaHikayesi.anaHikaye == null) {
                hataDurumu(true)
            } else {
                yarismaHikayeleriCollection
                    .document(yarismaHikayesi.id!!)
                    .collection("bekleyenler")
                    .orderBy("olusturmaTarihi", Query.Direction.ASCENDING)
                    .addSnapshotListener { snapshot, hata ->
                        if (hata != null) {
                            hataDurumu(true)
                            return@addSnapshotListener
                        }

                        val kullaniciYarismaHikayeleri = snapshot?.documents?.mapNotNull {
                            if (it["kabulDurumu"] == "bekliyor")
                                it.toObject<KullaniciYarismaHikayesi>()
                            else
                                null
                        } ?: emptyList()

                        _bekleyenYarismaHikayeleriListesi.value = kullaniciYarismaHikayeleri
                        hataDurumu(false)

                    }
            }

        }
    }

    fun yarismayiOnayla(
        kullaniciYarismaHikayesi: KullaniciYarismaHikayesi,
        hataDurumu: (Boolean) -> Unit
    ) {
        val veri = hashMapOf(
            "kabulDurumu" to "onaylandi",
        )

        sonYarismayiGetir { yarismaHikayesi ->
            if (yarismaHikayesi.anaHikaye == null) {
                hataDurumu(true)
            } else {
                yarismaHikayeleriCollection
                    .document(yarismaHikayesi.id!!)
                    .collection("bekleyenler")
                    .document(kullaniciYarismaHikayesi.yazarEmail!!)
                    .update(veri as Map<String, Any>)
                    .addOnSuccessListener {
                        onaylananYarismayiTasi(kullaniciYarismaHikayesi ,"yazar") {

                        }
                        hataDurumu(false)
                    }
                    .addOnFailureListener {
                        hataDurumu(false)
                    }
            }
        }

    }

    fun yarismayiReddet(
        kullaniciYarismaHikayesi: KullaniciYarismaHikayesi,
        hataDurumu: (Boolean) -> Unit
    ) {
        val veri = hashMapOf(
            "kabulDurumu" to "reddedildi",
        )

        sonYarismayiGetir { yarismaHikayesi ->
            if (yarismaHikayesi.anaHikaye == null) {
                hataDurumu(true)
            } else {
                yarismaHikayeleriCollection
                    .document(yarismaHikayesi.id!!)
                    .collection("bekleyenler")
                    .document(kullaniciYarismaHikayesi.yazarEmail!!)
                    .update(veri as Map<String, Any>)
                    .addOnSuccessListener {
                        hataDurumu(false)
                    }
                    .addOnFailureListener {
                        hataDurumu(false)
                    }
            }
        }

    }

    fun onaylananYarismayiTasi(
        kullaniciYarismaHikayesi: KullaniciYarismaHikayesi,
        yazarRol: String,
        hataDurumu: (Boolean) -> Unit

    ) {
        val veri = if (yazarRol == "yazar") {
            println("yazarRol: $yazarRol")
            hashMapOf(
                "kullaniciYarismaHikayeBaslik" to kullaniciYarismaHikayesi.kullaniciYarismaHikayeBaslik,
                "kullaniciYarismaHikayesi" to kullaniciYarismaHikayesi.kullaniciYarismaHikayesi,
                "yazarKullaniciAdi" to kullaniciYarismaHikayesi.yazarKullaniciAdi,
                "yazarEmail" to kullaniciYarismaHikayesi.yazarEmail,
                "oylar" to kullaniciYarismaHikayesi.oylar,
                "olusturmaTarihi" to kullaniciYarismaHikayesi.olusturmaTarihi,
            )
        } else {
            hashMapOf(
                "kullaniciYarismaHikayeBaslik" to kullaniciYarismaHikayesi.kullaniciYarismaHikayeBaslik,
                "kullaniciYarismaHikayesi" to kullaniciYarismaHikayesi.kullaniciYarismaHikayesi,
                "yazarKullaniciAdi" to kullaniciYarismaHikayesi.yazarKullaniciAdi,
                "yazarEmail" to aktifKullanici?.email!!,
                "oylar" to kullaniciYarismaHikayesi.oylar,
                "olusturmaTarihi" to FieldValue.serverTimestamp(),
            )
        }

        sonYarismayiGetir { yarismaHikayesi ->
            if (yarismaHikayesi.anaHikaye != null) {
                yarismaHikayeleriCollection
                    .document(yarismaHikayesi.id!!)
                    .collection("kullaniciYarismaHikayeleri")
                    .document(veri["yazarEmail"].toString())
                    .set(veri)
                    .addOnSuccessListener {
                        hataDurumu(false)
                    }
                    .addOnFailureListener {
                        hataDurumu(true)
                    }
            } else {
                hataDurumu(true)
            }
        }
    }

    fun kullanicininYarismaHikayesiniGetir(
        yazarRol: String,
        yarisma: (KullaniciYarismaHikayesi) -> Unit
    ) {
        val collectionAdi = if (yazarRol == "yazar") {
            "bekleyenler"
        } else {
            "kullaniciYarismaHikayeleri"
        }

        sonYarismayiGetir { yarismaHikayesi ->
            if (yarismaHikayesi.anaHikaye != null) {
                yarismaHikayeleriCollection
                    .document(yarismaHikayesi.id!!)
                    .collection(collectionAdi)
                    .document(aktifKullanici?.email!!)
                    .get(Source.SERVER)
                    .addOnSuccessListener {
                        yarisma(
                            KullaniciYarismaHikayesi(
                                it["kullaniciYarismaHikayeBaslik"] as String,
                                it["kullaniciYarismaHikayesi"] as String
                            )
                        )
                    }
                    .addOnFailureListener {
                        yarisma(KullaniciYarismaHikayesi(null, null))
                    }

            } else {
                yarisma(KullaniciYarismaHikayesi(null, null))
            }
        }
    }

    fun onaylanmisKullaniciYarismaHikayeleriniGetir(
        hangiYarisma:String,
        sonuc:(Boolean,List<KullaniciYarismaHikayesi>) -> Unit
    ) {
        if (hangiYarisma == "son") {
            sonYarismayiGetir { yarismaHikayesi ->
                //.orderBy("olusturmaTarihi", Query.Direction.ASCENDING)

                if (yarismaHikayesi.anaHikaye != null) {
                    yarismaHikayeleriCollection
                        .document(yarismaHikayesi.id!!)
                        .collection("kullaniciYarismaHikayeleri")
                        .orderBy("oylar", Query.Direction.ASCENDING)
                        .get(Source.SERVER)
                        .addOnSuccessListener {
                            val kullaniciYarismaHikayeleri = it.documents.mapNotNull { hikayeler->
                                hikayeler.toObject<KullaniciYarismaHikayesi>()
                            }
                            sonuc(false,kullaniciYarismaHikayeleri)
                        }
                        .addOnFailureListener {
                            sonuc(true, emptyList())
                        }
                        /*.addSnapshotListener { snapshot, hata ->
                            if (hata != null) {
                                hataDurumu(true)
                                return@addSnapshotListener
                            }

                            val kullaniciYarismaHikayeleri = snapshot?.documents?.mapNotNull {
                                it.toObject<KullaniciYarismaHikayesi>()
                            } ?: emptyList()

                            _onaylanmisKullaniciYarismaHikayeleri.value = kullaniciYarismaHikayeleri
                            _onaylanmisHikayelerYedek.value = kullaniciYarismaHikayeleri
                            //_onaylanmisKullaniciYarismaHikayeleri.value = kullaniciYarismaHikayeleri
                            hataDurumu(false)

                        }*/
                }
            }
        } else {
            yarismaHikayeleriCollection
                .document(hangiYarisma)
                .collection("kullaniciYarismaHikayeleri")
                .orderBy("oylar", Query.Direction.ASCENDING)
                .get(Source.SERVER)
                .addOnSuccessListener {
                    val kullaniciYarismaHikayeleri = it.documents.mapNotNull { hikayeler->
                        hikayeler.toObject<KullaniciYarismaHikayesi>()
                    }
                    sonuc(false,kullaniciYarismaHikayeleri)
                }
                .addOnFailureListener {
                    sonuc(true, emptyList())
                }

                /*.addSnapshotListener { snapshot, hata ->
                    if (hata != null) {
                        hataDurumu(true)
                        return@addSnapshotListener
                    }

                    val kullaniciYarismaHikayeleri = snapshot?.documents?.mapNotNull {
                        it.toObject<KullaniciYarismaHikayesi>()
                    } ?: emptyList()

                    _onaylanmisKullaniciYarismaHikayeleri.value = kullaniciYarismaHikayeleri
                    _onaylanmisHikayelerYedek.value = kullaniciYarismaHikayeleri
                    //_onaylanmisKullaniciYarismaHikayeleri.value = kullaniciYarismaHikayeleri
                    hataDurumu(false)

                }*/
        }
    }

    fun yarismayaOyVer(
        kullaniciYarismaHikayesi: KullaniciYarismaHikayesi,
        hataDurumu: (Boolean) -> Unit
    ) {
        sonYarismayiGetir {yarismaHikayesi ->
            val arttir = hashMapOf(
                "oylar" to FieldValue.increment(1L)
            )
            if (yarismaHikayesi.anaHikaye != null) {
                println(kullaniciYarismaHikayesi.yazarEmail)
                yarismaHikayeleriCollection
                    .document(yarismaHikayesi.id!!)
                    .collection("kullaniciYarismaHikayeleri")
                    .document(kullaniciYarismaHikayesi.yazarEmail!!)
                    .update(arttir as Map<String, Any>)
                    .addOnSuccessListener {
                        println(kullaniciYarismaHikayesi.yazarEmail)
                        val verilenOy = hashMapOf(
                            "verilenOy" to kullaniciYarismaHikayesi.yazarEmail
                        )
                        yazarlarCollection
                            .document(aktifKullanici?.email!!)
                            .collection("yazarinYarismaHikayeleri")
                            .document("aktifYarisma")
                            .update(verilenOy as Map<String,Any>)
                            .addOnSuccessListener {
                                hataDurumu(false)
                            }
                            .addOnFailureListener {
                                hataDurumu(true)
                            }
                    }
                    .addOnFailureListener {
                        hataDurumu(true)
                    }
            } else{
                hataDurumu(true)
            }

        }
    }

    fun yazarOyVermisMi(vermisMi:(Any) -> Unit) {
        yazarlarCollection
            .document(aktifKullanici?.email!!)
            .collection("yazarinYarismaHikayeleri")
            .document("aktifYarisma")
            .get()
            .addOnSuccessListener {
                if (it["verilenOy"] != null) {
                    if (it["verilenOy"] != "") {
                        vermisMi(it["verilenOy"]!!)
                    } else {
                        vermisMi(false)
                    }
                }
                else {
                    vermisMi(false)
                }
            }
            .addOnFailureListener {
                vermisMi("Hata")
            }

    }

    fun yarismaBittiktenSonraBilgileriGir(
        onaylanmisYarismaHikayeleri:List<KullaniciYarismaHikayesi>,
        basariliMi: (Boolean) -> Unit
    ) {
        for (hikayeBilgisi in onaylanmisYarismaHikayeleri) {
            val derece = onaylanmisYarismaHikayeleri.indexOfFirst { it.yazarEmail == hikayeBilgisi.yazarEmail } + 1
            val veri = hashMapOf(
                    "kullaniciYarismaHikayeBaslik" to hikayeBilgisi.kullaniciYarismaHikayeBaslik,
                    "kullaniciYarismaHikayesi" to hikayeBilgisi.kullaniciYarismaHikayesi,
                    "yazarKullaniciAdi" to hikayeBilgisi.yazarKullaniciAdi,
                    "yazarEmail" to hikayeBilgisi.yazarEmail,
                    "oylar" to hikayeBilgisi.oylar,
                    "olusturmaTarihi" to hikayeBilgisi.olusturmaTarihi,
                    "derece" to derece.toString()
            )

            sonYarismayiGetir { yarismaHikayesi->
                if (yarismaHikayesi.anaHikaye != null) {
                    veri["yarismaTarihi"] = yarismaHikayesi.baslamaTarihi
                    yazarlarCollection
                        .document(hikayeBilgisi.yazarEmail!!)
                        .collection("yazarinYarismaHikayeleri")
                        .document(yarismaHikayesi.id!!)
                        .set(veri)
                        .addOnSuccessListener {
                            verilenOylariTemizle(hikayeBilgisi) {verilenOyTemizlendiMi->
                                if (verilenOyTemizlendiMi) {
                                    basariliMi(true)
                                } else {
                                    basariliMi(false)
                                }

                            }
                        }
                        .addOnFailureListener {
                            basariliMi(false)
                        }

                }
            }
        }
    }



    fun verilenOylariTemizle(
        hikayeBilgisi:KullaniciYarismaHikayesi,
        basariliMi: (Boolean) -> Unit
    ) {

        val verilenOy = hashMapOf(
            "verilenOy" to ""
        )

        yazarlarCollection
            .document(hikayeBilgisi.yazarEmail!!)
            .collection("yazarinYarismaHikayeleri")
            .document("aktifYarisma")
            .set(verilenOy)
            .addOnSuccessListener {
                basariliMi(true)
            }
            .addOnFailureListener {
                basariliMi(false)
            }
    }

    fun bitenYarismalariGetir(basariliMi: (Boolean, List<YarismaHikayesi>) -> Unit) {
        yarismaHikayeleriCollection
            .orderBy("baslamaTarihi", Query.Direction.DESCENDING)
            .get(Source.SERVER)
            .addOnSuccessListener {bitenYarismalar->
                val yarismalar = bitenYarismalar.documents.mapNotNull {yarisma->
                    if (yarisma["bittiMi"] == true) {
                        yarisma.toObject<YarismaHikayesi>()
                    } else {
                        null
                    }
                }
                basariliMi(true, yarismalar)

            }
            .addOnFailureListener {
                basariliMi(false, emptyList())
            }
    }

    fun kullanicininYarismaHikayeleriniAl(
        email: String,
        basariliMi: (Boolean, List<KullaniciYarismaHikayesi>) -> Unit
    ) {

        yazarlarCollection
            .document(email)
            .collection("yazarinYarismaHikayeleri")
            .get(Source.SERVER)
            .addOnSuccessListener {
                val yarismalar = it.documents.mapNotNull {yarisma->
                    if (yarisma.id != "aktifYarisma") {
                        yarisma.toObject<KullaniciYarismaHikayesi>()
                    } else {
                        null
                    }

                }
                basariliMi(true, yarismalar)
            }
            .addOnFailureListener {
                basariliMi(false, emptyList())
            }
    }

    fun tiklananHikayeninAnaHikayesiniGetir(
        kullaniciYarismaHikayesi: KullaniciYarismaHikayesi,
        basariliMi: (Boolean,String) -> Unit
    ) {
        yarismaHikayeleriCollection
            .get(Source.SERVER)
            .addOnSuccessListener {yarismaHikayeleri->
                for (hikaye in yarismaHikayeleri) {
                    val baslamaTarihi = hikaye.getTimestamp("baslamaTarihi")
                    if (baslamaTarihi != null) {
                        val hikayeTarihi = baslamaTarihi.toDate()

                        // Calendar nesnelerini oluştur ve tarih bileşenlerini ayıkla
                        val hikayeninTarihi = Calendar.getInstance().apply { time = hikayeTarihi }
                        val yarismaninTarihi = Calendar.getInstance().apply { time = kullaniciYarismaHikayesi.yarismaTarihi }

                        val aynıGunAyYilMi = hikayeninTarihi.get(Calendar.YEAR) == yarismaninTarihi.get(Calendar.YEAR) &&
                                hikayeninTarihi.get(Calendar.MONTH) == yarismaninTarihi.get(Calendar.MONTH) &&
                                hikayeninTarihi.get(Calendar.DAY_OF_MONTH) == yarismaninTarihi.get(Calendar.DAY_OF_MONTH)

                        if (aynıGunAyYilMi) {
                            basariliMi(true, hikaye["anaHikaye"] as String)
                            return@addOnSuccessListener
                        }
                    }
                }
                basariliMi(false, "")
            }
            .addOnFailureListener {
                basariliMi(false,"")
            }
    }

    fun yeniHikayeyiYayinlaYaDaKaydet(
        hikaye: Hikaye,
        girisBolum: HikayeBolum,
        ilkBolum:HikayeBolum,
        kullaniciAdi: String,
        basariliMi: (Boolean,String) -> Unit
    ) {

        val hikayeVeri = hashMapOf(
            "baslik" to hikaye.baslik,
            "id" to hikaye.id,
            "yayinlandiMi" to hikaye.yayinlandiMi,
            "bittiMi" to false,
            "yazarEmail" to aktifKullanici?.email!!,
            "yazarKullaniciAdi" to kullaniciAdi,
            "olusturmaTarihi" to FieldValue.serverTimestamp(),
            "guncellenmeTarihi" to FieldValue.serverTimestamp()
        )

        yazarlarCollection
            .document(aktifKullanici.email!!)
            .collection("hikayeleri")
            .get(Source.SERVER)
            .addOnSuccessListener {
                val hikayeler = it.documents
                if (it.documents.isNotEmpty()) {
                    for (sistemdekiHikaye in hikayeler) {
                        if (sistemdekiHikaye.getString("baslik") == hikaye.baslik!!) {
                            basariliMi(false, "Başarısız! Bu başlıkta bir hikayeniz var.")
                            return@addOnSuccessListener
                        }
                    }

                    yazarlarCollection
                        .document(aktifKullanici.email!!)
                        .collection("hikayeleri")
                        .document(hikaye.id!!)
                        .set(hikayeVeri)
                        .addOnSuccessListener {
                            hikayeBolumuKaydet(hikaye.id,girisBolum) { basariDurumu1,_ ->
                                if (basariDurumu1) {
                                    hikayeBolumuKaydet(hikaye.id,ilkBolum) { basariDurumu2, _ ->
                                        if (basariDurumu2) {
                                            yazarHikayelerineKaydet(hikaye, kullaniciAdi) { basariDurumu3 ->
                                                if (basariDurumu3) {
                                                    basariliMi(true,"Başarıyla kaydedildi")

                                                } else {
                                                    basariliMi(false, "Başarısız! Kaydedilemedi")
                                                }
                                            }
                                        } else {
                                            basariliMi(false, "Başarısız! Kaydedilemedi")
                                        }

                                    }
                                } else {
                                    basariliMi(false, "Başarısız! Kaydedilemedi")
                                }
                            }

                        }
                        .addOnFailureListener {
                            basariliMi(false, "Başarısız! Kaydedilemedi")
                        }

                } else {
                    yazarlarCollection
                        .document(aktifKullanici.email!!)
                        .collection("hikayeleri")
                        .document(hikaye.id!!)
                        .set(hikayeVeri)
                        .addOnSuccessListener {
                            hikayeBolumuKaydet(hikaye.id,girisBolum) { basariDurumu1,_ ->
                                if (basariDurumu1) {
                                    hikayeBolumuKaydet(hikaye.id,ilkBolum) { basariDurumu2, _ ->
                                        if (basariDurumu2) {
                                            yazarHikayelerineKaydet(hikaye, kullaniciAdi) { basariDurumu3 ->
                                                if (basariDurumu3) {
                                                    basariliMi(true,"Başarıyla kaydedildi")

                                                } else {
                                                    basariliMi(false, "Başarısız! Kaydedilemedi")
                                                }
                                            }
                                        } else {
                                            basariliMi(false, "Başarısız! Kaydedilemedi")
                                        }

                                    }
                                } else {
                                    basariliMi(false, "Başarısız! Kaydedilemedi")
                                }
                            }

                        }
                        .addOnFailureListener {
                            basariliMi(false, "Başarısız! Kaydedilemedi")
                        }

                }
            }
            .addOnFailureListener {
                println("Buraya girdi, başarısız.")
                basariliMi(false, "Başarısız! Kaydedilemedi")
            }
    }

    fun hikayeBolumuKaydet(
        hikayeId:String,
        hikayeBolum: HikayeBolum,
        basariliMi: (Boolean,String) -> Unit
    ) {
        val hikayeBolumVeri = hashMapOf(
            "hikaye" to hikayeBolum.hikaye,
            "bolumId" to hikayeBolum.bolumId,
            "yayinlandiMi" to hikayeBolum.yayinlandiMi
        )

        val guncellenmeTarihi = hashMapOf(
            "guncellenmeTarihi" to FieldValue.serverTimestamp()
        )
        yazarlarCollection
            .document(aktifKullanici?.email!!)
            .collection("hikayeleri")
            .document(hikayeId)
            .collection("bolumler")
            .document(hikayeBolum.bolumId!!.toString())
            .set(hikayeBolumVeri)
            .addOnSuccessListener {
                yazarlarCollection
                    .document(aktifKullanici.email!!)
                    .collection("hikayeleri")
                    .document(hikayeId)
                    .update(guncellenmeTarihi as Map<String, Any>)
                    .addOnSuccessListener {
                        basariliMi(true, "Başarıyla kaydedildi")
                    }
                    .addOnFailureListener {
                        basariliMi(false, "Güncellenme tarihi kaydedilemedi")
                    }

            }
            .addOnFailureListener {
                basariliMi(false, "Başarısız! Kaydedilemedi")
            }
    }

    fun kullanicininHikayeleriniGetir(
        email: String,
        basariliMi: (Boolean, List<Hikaye>) -> Unit
    ) {

        /*val documentId = if (email == "aktif") {
            aktifKullanici?.email!!
        } else {
            email
        }*/

        yazarlarCollection
            .document(email)
            .collection("hikayeleri")
            .get(Source.SERVER)
            .addOnSuccessListener {
                val hikayeler = it.documents.mapNotNull {hikaye->
                    hikaye.toObject<Hikaye>()
                }
                basariliMi(true, hikayeler)
            }
            .addOnFailureListener {
                basariliMi(false, emptyList())
            }
    }

    fun hikayeyiGetir(
        email: String,
        hikayeId:String,
        basariliMi: (Boolean, Hikaye) -> Unit
    ) {
        val documentId = if (email == "aktif") {
            aktifKullanici?.email!!
        } else {
            email
        }

        yazarlarCollection
            .document(documentId)
            .collection("hikayeleri")
            .document(hikayeId)
            .get(Source.SERVER)
            .addOnSuccessListener {
                val hikaye = it.toObject<Hikaye>()
                basariliMi(true, hikaye!!)
            }
            .addOnFailureListener {
                basariliMi(false, Hikaye())
            }
    }

    fun hikayeninBolumleriniGetir(
        email: String,
        hikayeId:String,
        basariliMi: (Boolean, List<HikayeBolum>?) -> Unit
    ) {

        if (email == "aktif") {
            bolumlerListenerTakip = yazarlarCollection
                .document(aktifKullanici?.email!!)
                .collection("hikayeleri")
                .document(hikayeId)
                .collection("bolumler")
                .addSnapshotListener {snapshot,hata->
                    if (hata != null) {
                        basariliMi(false,null)
                        return@addSnapshotListener
                    }

                    val hikayeBolumleri = snapshot?.documents?.mapNotNull {
                        it.toObject<HikayeBolum>()
                    } ?: emptyList()

                    _hikayeBolumleriListesi.value = hikayeBolumleri
                    basariliMi(true,null)

                }
        } else {
            println("Buraya girdi")
            yazarlarCollection
                .document(email)
                .collection("hikayeleri")
                .document(hikayeId)
                .collection("bolumler")
                .get(Source.SERVER)
                .addOnSuccessListener {veri->
                    val liste = veri.documents.map {
                        it.toObject<HikayeBolum>()!!
                    }
                    basariliMi(true,liste)
                }
                .addOnFailureListener {
                    basariliMi(false,null)
                }
        }

    }

    fun bolumleriYayinla(
        hikayeId: String,
        yayinlanacakBolumler:List<HikayeBolum>,
        basariliMi: (Boolean) -> Unit
    ) {
        for (hikaye in yayinlanacakBolumler) {
            yazarlarCollection
                .document(aktifKullanici?.email!!)
                .collection("hikayeleri")
                .document(hikayeId)
                .collection("bolumler")
                .document(hikaye.bolumId!!.toString())
                .update("yayinlandiMi", true)
                .addOnSuccessListener {
                    basariliMi(true)
                }
                .addOnFailureListener {
                    basariliMi(false)
                }
        }
    }

    fun hikayeyiYayinla(
        hikayeId: String,
        basariliMi: (Boolean) -> Unit
    ) {
        yazarlarCollection
            .document(aktifKullanici?.email!!)
            .collection("hikayeleri")
            .document(hikayeId)
            .update("yayinlandiMi", true)
            .addOnSuccessListener {
                yazarlarCollection.document(aktifKullanici.email!!).get()
                    .addOnSuccessListener {
                        val kullaniciAdi = it.getString("kullaniciAdi")
                        val id = hikayeId + kullaniciAdi!!.capitalize(Locale.ROOT)
                        yazarHikayeleriCollection
                            .document(id)
                            .update("yayinlandiMi", true)
                            .addOnSuccessListener {
                                basariliMi(true)
                            }
                            .addOnFailureListener {
                                basariliMi(false)
                            }
                    }
                    .addOnFailureListener {
                        basariliMi(false)
                    }
            }
            .addOnFailureListener {
                basariliMi(false)
            }
    }

    fun hikayeyiFinalleYaDaDevamEttir(
        durum:Boolean,
        hikayeId: String,
        basariliMi: (Boolean) -> Unit
    ) {
        yazarlarCollection
            .document(aktifKullanici?.email!!)
            .collection("hikayeleri")
            .document(hikayeId)
            .update("bittiMi", durum)
            .addOnSuccessListener {
                basariliMi(true)
            }
            .addOnFailureListener {
                basariliMi(false)
            }
    }

    fun hikayeyiSil(hikayeId: String, basariliMi: (Boolean) -> Unit) {
        val bolumlerCollectionReference = yazarlarCollection
            .document(aktifKullanici?.email!!)
            .collection("hikayeleri")
            .document(hikayeId)
            .collection("bolumler")

        val kutuphanesineEkleyenlerCollectionReference = yazarlarCollection
            .document(aktifKullanici.email!!)
            .collection("hikayeleri")
            .document(hikayeId)
            .collection("kutuphanesine_ekleyenler")

        kutuphanesineEkleyenlerCollectionReference
            .get()
            .addOnSuccessListener {ekleyenler ->
                for (ekleyen in ekleyenler) {
                    yazarlarCollection
                        .document(ekleyen.id)
                        .collection("kutuphane")
                        .document(hikayeId)
                        .delete()
                        .addOnSuccessListener {

                        }
                        .addOnFailureListener {
                            basariliMi(false)
                        }
                }

            }

        yazarlarCollection.document(aktifKullanici.email!!)
            .get()
            .addOnSuccessListener {
                val id = hikayeId + it.getString("kullaniciAdi")!!.capitalize(Locale.ROOT)
                yazarHikayeleriCollection
                    .document(id)
                    .delete()
                    .addOnSuccessListener {

                    }
                    .addOnFailureListener {

                    }
            }

        altKoleksiyonlariSil(listOf(bolumlerCollectionReference, kutuphanesineEkleyenlerCollectionReference)) {
            if(it) {
                yazarlarCollection
                    .document(aktifKullanici.email!!)
                    .collection("hikayeleri")
                    .document(hikayeId)
                    .delete()
                    .addOnSuccessListener {
                        basariliMi(true)
                    }
                    .addOnFailureListener {
                        basariliMi(false)
                    }
            } else {
                basariliMi(false)
            }
        }
    }

    fun bolumSil(hikayeId: String, bolumId:String,basariliMi: (Boolean) -> Unit) {
        yazarlarCollection
            .document(aktifKullanici?.email!!)
            .collection("hikayeleri")
            .document(hikayeId)
            .collection("bolumler")
            .document(bolumId)
            .delete()
            .addOnSuccessListener {
                basariliMi(true)
            }
            .addOnFailureListener {
                basariliMi(false)
            }
    }

    fun bolumuDuzenle(
        hikayeId: String,
        bolumId:String,
        hikayeMetni:String,
        baslik: String?,
        basariliMi: (Boolean) -> Unit
    ) {
        if (baslik != null) {
            yazarlarCollection
                .document(aktifKullanici?.email!!)
                .collection("hikayeleri")
                .document(hikayeId)
                .update("baslik", baslik)
                .addOnSuccessListener {
                    yazarlarCollection
                        .document(aktifKullanici.email!!)
                        .collection("hikayeleri")
                        .document(hikayeId)
                        .collection("bolumler")
                        .document(bolumId)
                        .update("hikaye", hikayeMetni)
                        .addOnSuccessListener {
                            basariliMi(true)
                        }
                        .addOnFailureListener {
                            basariliMi(false)
                        }
                    basariliMi(true)
                }
                .addOnFailureListener {
                    basariliMi(false)
                }
        } else {
            yazarlarCollection
                .document(aktifKullanici?.email!!)
                .collection("hikayeleri")
                .document(hikayeId)
                .collection("bolumler")
                .document(bolumId)
                .update("hikaye", hikayeMetni)
                .addOnSuccessListener {
                    basariliMi(true)
                }
                .addOnFailureListener {
                    basariliMi(false)
                }
        }

    }

    fun kalinanYeriKaydet(hikayeId: String,kalinanBolum: Int,basariliMi: (Boolean) -> Unit) {
        yazarlarCollection
            .document(aktifKullanici?.email!!)
            .collection("kutuphane")
            .document(hikayeId)
            .update("kalinanBolum",kalinanBolum)
            .addOnSuccessListener {
                hikayeninOkunmaTarihiniGuncelle(hikayeId)
                basariliMi(true)
            }
            .addOnFailureListener {
                basariliMi(false)
            }
    }

    fun hikayeninOkunmaTarihiniGuncelle(hikayeId: String) {
        yazarlarCollection
            .document(aktifKullanici?.email!!)
            .collection("kutuphane")
            .document(hikayeId)
            .update("okunmaTarihi",FieldValue.serverTimestamp())
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
    }

    fun hikayeyiKutuphaneyeEkleCikar(
        hikaye: Hikaye,
        eklenecekMi:Boolean,
        kalinanBolum:Int,
        basariliMi: (Boolean) -> Unit,
    ) {
        if (eklenecekMi) {
            val veri = hashMapOf(
                "id" to hikaye.id,
                "baslik" to hikaye.baslik,
                "yazarKullaniciAdi" to hikaye.yazarKullaniciAdi,
                "yazarEmail" to hikaye.yazarEmail,
                "kalinanBolum" to kalinanBolum,
                "okunmaTarihi" to FieldValue.serverTimestamp()
            )
            yazarlarCollection
                .document(aktifKullanici?.email!!)
                .collection("kutuphane")
                .document(hikaye.id!!)
                .set(veri)
                .addOnSuccessListener {
                    //basariliMi(true)
                    yazarlarCollection
                        .document(hikaye.yazarEmail!!)
                        .collection("hikayeleri")
                        .document(hikaye.id)
                        .collection("kutuphanesine_ekleyenler")
                        .document(aktifKullanici.email!!)
                        .set(hashMapOf("yazarEmail" to aktifKullanici.email))
                        .addOnSuccessListener {
                            basariliMi(true)
                        }.addOnFailureListener {
                            basariliMi(false)
                        }
                }
                .addOnFailureListener {
                    basariliMi(false)
                }
        } else {
            yazarlarCollection
                .document(aktifKullanici?.email!!)
                .collection("kutuphane")
                .document(hikaye.id!!)
                .delete()
                .addOnSuccessListener {
                    yazarlarCollection
                        .document(hikaye.yazarEmail!!)
                        .collection("hikayeleri")
                        .document(hikaye.id)
                        .collection("kutuphanesine_ekleyenler")
                        .document(aktifKullanici.email!!)
                        .delete()
                        .addOnSuccessListener {
                            basariliMi(true)
                        }.addOnFailureListener {
                            basariliMi(false)
                        }
                }
                .addOnFailureListener {
                    basariliMi(false)
                }
        }

    }

    fun hikayeYazarinKutuphanesindeMi(
        hikayeId: String,
        basariliMi: (Boolean,Hikaye?) -> Unit
    ) {
        yazarlarCollection
            .document(aktifKullanici?.email!!)
            .collection("kutuphane")
            .document(hikayeId)
            .get(Source.SERVER)
            .addOnSuccessListener {
                if (it.exists()) {
                    if (it["baslik"] == null) {
                        basariliMi(false,null)
                    }
                    basariliMi(true, it.toObject<Hikaye>())
                } else {
                    basariliMi(false, null)
                }
            }
            .addOnFailureListener {
                basariliMi(false, null)
            }
    }

    fun kutuphanedekiHikayeleriGetir(
        basariliMi: (Boolean,List<Hikaye>) -> Unit
    ) {
        yazarlarCollection
            .document(aktifKullanici?.email!!)
            .collection("kutuphane")
            .get(Source.SERVER)
            .addOnSuccessListener { veri->
                if (veri.isEmpty) {
                    basariliMi(false, emptyList())
                } else {
                    basariliMi(
                        true,
                        veri.documents.mapNotNull {
                            it.toObject<Hikaye>()
                        }
                    )
                }
            }
            .addOnFailureListener {
                basariliMi(false, emptyList())
            }
    }

    fun yazarHikayelerineKaydet(hikaye: Hikaye, kullaniciAdi: String, basariliMi: (Boolean) -> Unit) {
        val id = hikaye.id + kullaniciAdi.capitalize(Locale.ROOT)
        println(id)
        val veri = hashMapOf(
            "id" to hikaye.id,
            "yayinlandiMi" to hikaye.yayinlandiMi,
            "yazarEmail" to aktifKullanici?.email
        )
        yazarHikayeleriCollection
            .document(id)
            .set(veri)
            .addOnSuccessListener {
                basariliMi(true)
            }
            .addOnFailureListener {
                basariliMi(false)
            }
    }

    fun rastgeleHikayeleriGetir(basariliMi: (Boolean, List<Hikaye>, List<HikayeBolum>) -> Unit) {
        yazarHikayeleriCollection
            .get(Source.SERVER)
            .addOnSuccessListener {
                if (it.documents.isNotEmpty()) {
                    val liste = it.documents.mapNotNull { yazarlarColVeri->
                        if (yazarlarColVeri.getBoolean("yayinlandiMi") == true) {
                            yazarlarColVeri.toObject<Hikaye>()
                        } else {
                            null
                        }
                    }.shuffled().take(5)

                    val hikayeler = mutableListOf<Hikaye>()
                    val girisler = mutableListOf<HikayeBolum>()
                    for (hikaye in liste) {
                        yazarlarCollection
                            .document(hikaye.yazarEmail!!)
                            .collection("hikayeleri")
                            .document(hikaye.id!!)
                            .get(Source.SERVER)
                            .addOnSuccessListener {
                                val veri = it.toObject<Hikaye>()!!
                                girisBolumunuAl(veri) { basariDurumu, giris ->
                                    if (basariDurumu) {
                                        hikayeler.add(veri)
                                        girisler.add(giris)
                                        if (hikayeler.count() == liste.count()) {
                                            basariliMi(true, hikayeler, girisler)
                                        }
                                    }
                                }

                            }
                            .addOnFailureListener {
                                basariliMi(false, emptyList(), emptyList())
                            }
                    }

                } else {
                    basariliMi(false, emptyList(), emptyList())
                }
            }
            .addOnFailureListener {
                basariliMi(false, emptyList(), emptyList())
            }
    }

    private fun girisBolumunuAl(hikaye:Hikaye, basariliMi: (Boolean, HikayeBolum) -> Unit) {
            yazarlarCollection
                .document(hikaye.yazarEmail!!)
                .collection("hikayeleri")
                .document(hikaye.id!!)
                .collection("bolumler")
                .get(Source.SERVER)
                .addOnSuccessListener {
                    basariliMi(true, it.documents[0].toObject<HikayeBolum>()!!)
                }
                .addOnFailureListener {
                    basariliMi(false, HikayeBolum())
                }
    }



    private fun altKoleksiyonlariSil(collectionRef: List<CollectionReference>, basariliMi: (Boolean) -> Unit) {
        for (collection in collectionRef) {
            collection.get()
                .addOnSuccessListener {
                    for (document in it.documents) {
                        collection.document(document.id).delete()
                            .addOnSuccessListener {sonuc->
                                if (it.documents.indexOf(document) == it.documents.count() - 1) {
                                    basariliMi(true)
                                }
                            }
                            .addOnFailureListener {
                                basariliMi(false)
                                return@addOnFailureListener
                            }
                    }
                }
                .addOnFailureListener {
                    basariliMi(false)
                    return@addOnFailureListener
                }
        }
    }


}