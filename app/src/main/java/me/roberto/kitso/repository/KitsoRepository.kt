package me.roberto.kitso.repository

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import me.roberto.kitso.database.BookItemDao
import me.roberto.kitso.model.Book
import me.roberto.kitso.model.BookItem
import me.roberto.kitso.model.HistoricData
import me.roberto.kitso.network.KitsoService
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject


/**
 * Created by roberto on 20/07/17.
 */
class KitsoRepository @Inject constructor(val kitsoService: KitsoService, val datasource: BookItemDao){

    private val TAG: String = "KITSO_REPO"


    fun getHistoricData(bookSymbol: String, range: String): Single<List<HistoricData>>? {

        //this is unofficial and not part of the current public api so we skip using retrofit
        //and the rest of the repository
        val client = OkHttpClient()
        val request = Request.Builder().url("https://bitso.com/trade/chartJSON/$bookSymbol/$range").build()
        val type = Types.newParameterizedType(List::class.java, HistoricData::class.java)
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter<List<HistoricData>>(type)

        Log.i(TAG, "execute chart data: ")
        val single = Single.create(SingleOnSubscribe<List<HistoricData>> { e ->
            val json = client.newCall(request).execute().body()?.string()

            val fromJson: List<HistoricData>? = adapter.fromJson(json)
            if (fromJson != null)
                e.onSuccess(fromJson)
            else
                e.onError(Throwable("Fetching data failed"))
        })

        return single
    }

    fun getBook(bookSymbol: String): Single<Book> {
        val ticker = kitsoService.getTicker(bookSymbol)
        val single = Single.create(SingleOnSubscribe<Book> { e ->


            val payload = ticker.execute().body()?.payload

            if (payload != null) {
                e.onSuccess(payload)
            } else {
                e.onError(Exception("Book not found"))
            }
        }).retry(3)

        return single
    }


    fun getAvailableBooks(): Observable<List<BookItem>>? {


        return Observable.create(ObservableOnSubscribe<List<BookItem>> { e ->

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


        }).retry(3)

    }

}