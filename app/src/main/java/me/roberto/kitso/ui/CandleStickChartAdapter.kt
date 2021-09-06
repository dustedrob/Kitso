package me.roberto.kitso.ui

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import me.roberto.kitso.R
import me.roberto.kitso.model.HistoricData

class CandleStickChartAdapter(val chart: CandleStickChart){

    private val labels = mutableListOf<String>()
    private lateinit var dataList : List<HistoricData>
    private lateinit var dataSet: CandleDataSet


    fun setData(historicData : List<HistoricData>){
        dataList = historicData
        val entries = mutableListOf<CandleEntry>()
        for ((index, value) in historicData.withIndex()) {
            entries.add(CandleEntry(index.toFloat(), value.high.toFloat(), value.low.toFloat(), value.open.toFloat(), value.close.toFloat()))
            labels.add(value.dated)
        }
        dataSet = CandleDataSet(entries, "Value")

    }


    fun setCandleSticktVisual(context: Context) {

        dataSet.shadowWidth = 1.8f
        dataSet.decreasingColor = Color.argb(200, 158, 109, 131)
        dataSet.shadowColorSameAsCandle = true
        dataSet.decreasingPaintStyle = Paint.Style.FILL_AND_STROKE
        dataSet.increasingColor = ContextCompat.getColor(context, R.color.colorPrimary)
        dataSet.increasingPaintStyle = Paint.Style.STROKE
        dataSet.neutralColor = Color.BLUE
        dataSet.valueTextColor = Color.RED

        dataSet.axisDependency = YAxis.AxisDependency.LEFT

        dataSet.setDrawValues(false)
        chart.axisRight.setDrawLabels(false)
        when (dataList?.size) {
            in 0..10 -> chart.xAxis.granularity = 2f
            in 11..20 -> chart.xAxis.granularity = 4f
            in 21..31 -> chart.xAxis.granularity = 7f

        }

        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.setDrawGridLines(false)
        val description = Description()
        description.isEnabled = false
        chart.description = description
        val legend = chart.legend
        legend.isEnabled = false


        val formatter = object : IAxisValueFormatter {
            override fun getFormattedValue(value: Float, axis: AxisBase?): String? {

                if (value.toInt() <= labels.size) {
                    return labels[value.toInt()]
                }

                return " "

            }

        }

        chart.xAxis.valueFormatter = formatter
        chart.data = CandleData(dataSet)
        chart.invalidate()


    }

    fun setVisibility(checkedId: Int) {

        when (checkedId) {
            R.id.candlestick_radio -> {
                chart.visibility = View.VISIBLE
            }

            R.id.linear_radio -> {
                chart.visibility = View.GONE
            }
        }

    }


}