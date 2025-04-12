package com.wiseku.aplikasi_catatan

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var listView: LinearLayout
    private val prefsName = "CatatanPrefs"
    private val keyData = "catatanList"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listCatatan)

        findViewById<Button>(R.id.btnTambah).setOnClickListener {
            tampilkanPopupTambah()
        }

        muatCatatan()
    }

    private fun tampilkanPopupTambah() {
        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.popup_input, null)
        val dialog = AlertDialog.Builder(this)
            .setView(popupView)
            .create()

        val inputTanggal = popupView.findViewById<EditText>(R.id.inputTanggal)
        val inputCatatan = popupView.findViewById<EditText>(R.id.inputCatatan)
        val btnSimpan = popupView.findViewById<Button>(R.id.btnSimpan)

        btnSimpan.setOnClickListener {
            val tanggal = inputTanggal.text.toString()
            val catatan = inputCatatan.text.toString()

            if (tanggal.isNotEmpty() && catatan.isNotEmpty()) {
                simpanCatatan(tanggal, catatan)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Isi semua form!", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun simpanCatatan(tanggal: String, catatan: String) {
        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)
        val listString = prefs.getString(keyData, "[]")
        val jsonArray = JSONArray(listString)

        val obj = JSONObject()
        obj.put("tanggal", tanggal)
        obj.put("catatan", catatan)
        jsonArray.put(obj)

        prefs.edit().putString(keyData, jsonArray.toString()).apply()
        muatCatatan()
    }

    private fun muatCatatan() {
        listView.removeAllViews()
        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)
        val listString = prefs.getString(keyData, "[]")
        val jsonArray = JSONArray(listString)

        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(i)
            val textView = TextView(this).apply {
                text = "${item.getString("tanggal")} - ${item.getString("catatan")}"
                textSize = 18f
                setPadding(16, 16, 16, 16)
                setBackgroundResource(R.drawable.note_bg)
            }
            listView.addView(textView)
        }
    }
}
