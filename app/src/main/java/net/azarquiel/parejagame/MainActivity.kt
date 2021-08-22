package net.azarquiel.parejagame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.size
import kotlinx.android.synthetic.main.activity_main.*
import net.azarquiel.parejagame.model.Ficha
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    var ivmatriz = Array(6) { arrayOfNulls<ImageView>(5)}
    var vector=ArrayList<Int>()
    var contador = 0
    var primeraFicha = 0
    var segundaFicha = 0
    var tapando = false

    var filaPrimeraFicha = 0
    var filaSegundaFicha = 0
    var columnaPrimeraFicha = 0
    var columnaSegundaFicha = 0

    var parejaFila1 = 0
    var parejaFila2 = 0
    var parejaColumna1 = 0
    var parejaColumna2 = 0
    var contadorParejas = 0
    var contadorIntentos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        makeTablero()
        begin()
    }

    private fun makeTablero() {
        for (i in 0 until 2)
            for (j in 0 until 15)
                vector.add(j)
        vector.shuffle()

        var cont = 0
        var id: Int
        for (i in 0 until lv.size){
            val lh = lv.getChildAt(i) as LinearLayout
            for (j in 0 until lh.size) {
                ivmatriz[i][j] = lh.getChildAt(j) as ImageView
                ivmatriz[i][j]!!.tag = Ficha(i,j,vector[cont])
                id = resources.getIdentifier("pokemon${vector[cont]}","drawable",packageName)
                ivmatriz[i][j]!!.setBackgroundResource(id)
                ivmatriz[i][j]!!.setOnClickListener(this)
                cont++
            }
        }
    }
    override fun onClick(v: View?) {
        if(tapando)return
        val imagenPulsada = v as ImageView
        val ficha = imagenPulsada.tag as Ficha

        ivmatriz[ficha.i][ficha.j]!!.setImageResource(android.R.color.transparent) //Para quitar la tapa cuando se pulsa
        //.......
        Toast.makeText(this, "(${ficha.i},${ficha.j}) -> Figura ${ficha.pokemon}", Toast.LENGTH_LONG).show()

        if (contador > 0){//Entra con la segunda ficha seleccionada y las compara
            segundaFicha = ficha.pokemon
            filaSegundaFicha = ficha.i
            columnaSegundaFicha = ficha.j

            if (primeraFicha == segundaFicha){
                ivmatriz[filaPrimeraFicha][columnaPrimeraFicha]!!.setImageResource(android.R.color.transparent)
                ivmatriz[filaSegundaFicha][columnaSegundaFicha]!!.setImageResource(android.R.color.transparent)
                ivmatriz[filaPrimeraFicha][columnaPrimeraFicha]!!.isEnabled = false
                ivmatriz[filaSegundaFicha][columnaSegundaFicha]!!.isEnabled = false
                contadorParejas++
                contadorIntentos++
                parejaFila2 = ficha.i
                parejaColumna2 = ficha.j
                contador--
            }else{
                ivmatriz[filaPrimeraFicha][columnaPrimeraFicha]!!.isEnabled = true
                ivmatriz[filaSegundaFicha][columnaSegundaFicha]!!.isEnabled = true
                contadorIntentos++
                tapando = true
                ponerTapa()
                contador--
            }

        }else{//Entra con la primera ficha seleccionada
            primeraFicha = ficha.pokemon
            filaPrimeraFicha = ficha.i
            columnaPrimeraFicha = ficha.j
            ivmatriz[filaPrimeraFicha][columnaPrimeraFicha]!!.isEnabled = false
            contador++
        }

        if(contadorParejas == 15){
            finJuego()
        }

    }

    private fun ponerTapa() {

        doAsync {
            SystemClock.sleep(1000)
            uiThread {
                ivmatriz[filaPrimeraFicha][columnaPrimeraFicha]!!.setImageResource(R.drawable.tapa)
                ivmatriz[filaSegundaFicha][columnaSegundaFicha]!!.setImageResource(R.drawable.tapa)
                ivmatriz[filaPrimeraFicha][columnaPrimeraFicha]!!.isEnabled = true
                ivmatriz[filaSegundaFicha][columnaSegundaFicha]!!.isEnabled = true
                tapando = false
            }
        }
    }

    private fun tapaAll() {
        ivmatriz.forEach {
            // el objeto es el it en este caso la imagen
            it.forEach {
                it!!.setImageResource(R.drawable.tapa)
            }
        }
    }

    private fun begin() {
        doAsync {
            SystemClock.sleep(5000)
            uiThread {
                tapaAll()
                tapando = false
            }
        }
    }



    private fun finJuego() {
        alert("FELICIDADES!!!! LO HICISTE EN $contadorIntentos INTENTOS", "GAME OVER"){
            yesButton {  }
        }.show()
    }
}


