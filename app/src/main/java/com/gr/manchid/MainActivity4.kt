package com.gr.manchid

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.gr.manchid.utils.OrgManager

class MainActivity4 : AppCompatActivity() {

    // Firebase
    private lateinit var auth: FirebaseAuth

    // Views
    private lateinit var cardRegistration: View
    private lateinit var progressbar : ProgressBar

    private lateinit var etInstitution: EditText
    private lateinit var etOrganizer: EditText
    private lateinit var etContact: EditText
    private lateinit var rgCategory: RadioGroup
    private lateinit var cbDeclaration: CheckBox

    private lateinit var btnSubmit: Button
    private lateinit var btnDashboard: Button
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var tvMyManchId: TextView

    private var mymanchId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        // ================================
        // INIT
        // ================================
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser ?: run {
            finish()
            return
        }

        bindViews()
        attachValidation()

        // ================================
        // CHECK ORGANIZER STATUS
        // ================================


        OrgManager.checkOrganizer(user.uid) { exists, id ->

            when {
                exists && !id.isNullOrEmpty() -> {
                    mymanchId = id
                    showRegisteredUI(id)
                }

                else -> {
                    showRegistrationUI()
                }
            }
        }


        // ================================
        // SUBMIT
        // ================================
        btnSubmit.setOnClickListener {
            registerOrganizer(user.uid)
        }

        // ================================
        // DASHBOARD
        // ================================
        btnDashboard.setOnClickListener {
            handleManageDashboardClick()
        }

        // ================================
        // UPDATE
        // ================================
        btnUpdate.setOnClickListener {
            updateOrganizer(user.uid)
        }

        // ================================
        // DELETE
        // ================================
        btnDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    // ================================
    // VIEW BINDING
    // ================================
    private fun bindViews() {
        etInstitution = findViewById(R.id.etInstitutionName)
        etOrganizer = findViewById(R.id.etOrganizerName)
        etContact = findViewById(R.id.etContact)
        rgCategory = findViewById(R.id.rgCategory)
        cbDeclaration = findViewById(R.id.cbDeclaration)
        cardRegistration = findViewById(R.id.cardRegistration)
        progressbar = findViewById(R.id.progressBar)


        btnSubmit = findViewById(R.id.btnSubmit)
        btnDashboard = findViewById(R.id.btnManageDashboard)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
        tvMyManchId = findViewById(R.id.tvMyManchId)

        btnSubmit.isEnabled = false
        btnSubmit.alpha = 0.5f
    }

    // ================================
    // TEXT WATCHER (FIXED)
    // ================================
    private val formWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            validateForm()
        }
    }

    private fun attachValidation() {
        etInstitution.addTextChangedListener(formWatcher)
        etOrganizer.addTextChangedListener(formWatcher)
        etContact.addTextChangedListener(formWatcher)

        rgCategory.setOnCheckedChangeListener { _, _ -> validateForm() }
        cbDeclaration.setOnCheckedChangeListener { _, _ -> validateForm() }
    }

    // ================================
    // VALIDATION
    // ================================
    private fun validateForm() {
        val valid = etInstitution.text.isNotBlank()
                && etOrganizer.text.isNotBlank()
                && etContact.text.isNotBlank()
                && rgCategory.checkedRadioButtonId != -1
                && cbDeclaration.isChecked

        btnSubmit.isEnabled = valid
        btnSubmit.alpha = if (valid) 1f else 0.5f
    }

    // ================================
    // REGISTER
    // ================================
    private fun registerOrganizer(uid: String) {

        val category = findViewById<RadioButton>(
            rgCategory.checkedRadioButtonId
        ).text.toString()

        val data = mapOf(
            "institutionName" to etInstitution.text.toString(),
            "organizerName" to etOrganizer.text.toString(),
            "contact" to etContact.text.toString(),
            "category" to category
        )

        OrgManager.registerOrganizer(
            uid,
            data,
            onSuccess = {
                mymanchId = it
                showRegisteredUI(it)
            },
            onError = {
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // ================================
    // UPDATE
    // ================================
    private fun updateOrganizer(uid: String) {
        val data = mapOf(
            "institutionName" to etInstitution.text.toString(),
            "organizerName" to etOrganizer.text.toString(),
            "contact" to etContact.text.toString()
        )

        OrgManager.updateOrganizer(
            uid,
            data,
            onSuccess = {
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
            },
            onError = {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // ================================
    // DELETE
    // ================================
    private fun deleteOrganizer() {
        val uid = auth.currentUser?.uid ?: return
        val id = mymanchId ?: return

        OrgManager.deleteOrganizer(
            uid,
            id,
            onSuccess = {
                recreate()
            },
            onFailure = {
                Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
            }
        )
    }


    // ================================
    // UI STATES
    // ================================
    private fun showRegisteredUI(id: String) {

        cardRegistration.visibility = View.GONE
        tvMyManchId.text = "MYMANCH ID: $id"
        tvMyManchId.visibility = View.VISIBLE
        progressbar.visibility = View.GONE

        btnDashboard.visibility = View.VISIBLE
        btnUpdate.visibility = View.VISIBLE
        btnDelete.visibility = View.VISIBLE
        btnSubmit.visibility = View.GONE
    }

    private fun showRegistrationUI() {
        btnSubmit.visibility = View.VISIBLE
        btnDelete.visibility = View.GONE
    }

    // delete confirmation dialogue
    private fun showDeleteConfirmationDialog() {

        AlertDialog.Builder(this)
            .setTitle("Delete Organizer")
            .setMessage(
                "Are you sure you want to delete this organizer?\n\n" +
                        "• Your MyManch ID will be released\n" +
                        "• This action cannot be undone"
            )
            .setPositiveButton("YES, DELETE") { _, _ ->
                deleteOrganizer()
            }
            .setNegativeButton("CANCEL", null)
            .setCancelable(true)
            .show()
    }


    // ================================
// HANDLE DASHBOARD FLOW
// ================================
    private fun handleManageDashboardClick() {

        val uid = auth.currentUser?.uid ?: return
        btnDashboard.isEnabled = false

        // 🔥 Step 1: ensure orgData exists (create only once)
        OrgManager.createOrgDataIfNotExists(
            uid = uid,
            onSuccess = {

                // 🔥 Step 2: ensure dashboard meta (merge-safe)
                OrgManager.initializeDashboard(
                    uid = uid,
                    onSuccess = {
                        openDashboard()   // 👉 Activity2
                    },
                    onFailure = {
                        btnDashboard.isEnabled = true
                        showToast(it)
                    }
                )
            },
            onFailure = {
                btnDashboard.isEnabled = true
                showToast(it)
            }
        )
    }

    // ================================
// INITIALIZE DASHBOARD (ONCE)
// ================================
    private fun initializeDashboard(uid: String) {

        OrgManager.initializeDashboard(
            uid = uid,
            onSuccess = {
                openDashboard()
            },
            onFailure = {
                btnDashboard.isEnabled = true
                showToast(it)
            }
        )
    }


    // ================================
// OPEN DASHBOARD
// ================================
    private fun openDashboard() {
        btnDashboard.isEnabled = true

        startActivity(
            Intent(this, MainActivity2::class.java)
        )
    }


    // ================================
// TOAST HELPER
// ================================
    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }









    //last class bracket
}
