package com.example.dawid.dronemeterclient

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import org.jetbrains.anko.toast
import android.content.pm.PackageManager
import android.os.Handler
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import android.bluetooth.le.BluetoothLeScanner




class MainActivity : AppCompatActivity(), AnkoLogger {

    lateinit var deviceBluetoothAdapter: BluetoothAdapter
    val REQUEST_ENABLE_BT = 1
    val REQUEST_FINE_LOCATION = 2
    val SCANNING = false
    val SCAN_PERIOD: Long = 6000
    lateinit var bleScanner : BluetoothLeScanner
    lateinit var bleScanResults: Map<String, BluetoothDevice>
    lateinit var bleScanCallback: ScanCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val bluetoothManager: BluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE)
                as BluetoothManager
        deviceBluetoothAdapter = bluetoothManager.adapter


        if (checkBleSupported()) toast("BLE supported") else toast("BLE not supported")
        validatePermissions()
        startBleScan()

        val handler = Handler()
        handler.postDelayed({ this.stopBleScan() }, SCAN_PERIOD)
    }


    fun checkBleSupported() = packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
            && deviceBluetoothAdapter != null


    fun requestEnableBluetooth() {

        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        info("Requested user to enable bluetooth.")
    }

    fun checkFineLocationEnabled() = packageManager.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, packageName) ==
            PackageManager.PERMISSION_GRANTED


    fun requestEnableFineLocation() = requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_FINE_LOCATION)


    fun validatePermissions() {
        if (!deviceBluetoothAdapter?.isEnabled!!) requestEnableBluetooth()
        else if (!checkFineLocationEnabled()) requestEnableFineLocation()

    }


    fun startBleScan() {
        toast("Ble scan started")
        bleScanner = deviceBluetoothAdapter?.bluetoothLeScanner

        bleScanResults = mutableMapOf()

        bleScanCallback = BleScanCallback(bleScanResults as MutableMap<String, BluetoothDevice>)

        bleScanner?.startScan(bleScanCallback)

    }

    fun stopBleScan() {
        toast("Ble scan stopped")
        bleScanner.stopScan(bleScanCallback)
        bleScanResults.keys.forEach { address -> info("$address")}
        if (bleScanResults.size == 0) info("No device was found")
    }


    class BleScanCallback(scanResult: MutableMap<String, BluetoothDevice>) : ScanCallback(), AnkoLogger {

        var scanResults = scanResult

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            addScanResult(result)
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            results?.forEach({ result -> addScanResult(result) })
        }

        override fun onScanFailed(errorCode: Int) {
            info("Bluetooth LE scan failed. Error code: $errorCode")

        }

        fun addScanResult(result: ScanResult?) {
            val device: BluetoothDevice = result!!.device
            val deviceAdress: String = device.address
            scanResults.put(deviceAdress, device)
        }

        }

    }




