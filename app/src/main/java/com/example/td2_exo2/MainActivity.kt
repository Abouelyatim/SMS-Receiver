package com.example.td2_exo2

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils.replace
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.TaskCompletionSource
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.popup.*


class MainActivity : AppCompatActivity() {

    companion object{
        val contact:MutableList<Contact> = ArrayList()
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_CONTACTS

        )
    }
    val SMS_PERMISSION_CODE=1
    val PICK_CONTACT_PERMISSION_CODE=2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        requestSmsPermission()

        button.setOnClickListener {
            dispatchContactIntent()
        }

    }
    fun dispatchContactIntent() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, PICK_CONTACT_PERMISSION_CODE)
    }
    private fun requestSmsPermission() {

        if (allPermissionsGranted()) {


        } else {
            ActivityCompat.requestPermissions(this,
                REQUIRED_PERMISSIONS,
                PICK_CONTACT_PERMISSION_CODE
            )

        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_PERMISSION_CODE) {
            val smsListener = SmsListener()
            val intentFilter = IntentFilter()
            intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED")
            registerReceiver(smsListener, intentFilter)
        }


    }

    var cNumber:String?=null
    var name:String?=null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_CONTACT_PERMISSION_CODE) {


            val contactData: Uri? = data?.data
            val c: Cursor = managedQuery(contactData, null, null, null, null);
            if (c.moveToFirst()) {


                val id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                val hasPhone =
                    c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                if (hasPhone.equals("1") ) {
                    val phones: Cursor? = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                        null, null
                    )
                    phones!!.moveToFirst()
                     cNumber = phones.getString(phones.getColumnIndex("data1"))

                    //Toast.makeText(this, "number is:" + cNumber, Toast.LENGTH_LONG).show()

                }
                 name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                //Toast.makeText(this, "number is:" + name, Toast.LENGTH_LONG).show()


                popup()


            }
        }


    }

    fun popup(){

        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // Inflate a custom view using layout inflater
        val view = inflater.inflate(R.layout.popup,null)

        // Initialize a new instance of popup window
        val popupWindow = PopupWindow(
            view, // Custom view to show in popup window
            LinearLayout.LayoutParams.MATCH_PARENT, // Width of popup window
            LinearLayout.LayoutParams.WRAP_CONTENT // Window height
        )

        // Set an elevation for the popup window
        popupWindow.elevation = 10.0F

        popupWindow.setFocusable(true)

        // If API level 23 or higher then execute the code
        // Create a new slide animation for popup window enter transition
        val slideIn = Slide()
        slideIn.slideEdge = Gravity.TOP
        popupWindow.enterTransition = slideIn

        // Slide animation for popup window exit transition
        val slideOut = Slide()
        slideOut.slideEdge = Gravity.RIGHT
        popupWindow.exitTransition = slideOut

        val buttonPopup = view.findViewById<Button>(R.id.button_popup)

        val mail:EditText=view.findViewById<EditText>(R.id.mail)
        buttonPopup.setOnClickListener{
            // Dismiss the popup window
            val ct=Contact(cNumber!!.replace("\\s".toRegex(), ""),mail.text.toString())
            contact.add(ct)

            Toast.makeText(this,"numero ajouter",Toast.LENGTH_SHORT).show()
            popupWindow.dismiss()
        }

        val nom:TextView= view.findViewById<Button>(R.id.nom)
        nom.setText(name)
        val numero:TextView= view.findViewById<Button>(R.id.numero)
        numero.setText(cNumber)


        TransitionManager.beginDelayedTransition(root_layout)
        popupWindow.showAtLocation(
            root_layout, // Location to display popup window
            Gravity.CENTER, // Exact position of layout to display popup
            0, // X offset
            0 // Y offset
        )


    }

}
