package com.amm.robotarmcontrol

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import java.io.IOException
import java.util.*

class ArmControlActivity : AppCompatActivity() {

    companion object {
        var RAC_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var RAC_bluetoothSocket: BluetoothSocket? = null
        lateinit var RAC_progress: ProgressDialog
        lateinit var RAC_bluetoothAdapter: BluetoothAdapter
        var RAC_isConnected: Boolean = false
        lateinit var RAC_address: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arm_control)
        RAC_address = intent.getStringExtra(BTScanActivity.EXTRA_ADDRESS).toString()
        ConnectToDevice(this).execute()
        var RAC_BT_DISCONNECT = findViewById<Button>(R.id.Disconnect_Button)
        RAC_BT_DISCONNECT.setOnClickListener { disconnect() }
        var Command = ""
        var Grip_Move = findViewById<SeekBar>(R.id.Grip_Slider)
        var Wrist_Pitch_Move = findViewById<SeekBar>(R.id.Wrist_Pitch_Slider)
        var Wrist_Roll_Move = findViewById<SeekBar>(R.id.Wrist_Roll_Slider)
        var Elbow_Move = findViewById<SeekBar>(R.id.Elbow_Slider)
        var Shoulder_Move = findViewById<SeekBar>(R.id.Shoulder_Slider)
        var Waist_Move = findViewById<SeekBar>(R.id.Waist_slider)

        Grip_Move.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Command = "s1"+progress.toString()
                SendCommands(Command)
                println(Command)
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
                connectSuccess = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!connectSuccess) {
                Log.i("data", "couldn't connect")
            } else {
                RAC_isConnected = true
            }
            RAC_progress.dismiss()
        }
    }
}