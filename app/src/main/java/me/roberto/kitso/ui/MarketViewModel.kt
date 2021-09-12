package me.roberto.kitso.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.roberto.kitso.database.BookItemDao
import me.roberto.kitso.model.Book
import me.roberto.kitso.model.BookItem
import me.roberto.kitso.model.HistoricData
import me.roberto.kitso.model.OrderBook
import me.roberto.kitso.repository.KitsoRepository
import java.util.concurrent.TimeUnit

/**
 * Created by roberto on 6/07/17.
 */
class MarketViewModel (
        private val dataSource: BookItemDao,
        private var kitsoRepository: KitsoRepository
) : ViewModel() {
    val disposable = CompositeDisposable()
    lateinit var autoDisposable: Disposable
    var book: MutableLiveData<Book> = MutableLiveData()
    var chartData: MutableLiveData<List<HistoricData>> = MutableLiveData()
    var availableBooks: MutableLiveData<List<BookItem>> = MutableLiveData()
    var orderBook: MutableLiveData<OrderBook> = MutableLiveData()
    lateinit var currentCoin: String


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
        autoDisposable.dispose()
    }

    private val TAG: String = "market_model"

    fun updateBook(s: String) {
        currentCoin = s
        kitsoRepository.getBook(s).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe({ bookItem ->
                    book.value = bookItem
                           },
                    { t -> Log.e(TAG, "something went wrong: ${t.message}") })
                .also {
                    disposable.add(it)
                }

    }


    fun autoRefreshData(checked: Boolean) {
        if (checked) {
            autoDisposable = Observable.interval(10, TimeUnit.SECONDS, Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).retry().subscribe { updateBook(currentCoin) }
        } else {
            autoDisposable.dispose()
        }
    }


    fun updateBooks() {
        streamBooks()?.observeOn(AndroidSchedulers.mainThread())?.subscribe({ list ->
            availableBooks?.value = list
        },
                { error -> Log.e(TAG, "error: " + error.message); })


    }

    private fun streamBooks(): Observable<List<BookItem>> {

        val localBooks: Maybe<List<BookItem>> = dataSource.load().subscribeOn(Schedulers.newThread()).filter { list -> list.isNotEmpty() }
        val remoteBooks = kitsoRepository.getAvailableBooks().map {
            it.books
        }.subscribeOn(Schedulers.newThread())?.doOnNext { list ->
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

        return Observable.concat(localBooks.toObservable(), remoteBooks)
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
        kitsoRepository.getHistoricData(book, range)?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.newThread())
                ?.subscribe({ data -> chartData.value = data }, { t -> Log.e(TAG, "seomthing went wront: ${t.message}") })
                .also { disposable.addAll(it) }
    }

}