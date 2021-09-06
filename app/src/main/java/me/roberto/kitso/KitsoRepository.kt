package me.roberto.kitso

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import me.roberto.kitso.database.Injection
import me.roberto.kitso.model.Book
import me.roberto.kitso.model.BookItem
import me.roberto.kitso.model.HistoricData
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


/**
 * Created by roberto on 20/07/17.
 */
class KitsoRepository {
    private val kitsoService: KitsoService = Injection.provideKitsoService()

    private val TAG: String = "KITSO_REPO"

    fun getHistoricData(bookSymbol: String, range: String): Single<List<HistoricData>>? = Single.create { e ->


        //this is unofficial and not part of the current public api so we skip using retrofit
        //and the rest of the repository
        val client = OkHttpClient()
        val request = Request.Builder().url("https://bitso.com/trade/chartJSON/$bookSymbol/$range").build()
        val type = Types.newParameterizedType(List::class.java, HistoricData::class.java)
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter<List<HistoricData>>(type)

        val json = client.newCall(request).execute().body()?.string()

        val fromJson: List<HistoricData>? = adapter.fromJson(json)
        if (fromJson != null)
            e.onSuccess(fromJson)
        else
            e.onError(Throwable("Fetching data failed"))
    }


    fun getBook(bookSymbol: String): Single<Book> = Single.create<Book> { e ->

        val ticker = kitsoService.getTicker(bookSymbol)
        val payload = ticker.execute().body()?.payload

        if (payload != null) {
            Log.i(TAG, "sucess payload : " + payload.book)
            e.onSuccess(payload)
        } else {
            e.onError(Exception("Book not found"))
        }
    }.retry(3)


    fun getAvailableBooks(): Observable<List<BookItem>> = Observable.create<List<BookItem>> { e ->

        val availableBooks = kitsoService.getAvailableBooks()
        val response = availableBooks.execute()

        if (response.isSuccessful) {

            val payload = response?.body()?.payload
            if (payload != null) {
                e.onNext(payload)
                e.onComplete()
            } else {
                e.onError(Exception("Error"))
            }

        } else {
            e.onError(Exception("Error"))
        }


    }.retry(3)

}