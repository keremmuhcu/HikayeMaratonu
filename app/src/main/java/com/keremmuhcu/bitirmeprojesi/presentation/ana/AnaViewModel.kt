package com.keremmuhcu.bitirmeprojesi.presentation.ana

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.keremmuhcu.bitirmeprojesi.data.FirebaseRepository
import com.keremmuhcu.bitirmeprojesi.data.models.KullaniciYarismaHikayesi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

class AnaViewModel:ViewModel() {
    private val _anaState = MutableStateFlow(AnaState())
    val anaState:StateFlow<AnaState> = _anaState
    private val firebaseRepository = FirebaseRepository()

    fun setToastMesaj(mesaj:String) {
        _anaState.value = _anaState.value.copy(toastMesaj = mesaj)
    }

    fun setInternetVarMi(durum: Boolean) {
        _anaState.value = _anaState.value.copy(internetVarMi = durum)
    }

    fun ekle() {
        /*
        FirebaseRepository sınıfında tarihi değiştir.
        val tarih'i değiştir.
        calender'deki tarihi de değiştir.
         */
        val tarih = "02tem2024"
        val calendar = Calendar.getInstance()

        val ahmet = KullaniciYarismaHikayesi(
            "Kırık Zihinler, Kırık Hayatlar",
            "Günler haftalara, haftalar aylara dönüşürken Muhsin, hastanenin karanlık dehlizlerinde kaybolmuş gibiydi. Her hasta, ona yeni bir acı, yeni bir travma sunuyordu. Kendi zihni de bu ağırlığın altında ezilmeye başlamıştı. Rüyalarında hastalarının çığlıklarını duyuyor, uyanıkken onların hayallerini görüyordu.\n" +
                    "\n" +
                    "Bir gün, Muhsin, hastanenin en tehlikeli koğuşunda dolaşırken, bir hasta tarafından saldırıya uğradı. Saldırgan, yıllar önce işlediği bir cinayetin suçluluğuyla boğuşan bir adamdı. Muhsin, adamın gözlerindeki deliliği gördüğünde, kendi akıl sağlığından şüphe etmeye başlamıştı.\n" +
                    "\n" +
                    "Bu olay, Muhsin'in hayatında bir dönüm noktası oldu. Hastaneden istifa etti ve bir daha asla psikiyatriye dönmedi. Kırık zihinlerin arasında kaybolmuş kendi zihnini onarmak için uzun bir yolculuğa çıktı. Belki bir gün, iyileşebilir ve yeniden insanlara yardım edebilirdi. Ancak o gün gelene kadar, Muhsin, kendi karanlık dehlizlerinde kaybolmaya devam edecekti.",
            "ahmet",
            "ahmet@gmail.com",
            "bekliyor",
            0
        )

        val can = KullaniciYarismaHikayesi(
            "Umut Işığı",
            "Muhsin, her geçen gün hastalarının yaşadığı acıları daha derinden hissediyordu. Onların kırık zihinlerini onarmak için çabalarken, kendi içinde de bir değişim başlamıştı. Hastalarının hikayeleri, ona insan ruhunun karmaşıklığını ve dayanıklılığını öğretmişti.\n" +
                    "\n" +
                    "Bir gün, Muhsin, genç bir kadın hastayla tanıştı. Kadın, yıllarca süren depresyonla mücadele ediyordu. Muhsin, kadının içindeki umut kıvılcımını gördü ve onu hayata bağlamak için elinden geleni yaptı. Uzun süren terapiler sonucunda, kadın yavaş yavaş iyileşmeye başladı.\n" +
                    "\n" +
                    "Kadının değişimi, Muhsin'e büyük bir ilham kaynağı oldu. Hastalarına yardım etmenin, onların hayatlarına umut ışığı olabileceğini anlamıştı. Muhsin, akıl hastanesindeki görevine daha bir şevkle sarıldı. Kırık zihinleri onarmak için yeni yöntemler denedi, hastalarıyla daha güçlü bağlar kurdu. Muhsin, akıl hastalığının karanlık dünyasında bir umut ışığı olmuştu.",
            "can",
            "can@mail.com",
            "bekliyor",
            0
        )

        val firat = KullaniciYarismaHikayesi(
            "Karanlık Labirent",
            "Muhsin, hastalarının zihinlerinde dolaştıkça, kendi akıl sağlığından şüphe etmeye başlamıştı. Gerçeklik ile hayal arasındaki çizgi giderek bulanıklaşıyordu. Hastanenin koridorları, ona sonsuz bir labirent gibi görünüyordu. Çıkış yolu yoktu, sadece deliliğe giden yollar vardı.\n" +
                    "\n" +
                    "Bir gece, Muhsin, hastanede yalnız başına kalmıştı. Karanlık koridorlarda dolaşırken, bir ses duydu. Ses, onu hastanenin bodrum katına çağırıyordu. Muhsin, korkuyla karışık bir merakla bodrum katına indi. Orada, yıllar önce kapatılan bir deney odasını buldu.\n" +
                    "\n" +
                    "Odanın içinde, eski tıbbi aletler ve dosyalar vardı. Muhsin, dosyaları karıştırırken, hastanenin karanlık geçmişiyle ilgili korkunç gerçekleri ortaya çıkardı. Hastane, yıllar önce etik dışı deneylere ev sahipliği yapmıştı. Muhsin, bu gerçekleri öğrendiğinde dehşete düştü. Kendini bir anda bu karanlık labirentin içinde hapsolmuş hissetti. Çıkış yolu yoktu, sadece delilik vardı",
            "firat",
            "fdfdgfg@gmail.com",
            "bekliyor",
            0
        )

        val kerem = KullaniciYarismaHikayesi(
            "İyileşmeyen Yaralar",
            "Muhsin, hastalarıyla kurduğu bağlar derinleştikçe, onların acılarını kendi içinde taşımaya başlamıştı. Her bir hastanın hikayesi, Muhsin'in ruhunda derin yaralar açıyordu. Geçmişte yaşadığı travmalar da bu yaraları daha da derinleştiriyordu.\n" +
                    "\n" +
                    "Bir gün, Muhsin, çocukluk travmalarıyla boğuşan bir hastayla tanıştı. Hastanın yaşadığı acılar, Muhsin'in kendi çocukluk anılarını tetikledi. Muhsin, bastırmaya çalıştığı duygularıyla yüzleşmek zorunda kaldı.\n" +
                    "\n" +
                    "Bu yüzleşme, Muhsin'in akıl sağlığını daha da kötüleştirdi. Hastalarına yardım etmek isterken, kendi yaralarını daha da derinleştiriyordu. Muhsin, iyileşmeyen yaralarının acısıyla baş edemez hale geldi. Hastaneden ayrıldı ve kendini alkole verdi. Bir zamanlar idealist bir doktor olan Muhsin, şimdi kendi karanlığında kaybolmuştu.",
            "kerem",
            "kerem@mail.com",
            "onaylandi",
            0
        )

        val mehmet = KullaniciYarismaHikayesi(
            "Çığlıklar Arasında Bir Fısıltı",
            "Hastanenin soğuk duvarları, Muhsin'in yalnızlığını daha da belirginleştiriyordu. Bir gece yarısı nöbetinde, hastaların çığlıkları arasında bir fısıltı duydu. Ses, onu terk edilmiş bir odaya çekiyordu. Odaya girdiğinde, tozlu bir piyanonun başında oturan yaşlı bir kadın gördü.\n" +
                    "\n" +
                    "Kadın, Muhsin'i görünce gülümsedi ve piyanoyu çalmaya başladı. Muhsin, hayatında hiç duymadığı kadar güzel bir melodiyle büyülenmişti. Kadın, çalarken ona hikayesini anlattı. Yıllar önce ünlü bir piyanist olan kadın, bir kaza sonucu aklını kaybetmiş ve hastaneye kapatılmıştı.\n" +
                    "\n" +
                    "Muhsin, kadının hikayesinden çok etkilendi. Onun içindeki müziği yeniden canlandırmak için elinden geleni yaptı. Kadın, Muhsin'in yardımıyla yavaş yavaş iyileşmeye başladı. Bir süre sonra, hastanede bir konser verdi. Muhsin, o gece kadının gözlerindeki mutluluğu gördüğünde, akıl hastalarına yardım etmenin sadece bir iş değil, aynı zamanda bir sanat olduğunu anlamıştı.",
            "mehmet",
            "m@m.com",
            "bekliyor",
            0
        )

        val veli = KullaniciYarismaHikayesi(
            "Aynadaki Yabancı",
            "Muhsin, her geçen gün hastalarına daha çok benzemeye başladığını fark ediyordu. Onların takıntıları, korkuları ve sanrıları, Muhsin'in zihnine de sıçramaya başlamıştı. Aynaya baktığında, tanıdık bir yabancı görüyordu.\n" +
                    "\n" +
                    "Bir sabah, Muhsin uyandığında, odasının duvarlarında garip semboller çizili olduğunu gördü. Semboller, ona bir mesaj vermeye çalışıyor gibiydi. Muhsin, bu sembollerin anlamını çözmeye çalıştıkça, gerçeklikten daha da uzaklaşıyordu.\n" +
                    "\n" +
                    "Hastanenin diğer doktorları, Muhsin'in davranışlarındaki değişikliği fark etmişlerdi. Onu muayene ettiler ve şizofreni teşhisi koydular. Muhsin, bir zamanlar tedavi etmeye çalıştığı hastalardan biri haline gelmişti. Aynadaki yabancı, artık Muhsin'in ta kendisiydi.",
            "veli",
            "veli@mail.com",
            "bekliyor",
            0
        )

        val yusuf = KullaniciYarismaHikayesi(
            "Kurtuluş Yolu",
            "Muhsin, akıl hastanesinde geçirdiği her gün, kendi akıl sağlığından daha da şüphe duymaya başlamıştı. Hastalarının hikayeleri, onun iç dünyasında derin yaralar açarken, aynı zamanda ona farklı bir bakış açısı da kazandırmıştı. Akıl hastalığının sadece bir hastalık olmadığını, aynı zamanda bir varoluş biçimi olabileceğini düşünmeye başlamıştı.\n" +
                    "\n" +
                    "Bir gün, Muhsin, hastanede düzenlenen bir terapi seansına katıldı. Seans sırasında, hastalardan biri Muhsin'e hayatının anlamını sorgulayan bir soru yöneltti. Muhsin, bu soru karşısında derin düşüncelere daldı. Kendi hayatını, mesleğini ve değerlerini sorgulamaya başladı.\n" +
                    "\n" +
                    "Bu sorgulama süreci, Muhsin'in hayatında bir dönüm noktası oldu. Akıl hastanesindeki görevinden ayrılmaya ve kendi iç yolculuğuna çıkmaya karar verdi. Hastanedeki deneyimleri, ona insan ruhunun karmaşıklığını ve güzelliğini öğretmişti. Muhsin, artık bu deneyimleri kendi hayatına anlam katmak için kullanacaktı. Akıl hastanesinden ayrılırken, Muhsin'in yüzünde bir huzur ifadesi vardı. Kendini hapsettiği düşüncelerden kurtulmuş, yeni bir başlangıç yapmaya hazırdı.",
            "yusufislam",
            "yusuf@gmail.com",
            "bekliyor",
            0
        )

        val liste = listOf(
            ahmet,can,firat,kerem
        )

        val tarihListesi = listOf(
            listOf(2,19),
            listOf(2,20),
            listOf(2,22),
            listOf(3,1),
        )

        var tarihIndex = 0
        for (hikaye in liste) {
            calendar.set(2024, Calendar.JULY, tarihListesi[tarihIndex][0], tarihListesi[tarihIndex][1], 45, 0)
            val veri = hashMapOf(
                "kullaniciYarismaHikayeBaslik" to hikaye.kullaniciYarismaHikayeBaslik,
                "kullaniciYarismaHikayesi" to hikaye.kullaniciYarismaHikayesi,
                "yazarKullaniciAdi" to hikaye.yazarKullaniciAdi,
                "yazarEmail" to hikaye.yazarEmail,
                "kabulDurumu" to hikaye.kabulDurumu,
                "oylar" to 0,
                "olusturmaTarihi" to Timestamp(calendar.time)
            )

            val collection = if (hikaye.yazarKullaniciAdi == "kerem") {
                "kullaniciYarismaHikayeleri"
            } else {
                "bekleyenler"
            }

            firebaseRepository.yarismaHikayeleriCollection
                .document(tarih)
                .collection(collection)
                .document(hikaye.yazarEmail!!)
                .set(veri)
                .addOnSuccessListener {

                }
                .addOnFailureListener {
                }

            tarihIndex++

        }


    }

