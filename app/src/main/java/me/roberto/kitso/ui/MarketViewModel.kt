package me.roberto.kitso.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import me.roberto.kitso.*
import me.roberto.kitso.database.BookItemDao
import java.util.concurrent.TimeUnit

/**
 * Created by roberto on 6/07/17.
 */
class MarketViewModel(private val dataSource: BookItemDao) : ViewModel() {
    var book: MutableLiveData<Book>? = MutableLiveData()
    var chartData: MutableLiveData<List<HistoricData>>? = MutableLiveData()
    var availableBooks: MutableLiveData<List<BookItem>>? = MutableLiveData()
    var orderBook: MutableLiveData<OrderBook>? = MutableLiveData()
    var kitsoRepository: KitsoRepository? = KitsoRepository()
    lateinit var currentCoin: String
    lateinit var disposable: Disposable


    private val TAG: String? = "market_model"

    fun updateBook(s: String) {

        currentCoin = s
        kitsoRepository?.getBook(s)?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.newThread())
                ?.subscribe({bookItem-> book?.value = bookItem},{t-> Log.e(TAG, "something went wrong: ${t.message}" )})


    }

//        kitsoRepository?.getOrderBook(s)?.observeOn(AndroidSchedulers.mainThread())
//                ?.subscribeOn(Schedulers.newThread())?.subscribeWith(object: DisposableObserver<OrderBook>()
//        {
//            override fun onError(e: Throwable) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onComplete() {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onNext(t: OrderBook) {
//                orderBook?.value=t
//            }
//
//        })


    fun autoRefreshData(checked: Boolean) {

        if (checked) {
            disposable = Observable.interval(10, TimeUnit.SECONDS, Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).retry().subscribe { updateBook(currentCoin) }
        } else {
            disposable.dispose()
        }
    }


    fun updateBooks() {


        streamBooks()?.observeOn(AndroidSchedulers.mainThread())?.subscribe({list->
            availableBooks?.value = list},
                {error-> Log.e(TAG, "error: "+error.message );})


    }

    private fun streamBooks(): Observable<List<BookItem>>? {

        val localBooks: Maybe<List<BookItem>>? = dataSource.load().subscribeOn(Schedulers.newThread()).filter { list -> !list.isEmpty() }

        val remoteBooks=kitsoRepository?.getAvailableBooks()?.subscribeOn(Schedulers.newThread())?.doOnNext{
            list->
            list.forEach {
            dataSource.save(it)
            }
        }

        /**
         * Load list from api layer and save it to DB.
         */
//        val remoteBooks = kitsoRepository?.getAvailableBooks()?.map { list ->
//
//            Observable.create(ObservableOnSubscribe<List<BookItem>> { subscriber ->
//
//                list.forEach { dataSource.save(it) }
//
//                subscriber.onComplete()
//            }).subscribeOn(Schedulers.newThread()).subscribe()
//
//            list
//        }
//                ?.subscribeOn(Schedulers.io())


        return Observable.concat(localBooks?.toObservable(), remoteBooks)
    }


    /**
     *
     * .doOnSuccess {

    t->
    Log.i(TAG, "books: ")
    availableBooks?.value=t

    }?.flatMap {bookList->Single.create(SingleOnSubscribe<List<BookItem>> {bookList.forEach { book->dataSource.save(book) } }) }
     */


    fun updateChartData(book: String, range: String) {
        kitsoRepository?.getHistoricData(book, range)?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.newThread())
                ?.subscribe({data-> chartData?.value = data},{t->Log.e(TAG, "seomthing went wront: ${t.message}" )})
    }

}