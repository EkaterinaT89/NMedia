package ru.netology.nmedia

import android.view.View
import ru.netology.nmedia.dto.Post
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.round
import kotlin.math.roundToInt

object PostService {

    fun countPresents(counter: Long): String {
        return when(counter) {
            in 0..999 -> "${counter}"

            in 1000..1099 -> "1K"
            in 1100..1999 -> counterPrinter(counter.toDouble()) + "K"

            in 2000..2099 -> "2K"
            in 2100..2999 -> counterPrinter(counter.toDouble()) + "K"

            in 3000..3099 -> "3K"
            in 3100..3999 -> counterPrinter(counter.toDouble()) + "K"

            in 4000..4099 -> "4K"
            in 4100..4999 -> counterPrinter(counter.toDouble()) + "K"

            in 5000..5099 -> "5K"
            in 5100..5999 -> counterPrinter(counter.toDouble()) + "K"

            in 6000..6099 -> "6K"
            in 6100..6999 -> counterPrinter(counter.toDouble()) + "K"

            in 7000..7099 -> "7K"
            in 7100..7999 -> counterPrinter(counter.toDouble()) + "K"

            in 8000..8099 -> "8K"
            in 8100..8999 -> counterPrinter(counter.toDouble()) + "K"

            in 9000..9099 -> "9K"
            in 9000..9999 -> counterPrinter(counter.toDouble()) + "K"


            10000.toLong() -> "10K"
            in 10001..1000000 -> "${counter/100}" + "K"
            else -> "${counter/1000}" + "M"
        }
    }

    fun counterPrinter(counterForPrint: Double): String {
        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.FLOOR
        return df.format(counterForPrint/1000).toDouble().toString()
    }

}

