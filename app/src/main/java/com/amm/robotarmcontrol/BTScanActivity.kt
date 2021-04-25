package com.amm.robotarmcontrol

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.controls.actions.ControlAction
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import org.jetbrains.anko.toast

class BTScanActivity : AppCompatActivity() {

    private var RAC_BT_Adapter: BluetoothAdapter? = null
    private lateinit var RAC_PairedDevices: Set<BluetoothDevice>
    private val REQUEST_ENABLE_BLUETOOTH = 1

    companion object {
        val EXTRA_ADDRESS: String = "Device_address"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_t_scan)

        RAC_BT_Adapter = BluetoothAdapter.getDefaultAdapter()
        if(RAC_BT_Adapter == null) {
            toast("This device doesn't have a bluetooth adapter")
            return
        }
        if (!RAC_BT_Adapter!!.isEnabled) {
            val enableBluetothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetothIntent, REQUEST_ENABLE_BLUETOOTH)
        }

        var scan_button = findViewById<Button>(R.id.scan_button)
        scan_button.setOnClickListener { pairedDeviceList() }



    }


    private fun pairedDeviceList(){
        RAC_PairedDevices = RAC_BT_Adapter!!.bondedDevices
        val list : ArrayList<BluetoothDevice> = ArrayList()
        val nlist : ArrayList<String> = ArrayList()

        if (!RAC_PairedDevices.isEmpty()) {
            for (device: BluetoothDevice in RAC_PairedDevices) {
                nlist.add(device.name)
                list.add(device)
                Log.i("device", ""+device.name)
            }
        } else {
            toast("no paired bluetooth devices found")
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nlist)
        var bt_list = findViewById<ListView>(R.id.bt_list)
        bt_list.adapter = adapter
        bt_list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val device: BluetoothDevice = list[position]
            val address: String = device.address

            val intent = Intent(this, ArmControlActivity::class.java)
            intent.putExtra(EXTRA_ADDRESS, address)
            startActivity(intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK){
                if(RAC_BT_Adapter!!.isEnabled){
                    toast("Bluetooth has been enabled")
                } else {
                    toast("Bluetooth has been disabled")
                }
            } else if (resultCode ==  Activity.RESULT_CANCELED){
                toast("Bluetooth connection has been cancelled")
            }
        }
    }
}
