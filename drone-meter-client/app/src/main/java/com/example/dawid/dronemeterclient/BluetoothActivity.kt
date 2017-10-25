package com.example.dawid.dronemeterclient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import org.jetbrains.anko.toast

class BluetoothActivity : AppCompatActivity() {

    var deviceBluetoothAdapter : BluetoothAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        val bluetoothManager : BluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val check = packageManager.hasSystemFeature("FEATURE_BLUETOOTH")
        //val checkBleSupport: Boolean = applicationContext.packageManager.hasSystemFeature("FEATURE_BLUETOOTH_LE")
        //deviceBluetoothAdapter = bluetoothManager.adapter
        if (check) deviceBluetoothAdapter = bluetoothManager.adapter
        else toast("BLE not supported")



    }

    fun checkBleSupport(bluetoothManager: BluetoothManager, context: Context) : Boolean = context.packageManager.hasSystemFeature("FEATURE_BLUETOOTH_LE") && bluetoothManager.adapter != null


}

