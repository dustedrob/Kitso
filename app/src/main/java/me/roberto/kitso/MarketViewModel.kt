package me.roberto.kitso

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by roberto on 6/07/17.
 */
class MarketViewModel : ViewModel()
{
    var book: MutableLiveData<Book>? =MutableLiveData()
    var chartData: MutableLiveData<List<HistoricData>>? = MutableLiveData()
    var availableBooks:MutableLiveData<List<BookItem>>?= MutableLiveData()
    var orderBook:MutableLiveData<OrderBook>?= MutableLiveData()
    var kitsoRepository: KitsoRepository?= KitsoRepository()
    lateinit var currentCoin:String
    lateinit var disposable:Disposable


    private val TAG: String?="market_model"

    fun updateBook(s: String)
    {

        currentCoin=s
        Log.i(TAG, "updating from remote: ")
        kitsoRepository?.getBook(s)?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.newThread())
                ?.subscribeWith(object : DisposableSingleObserver<Book>() {
            override fun onError(e: Throwable) {

                Log.e(TAG, "error : "+e.message )
            }

            override fun onSuccess(t: Book) {
                Log.i(TAG, "success:" )
                book?.value=t
            }

        })

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


    }



    fun autoRefreshData(checked: Boolean)
    {

        if (checked) {
             disposable = Observable.interval(10, TimeUnit.SECONDS, Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread()).retry().subscribe { updateBook(currentCoin); }
        }
        else
        {
            disposable.dispose()
        }
    }



    fun findAvailableBooks()
    {
        Log.i(TAG, "finding available books: ")
        kitsoRepository?.getAvailableBooks()?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.newThread())
                ?.doOnSuccess {

                    t->
                    Log.i(TAG, "books: ")
                    availableBooks?.value=t


                }
                ?.doOnError{e-> Log.e(TAG, "error: "+e.message );}
                ?.subscribe()
    }

    fun updateChartData(book:String,range:String)
    {
        kitsoRepository?.getHistoricData(book,range)?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.newThread())
                ?.subscribeWith(object : DisposableSingleObserver<List<HistoricData>>() {
                    override fun onError(e: Throwable) {

                        Log.e(TAG, "error : "+e.message )
                    }

                    override fun onSuccess(t: List<HistoricData>) {
                        Log.i(TAG, "success:" )
                        chartData?.value=t
                    }

                })
    }

}