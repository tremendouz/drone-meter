package com.example.dawid.dronemeterclient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import org.jetbrains.anko.toast
import android.content.pm.PackageManager


class MainActivity : AppCompatActivity() {

    var deviceBluetoothAdapter : BluetoothAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val bluetoothManager : BluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

        val check = packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)

        deviceBluetoothAdapter = bluetoothManager.adapter

        if (check) toast("BLE supported") else toast("BLE not supported")

    }

}