    fun tarihleriAyarla() {
        val calendar = Calendar.getInstance()
        firebaseRepository.yazarlarCollection.get()
            .addOnSuccessListener {yazarlar->
                for (yazar in yazarlar) {
                    firebaseRepository.yazarlarCollection.document(yazar["email"] as String)
                        .collection("yazarinYarismaHikayeleri")
                        .get()
                        .addOnSuccessListener {hikayeler->
                            for (hikaye in hikayeler) {
                                when(hikaye.id.toString()) {
                                    "01tem2024" -> calendar.set(2024, Calendar.JULY, 1,12,4,59)
                                    "02nis2024" -> calendar.set(2024, Calendar.APRIL, 2,4,6,13)
                                    "07may2024" -> calendar.set(2024, Calendar.MAY, 7,3,0,0)
                                    "10haz2024" -> calendar.set(2024, Calendar.JUNE, 10,6,57,3)
                                    "17haz2024" -> calendar.set(2024, Calendar.JUNE, 17,5,19,29)
                                    "19may2024" -> calendar.set(2024, Calendar.MAY, 19,4,22,13)
                                    "24haz2024" -> calendar.set(2024, Calendar.JUNE, 24,4,39,18)
                                    "26nis2024" -> calendar.set(2024, Calendar.APRIL, 26,12,46,6)
                                }
                                firebaseRepository.yazarlarCollection.document(yazar["email"] as String)
                                    .collection("yazarinYarismaHikayeleri")
                                    .document(hikaye.id)
                                    .update("yarismaTarihi", calendar.time)
                                    .addOnSuccessListener {

                                    }
                            }
                        }
                }
            }
    }

}