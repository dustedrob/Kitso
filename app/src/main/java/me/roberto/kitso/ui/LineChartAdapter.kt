package me.roberto.kitso.ui

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import me.roberto.kitso.R
import me.roberto.kitso.model.HistoricData

class LineChartAdapter(val chart: LineChart){

    private val labels = mutableListOf<String>()
    private lateinit var dataList : List<HistoricData>
    private lateinit var dataSet: LineDataSet

    fun setData(historicData : List<HistoricData>){

        dataList = historicData
        val entries = mutableListOf<Entry>()

            for ((index, value) in historicData.withIndex()) {
                entries.add(Entry(index.toFloat(), value.close.toFloat()))
                labels.add(value.dated)
            }
            dataSet = LineDataSet(entries, "Value")

    }

    fun setLineChartVisual(context: Context) {
        dataSet.fillColor = ContextCompat.getColor(context, R.color.colorPrimary)
        dataSet.color = ContextCompat.getColor(context, R.color.colorPrimary)
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.highLightColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        dataSet.axisDependency = YAxis.AxisDependency.LEFT
        dataSet.lineWidth = 2f
        dataSet.setCircleColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
        dataSet.setDrawCircleHole(false)
        dataSet.setDrawValues(false)
        chart.axisRight.setDrawLabels(false)
        when (dataList.size) {
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
        chart.data = LineData(dataSet)
        chart.invalidate()
    }

    fun setVisibility(checkedId: Int) {

        when (checkedId) {
            R.id.candlestick_radio -> {
                chart.visibility = View.GONE
            }

            R.id.linear_radio -> {
                chart.visibility = View.VISIBLE
            }
        }

    }
}