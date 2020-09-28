package com.example.hw3

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.material.snackbar.Snackbar

val permMap: HashMap<String, Int> = mapOf(
    Manifest.permission.READ_CONTACTS to 1,
    Manifest.permission.SEND_SMS to 2
) as HashMap<String, Int>

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!checkPerm(Manifest.permission.READ_CONTACTS)) showAlert()
        if (checkPerm(Manifest.permission.READ_CONTACTS)) showContactList()
    }

    fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage(R.string.alert_text)
            setPositiveButton(R.string.ok) { _, _ -> requirePerm(Manifest.permission.READ_CONTACTS) }
        }
        builder.create()
        builder.show()
    }

    fun showContactList() {
        val contactList: List<Contact> =
            if (checkPerm(Manifest.permission.READ_CONTACTS)) fetchAllContacts() else emptyList()
        Toast.makeText(
            this,
            resources.getQuantityString(
                R.plurals.contact_found,
                contactList.size,
                contactList.size
            ),
            Toast.LENGTH_SHORT
        ).show()
        contact_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ContactRecyclerViewAdapter(contactList,
                {
                    startActivity(Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:${it.phone}")
                    })
                },
                {
                    requirePerm(Manifest.permission.SEND_SMS)
                    if (checkPerm(Manifest.permission.SEND_SMS))
                        startActivity(Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("smsto:${it.phone}")
                            putExtra("sms_body",resources.getText(R.string.sms_text))
                        })
                })
        }
    }

    fun checkPerm(permission: String) = (ContextCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED)

    fun requirePerm(permission: String) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(permission),
            permMap[permission]!!
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            requestCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (requestCode == 1)
                        showContactList()
                } else {
                    Snackbar.make(
                        contact_main_layout,
                        R.string.snackbar_text,
                        if (requestCode == 1) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }
}

