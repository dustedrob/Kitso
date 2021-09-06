package me.roberto.kitso.ui


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.charts.LineChart
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_market.chart_view
import kotlinx.android.synthetic.main.fragment_market.my_toolbar
import kotlinx.android.synthetic.main.fragment_market.radioGroup
import kotlinx.android.synthetic.main.price_layout.createdAt
import kotlinx.android.synthetic.main.price_layout.max
import kotlinx.android.synthetic.main.price_layout.min
import kotlinx.android.synthetic.main.price_layout.precio
import kotlinx.android.synthetic.main.price_layout.vol
import me.roberto.kitso.R
import me.roberto.kitso.di.DaggerApplicationComponent
import me.roberto.kitso.di.KitsoApplication
import me.roberto.kitso.model.Book
import me.roberto.kitso.model.BookItem
import me.roberto.kitso.model.HistoricData
import me.roberto.kitso.utils.Utils.stringToDate
import javax.inject.Inject


class MarketFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val SELECTED_INDEX = "selected_index"
    private var selectedItem = -1
    private lateinit var spinner: Spinner
    private lateinit var progressBar: ProgressBar
    lateinit var lineChartAdapter: LineChartAdapter
    lateinit var candleChartAdapter: CandleStickChartAdapter
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels <MarketViewModel> {viewModelFactory}
    private lateinit var lineChart: LineChart
    private lateinit var candleChart:CandleStickChart
    val PREFS = "me.roberto.kitso.preferences"
    val PREFS_SELECTED_ITEM = "me.roberto.kitso.preferences.item"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val applicationContext = context?.applicationContext as KitsoApplication
        DaggerApplicationComponent.builder()
                .applicationContext(applicationContext)
                .build()
                .inject(this)

        selectedItem = savedInstanceState?.getInt(SELECTED_INDEX) ?: -1
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SELECTED_INDEX, selectedItem)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }


    fun updateCoin() {

        val index = activity?.findViewById<Spinner>(R.id.spinner)?.selectedItemPosition

        index?.let{

            if (selectedItem != index) {

                chart_view.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
            selectedItem = index
        }


        if (spinner.selectedItem != null) {
            val selectedItem = spinner.selectedItem as BookItem
            val range = "1month"
            viewModel.updateBook(selectedItem.book)
            viewModel.updateChartData(selectedItem.book, range)

        } else {
            refreshLayout?.isRefreshing = false
            Snackbar.make(my_toolbar, "Network Error", Snackbar.LENGTH_SHORT).show()
        }

    }


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        if (selectedItem != p2) {
            updateCoin()
        }
        activity?.getSharedPreferences(PREFS, 0)?.edit {
            putInt(PREFS_SELECTED_ITEM, p2)
            commit()
        }
    }


    companion object {
        val TAG = MarketFragment::class.java.simpleName
        fun newInstance() = MarketFragment()
    }

    private var refreshLayout: SwipeRefreshLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val inflate = inflater.inflate(R.layout.fragment_market, container, false)
        refreshLayout = inflate.findViewById(R.id.refresh)
        spinner = inflate.findViewById(R.id.spinner)
        progressBar = inflate.findViewById(R.id.progressBar)
        lineChart = inflate.findViewById(R.id.linear_chart)
        candleChart = inflate.findViewById(R.id.candle_chart)
        lineChartAdapter = LineChartAdapter(lineChart)
        candleChartAdapter = CandleStickChartAdapter(candleChart)
        spinner = inflate.findViewById(R.id.spinner)
        progressBar = inflate.findViewById(R.id.progressBar)

        if (savedInstanceState != null) {
            spinner.setSelection(savedInstanceState.getInt(SELECTED_INDEX))
            progressBar.visibility = View.VISIBLE

        }

        refreshLayout?.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark))
        refreshLayout?.setOnRefreshListener {
            updateCoin()
        }


        val toolbar : Toolbar = inflate.findViewById(R.id.my_toolbar)
        toolbar.inflateMenu(R.menu.menu_market)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_settings -> {
                    updateCoin()
                }
                R.id.action_auto_refresh -> {
                    it.isChecked = !it.isChecked
                    viewModel.autoRefreshData(it.isChecked)
                }
            }
            true
        }

        val radioGroup = inflate.findViewById<RadioGroup>(R.id.radioGroup)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            lineChartAdapter.setVisibility(checkedId)
            candleChartAdapter.setVisibility(checkedId)
        }

        return inflate
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            spinner.setSelection(savedInstanceState.getInt(SELECTED_INDEX))
            progressBar.visibility = View.VISIBLE
        }
        viewModel.availableBooks?.observe(viewLifecycleOwner, bookObserver)
        viewModel.book?.observe(viewLifecycleOwner, tickerObserver)
        viewModel.chartData?.observe(viewLifecycleOwner, chartDataObserver)
        viewModel.updateBooks()
    }


    class BookAdapter(context: Context, resource: Int, items: MutableList<BookItem>) : ArrayAdapter<BookItem>(context, resource, items)


    private val bookObserver: Observer<List<BookItem>> = Observer { bookItems ->

        val adapter = context?.let { BookAdapter(it, R.layout.spinner_item, bookItems as MutableList<BookItem>) }
        adapter?.setDropDownViewResource(R.layout.spinner_item)
        spinner.adapter = adapter

        val savedCoin = requireActivity().getSharedPreferences(PREFS, 0).getInt(PREFS_SELECTED_ITEM, -1)
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


    private val chartDataObserver = Observer<List<HistoricData>> { dataList ->


        dataList?.let {
            lineChartAdapter.setData(it)
            candleChartAdapter.setData(it)


            context?.let { context ->
                lineChartAdapter.setLineChartVisual(context)
                candleChartAdapter.setCandleSticktVisual(context)
            }
        }
            lineChartAdapter.setVisibility(radioGroup.checkedRadioButtonId)
            candleChartAdapter.setVisibility(radioGroup.checkedRadioButtonId)

            progressBar.visibility = View.GONE
            chart_view.visibility = View.VISIBLE
    }

}
