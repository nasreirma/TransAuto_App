@file:Suppress("DEPRECATION")

package com.amm.robotarmcontrol

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.IOException
import java.util.*
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.*
import com.amm.robotarmcontrol.R.color.Green

@Suppress("DEPRECATION")
class ArmControlActivity : AppCompatActivity() {

    companion object {
        var RAC_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var RAC_bluetoothSocket: BluetoothSocket? = null
        lateinit var RAC_progress: ProgressDialog
        lateinit var RAC_bluetoothAdapter: BluetoothAdapter
        var RAC_isConnected: Boolean = false
        lateinit var RAC_address: String
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arm_control)
        RAC_address = intent.getStringExtra(BTScanActivity.EXTRA_ADDRESS).toString()
        ConnectToDevice(this).execute()
        val RAC_BT_DISCONNECT = findViewById<Button>(R.id.Disconnect_Button)
        RAC_BT_DISCONNECT.setOnClickListener { disconnect() }
        var Command: String
        val Grip_Move = findViewById<SeekBar>(R.id.Grip_Slider)
        val Wrist_Pitch_Move = findViewById<SeekBar>(R.id.Wrist_Pitch_Slider)
        val Wrist_Roll_Move = findViewById<SeekBar>(R.id.Wrist_Roll_Slider)
        val Elbow_Move = findViewById<SeekBar>(R.id.Elbow_Slider)
        val Shoulder_Move = findViewById<SeekBar>(R.id.Shoulder_Slider)
        val Waist_Move = findViewById<SeekBar>(R.id.Waist_slider)
        val Speed_Move = findViewById<SeekBar>(R.id.Speed_slider)
        val RUN = findViewById<Button>(R.id.RUN)
        val RESET = findViewById<Button>(R.id.RESET)
        val SAVE = findViewById<Button>(R.id.SAVE)
        var RUN_STAT : Boolean = false


        Grip_Move.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Command = "s1"+progress.toString()
                SendCommands(Command)
                findViewById<TextView>(R.id.Grip).text = "Grip : $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        Wrist_Pitch_Move.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Command = "s2"+progress.toString()
                SendCommands(Command)
                findViewById<TextView>(R.id.Wrist_Pitch).text = "Wrist Pitch : $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        Wrist_Roll_Move.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Command = "s3"+progress.toString()
                SendCommands(Command)
                findViewById<TextView>(R.id.Wrist_Roll).text = "Wrist Roll : $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        Elbow_Move.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Command = "s4"+progress.toString()
                SendCommands(Command)
                findViewById<TextView>(R.id.Elbow).text = "Elbow : $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        Shoulder_Move.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Command = "s5"+progress.toString()
                SendCommands(Command)
                findViewById<TextView>(R.id.Shoulder).text = "Shoulder : $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        Waist_Move.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Command = "s6"+progress.toString()
                SendCommands(Command)
                findViewById<TextView>(R.id.Waist).text = "Waist : $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        Speed_Move.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Command = "ss"+progress.toString()
                SendCommands(Command)
                findViewById<TextView>(R.id.Speed).text = "Speed : $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        SAVE.setOnClickListener {
            SendCommands("SAVE")
        }

        RESET.setOnClickListener {
            SendCommands("RESET")
        }

        RUN.setOnClickListener {
            if (RUN_STAT == false){
                SendCommands("RUN")
                RUN.setText("PAUSE")
                RUN.setBackgroundColor(RED)
            }
            else if (RUN_STAT == true){
                SendCommands("PAUSE")
                RUN.setText("RUN")
                RUN.setBackgroundColor(Green)
            }
            RUN_STAT = !RUN_STAT
        }
    }




    private fun SendCommands(input : String) {
        if (RAC_bluetoothSocket != null) {
            try{
                RAC_bluetoothSocket!!.outputStream.write(input.toByteArray())
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun disconnect() {
        if (RAC_bluetoothSocket != null) {
            try {
                RAC_bluetoothSocket!!.close()
                RAC_bluetoothSocket = null
                RAC_isConnected = false
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        finish()
    }

    private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>() {
        private var connectSuccess: Boolean = true
        private val context: Context

        init {
            this.context = c
        }

        override fun onPreExecute() {
            super.onPreExecute()
            RAC_progress = ProgressDialog.show(context, "Connecting...", "please wait")
        }

        override fun doInBackground(vararg p0: Void?): String? {
            try {
                if (RAC_bluetoothSocket == null || !RAC_isConnected) {
                    RAC_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device: BluetoothDevice = RAC_bluetoothAdapter.getRemoteDevice(RAC_address)
                    RAC_bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(RAC_myUUID)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    RAC_bluetoothSocket!!.connect()
                }
            } catch (e: IOException) {
                connectSuccess =  false
                e.printStackTrace()
            }
            return null
        }

         override fun onPostExecute(result: String?) {
             (context as ArmControlActivity)
            super.onPostExecute(result)
            fun showAlert() {
                MaterialAlertDialogBuilder(context).setTitle("Error").setMessage("Error communicating with the Bluetooth device").setPositiveButton("Try again"){_, _ ->
                    val intent = Intent(context,BTScanActivity::class.java)
                    context.startActivity(intent)
                    context.finish()
                }
                    .show()
            }
            if (!connectSuccess) {
                showAlert()
            } else {
                RAC_isConnected = true
                context.findViewById<TextView>(R.id.textView2).text = "Connected"
                context.findViewById<TextView>(R.id.textView2).setTextColor(GREEN.toInt())
            }
            RAC_progress.dismiss()
        }
    }
}




