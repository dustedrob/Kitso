package me.roberto.kitso.ui


import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_market.*
import kotlinx.android.synthetic.main.price_layout.*
import me.roberto.kitso.Book
import me.roberto.kitso.BookItem
import me.roberto.kitso.HistoricData
import me.roberto.kitso.R
import me.roberto.kitso.database.Injection
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MarketFragment : Fragment(), AdapterView.OnItemSelectedListener {

    val PREFS = "me.roberto.kitso.preferences"
    val PREFS_SELECTED_ITEM = "me.roberto.kitso.preferences.item"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectedItem = savedInstanceState?.getInt(SELECTED_INDEX) ?: -1


    }

    private val SELECTED_INDEX = "selected_index"
    private var selectedItem = -1

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SELECTED_INDEX, selectedItem)
    }


    private lateinit var viewModel: MarketViewModel
    private lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }


    fun updateCoin() {

        val selectedIndex = activity?.findViewById<Spinner>(R.id.spinner)?.selectedItemPosition
        if (selectedItem != selectedIndex) {

            chart_view.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
        selectedItem = selectedIndex!!

        if (spinner.selectedItem != null) {
            val selectedItem = spinner.selectedItem as BookItem
            val range = "1month"


            viewModel.updateBook(selectedItem.book!!)
            viewModel.updateChartData(selectedItem.book, range)

        } else {
            refreshLayout?.isRefreshing = false
            Snackbar.make(my_toolbar, "Network Error", Snackbar.LENGTH_SHORT).show()
        }

    }


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        if (selectedItem != p2) {
            Log.i(TAG, "updating coin: ")
            updateCoin()
        }
        activity?.getSharedPreferences(PREFS, 0)?.edit()?.putInt(PREFS_SELECTED_ITEM, p2)?.commit()


    }


    companion object {
        val TAG = MarketFragment::class.java.simpleName!!
        fun newInstance() = MarketFragment()
    }

    private var refreshLayout: SwipeRefreshLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val inflate = inflater.inflate(R.layout.fragment_market, container, false)
        refreshLayout = inflate.findViewById(R.id.refresh)

        refreshLayout?.setColorSchemeColors(ContextCompat.getColor(context!!, R.color.colorPrimaryDark))
        refreshLayout?.setOnRefreshListener {
            updateCoin()
        }

        return inflate
    }


    class BookAdapter(context: Context?, val resource: Int, val items: MutableList<BookItem>?) : ArrayAdapter<BookItem>(context, resource, items)


    private val bookObserver: Observer<List<BookItem>> = Observer { bookItems ->


        val adapter = BookAdapter(context, R.layout.spinner_item, bookItems as MutableList<BookItem>?)
        adapter.setDropDownViewResource(R.layout.spinner_item)
        spinner.adapter = adapter

        val savedCoin = activity!!.getSharedPreferences(PREFS, 0).getInt(PREFS_SELECTED_ITEM, -1)
        if (savedCoin > -1) {
            spinner.setSelection(savedCoin)
        }
        spinner.onItemSelectedListener = this


    }


    private val tickerObserver: Observer<Book> = Observer { book ->
        precio.text = book?.last
        vol.text = book?.volume
        //variation.text=book?.vwap
        max.text = book?.high
        min.text = book?.low
        createdAt.text = stringToDate(book?.created_at)


        refreshLayout?.isRefreshing = false
    }


    fun setLineChartVisual(dataSet: LineDataSet, dataList: List<HistoricData>?, labels: MutableList<String>) {
        dataSet.fillColor = ContextCompat.getColor(activity!!, R.color.colorPrimary)
        dataSet.color = ContextCompat.getColor(activity!!, R.color.colorPrimary)
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.highLightColor = ContextCompat.getColor(activity!!, R.color.colorPrimaryDark)
        dataSet.axisDependency = YAxis.AxisDependency.LEFT
        dataSet.lineWidth = 2f
        dataSet.setCircleColor(ContextCompat.getColor(activity!!, R.color.colorPrimaryDark))
        dataSet.setDrawCircleHole(false)
        dataSet.setDrawValues(false)
        linear_chart.axisRight.setDrawLabels(false)
        when (dataList?.size) {
            in 0..10 -> linear_chart.xAxis.granularity = 2f
            in 11..20 -> linear_chart.xAxis.granularity = 4f
            in 21..31 -> linear_chart.xAxis.granularity = 7f

        }

        linear_chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        linear_chart.xAxis.setDrawGridLines(false)
        val description = Description()
        description.isEnabled = false
        linear_chart.description = description
        val legend = linear_chart.legend
        legend.isEnabled = false


        val formatter = object : IAxisValueFormatter {
            override fun getFormattedValue(value: Float, axis: AxisBase?): String? {

                if (value.toInt() <= labels.size) {
                    return labels[value.toInt()]
                }

                return " "

            }

        }

        linear_chart.xAxis.valueFormatter = formatter
        linear_chart.data = LineData(dataSet)
        linear_chart.invalidate()


    }


    fun setCandleSticktVisual(dataSet: CandleDataSet, dataList: List<HistoricData>?, labels: MutableList<String>) {

        dataSet.shadowWidth = 1.8f
        dataSet.decreasingColor = Color.argb(200, 158, 109, 131)
        dataSet.shadowColorSameAsCandle = true
        dataSet.decreasingPaintStyle = Paint.Style.FILL_AND_STROKE
        dataSet.increasingColor = ContextCompat.getColor(activity!!, R.color.colorPrimary)
        dataSet.increasingPaintStyle = Paint.Style.STROKE
        dataSet.neutralColor = Color.BLUE
        dataSet.valueTextColor = Color.RED

        dataSet.axisDependency = YAxis.AxisDependency.LEFT

        dataSet.setDrawValues(false)
        candle_chart.axisRight.setDrawLabels(false)
        when (dataList?.size) {
            in 0..10 -> candle_chart.xAxis.granularity = 2f
            in 11..20 -> candle_chart.xAxis.granularity = 4f
            in 21..31 -> candle_chart.xAxis.granularity = 7f

        }

        candle_chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        candle_chart.xAxis.setDrawGridLines(false)
        val description = Description()
        description.isEnabled = false
        candle_chart.description = description
        val legend = candle_chart.legend
        legend.isEnabled = false


        val formatter = object : IAxisValueFormatter {
            override fun getFormattedValue(value: Float, axis: AxisBase?): String? {

                if (value.toInt() <= labels.size) {
                    return labels[value.toInt()]
                }

                return " "

            }

        }

        candle_chart.xAxis.valueFormatter = formatter
        candle_chart.data = CandleData(dataSet)
        candle_chart.invalidate()


    }

    private val chartDataObserver = Observer<List<HistoricData>> { dataList ->


        if (dataList != null) {


            val lineEntries = ArrayList<Entry>()
            val candleEntries = ArrayList<CandleEntry>()
            val labels = ArrayList<String>()

            for ((index, value) in dataList.withIndex()) {

                lineEntries.add(Entry(index.toFloat(), value.close.toFloat()))
                candleEntries.add(CandleEntry(index.toFloat(), value.high.toFloat(), value.low.toFloat(), value.open.toFloat(), value.close.toFloat()))
                labels.add(value.dated)
            }
            val dataSet = LineDataSet(lineEntries, "Value")
            val candleDataSet = CandleDataSet(candleEntries, "Value")
            setLineChartVisual(dataSet, dataList, labels)
            setCandleSticktVisual(candleDataSet, dataList, labels)

            when (radioGroup.checkedRadioButtonId) {
                R.id.candlestick_radio -> {


                    linear_chart.visibility = View.GONE
                    candle_chart.visibility = View.VISIBLE

                }

                R.id.linear_radio -> {

                    linear_chart.visibility = View.VISIBLE
                    candle_chart.visibility = View.GONE

                }
            }

            progressBar.visibility = View.GONE
            chart_view.visibility = View.VISIBLE


        }

    }


    fun stringToDate(date: String?): String? {


        val tz = TimeZone.getTimeZone("UTC")
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        df.timeZone = tz
        try {
            val parse = df.parse(date)
            val readableFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            return readableFormat.format(parse)

        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }


    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModelFactory = Injection.provideViewModelFactory(activity!!)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MarketViewModel::class.java)

        viewModel.availableBooks?.observe(this, bookObserver)
        viewModel.book?.observe(this, tickerObserver)
        viewModel.chartData?.observe(this, chartDataObserver)

        viewModel.updateBooks()


        if (savedInstanceState != null) {
            spinner.setSelection(savedInstanceState.getInt(SELECTED_INDEX))
            progressBar.visibility = View.VISIBLE

        }

        my_toolbar.inflateMenu(R.menu.menu_market)
        my_toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_settings -> {
                    Log.i(TAG, "click on update")
                    updateCoin()
                }
                R.id.action_auto_refresh -> {
                    Log.i(TAG, "click on auto refresh")
                    it.isChecked = !it.isChecked
                    viewModel.autoRefreshData(it.isChecked)
                }
            }
            true
        }


//        refresh_switch.setOnCheckedChangeListener { _, isChecked -> viewModel.autoRefreshData(isChecked) }


        radioGroup.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                R.id.linear_radio -> {


                    candle_chart.visibility = View.GONE
                    linear_chart.visibility = View.VISIBLE
                }

                R.id.candlestick_radio -> {
                    candle_chart.visibility = View.VISIBLE
                    linear_chart.visibility = View.GONE
                }
            }
        }
    }

}
