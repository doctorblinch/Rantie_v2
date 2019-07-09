package doctorblinch.com

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var main_cost_meter = findViewById(R.id.main_input_price_of_meter) as EditText
        var main_cost_rent = findViewById(R.id.main_input_cost_of_rent) as EditText
        var main_quantity_of_meter = findViewById(R.id.main_input_quantity_of_meters) as EditText
        var main_tax = findViewById(R.id.main_input_tax) as EditText
        var main_delay = findViewById(R.id.main_input_delay) as EditText
        var button_next = findViewById(R.id.main_button_next) as Button


        button_next.setOnClickListener {


            var meter_cost = 0.0f
            try {
                meter_cost = main_cost_meter.text.toString().toFloat()
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Неверное значение цены за метр!", Toast.LENGTH_SHORT).show()
            }

            var cost_rent = 0.0f
            try {
                cost_rent = main_cost_rent.text.toString().toFloat()
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Неверное значение цены аренды!", Toast.LENGTH_SHORT).show()
            }
            var quantity_of_meters = 0.0f
            try {
                quantity_of_meters = main_quantity_of_meter.text.toString().toFloat()
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Неверное значение кол-ва метров!", Toast.LENGTH_SHORT).show()
            }
            var tax = 0.0f
            if (main_tax.text.toString() != "") {
                try {
                    tax = main_tax.text.toString().toFloat()
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Неверное значение налога!", Toast.LENGTH_SHORT).show()
                }
            }
            if (tax > 100.0f){
                Toast.makeText(this, "Неверное значение налога!", Toast.LENGTH_SHORT).show()
            }
            var delay = 0.0f
            if (main_delay.text.toString() != "") {
                try {
                    delay = main_delay.text.toString().toFloat()
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Неверное значение отсрочки!", Toast.LENGTH_SHORT).show()
                }
            }

            var message = ""
            var investment_attr = (12.0f * cost_rent * (1.0f - tax/100.0f))/ meter_cost
            investment_attr = 1.0f / (1.0f / investment_attr + delay / 12.0f)
            message += "Инвестиционная привлекательность - " + 100*investment_attr + "%\n\n"
            var total_price = meter_cost * quantity_of_meters
            message += "Общая стоимость помещения - " + format_output(total_price.toInt()) + "\n\n"
            var rent_total = 12.0f * cost_rent * quantity_of_meters
            message += "Годовая прибыль аренды - " + format_output(rent_total.toInt()) + "\n\n"
            var investment_return = investment_return(investment_attr)
            message += investment_return

            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Результат:")
            alertDialog.setMessage(message)
            // alertDialog.setMessage(meter_cost.toString() + cost_rent.toString() + quantity_of_meters.toString() + tax.toString() +delay.toString())
            alertDialog.show()
        }

    }

    private fun investment_return(investment_attr: Float): String {
        var result = "Возврат инвестиций через\n"
        var years = (1 / investment_attr).toInt()
        var months = ceil((1 / investment_attr - years) * 12.0f).toInt()
        if (months == 12){
            months = 0
            years += 1
        }
        if (years == 0) {

        } else if ((years == 11) || (years == 12)) {
            result += years.toString() + " лет "
        } else {
            when (years % 10) {
                0 -> result += years.toString() + " лет "
                1 -> result += years.toString() + " год "
                in 2..4 -> result += years.toString() + " года "
                in 5..9 -> result += years.toString() + " лет "
            }
        }


        when (months){
            1 -> result += months.toString() + " месяц\n"
            in 2..4 -> result += months.toString() + " месяца\n"
            in 5..12 -> result += months.toString() + " месяцев\n"
            else -> result += "\n"
        }
        //Toast.makeText(this, years.toString() + months.toString(), Toast.LENGTH_SHORT).show()
        return result
    }

    private fun format_output(number : Int): String{
        var result = ""
        if (number < 10_000){
            return number.toString()
        }
        if (number < 1_000_000){
            result = number.toString()
            result = result.substring(0,result.length - 3) + "'" + result.substring(result.length-3, result.length)
            return result
        }
        if (number < 1_000_000_000){
            result = number.toString()
            result = result.substring(0,result.length - 6) + "'" + result.substring(result.length - 6,result.length - 3) + "'" + result.substring(result.length-3, result.length)
            return result
        }
        return number.toString()
    }

}
