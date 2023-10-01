package com.example.jobguru.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.jobguru.R
import com.example.jobguru.databinding.ActivityEmpApplicantsBinding
import com.example.jobguru.databinding.ActivityEmpProfileBinding
import com.example.jobguru.view.EmpEditProfileFragment
import com.example.jobguru.view.EmpInterviewActivity
import com.example.jobguru.view.EmpJobsActivity
import com.example.jobguru.view.EmpNavigationFragment
import com.example.jobguru.viewmodel.EmpProfileViewModel

class EmpProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmpProfileBinding
    private lateinit var viewModel: EmpProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmpProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavigationBar()
        viewModel = ViewModelProvider(this).get(EmpProfileViewModel::class.java)
        val sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val empEmail = sharedPreferences.getString("personInChargeEmail", "") ?: ""

        viewModel.getProfileData(empEmail)

        var eName = ""
        var eIndustry = ""
        var eAddress = ""
        var ePostcode = ""
        var eState = ""
        var ePersonInChargeName = ""
        var ePersonInChargeContact = ""
        var ePersonInChargeDesignation = ""
        var ePersonInChargeGender = ""

        viewModel.empLogo.observe(this,
            { empLogo -> binding.tvEmployerLogoImage.setImageBitmap(empLogo) })

        viewModel.empName.observe(this, { empName ->
            binding.tvEmployerName.text = empName
            eName = empName
        })

        viewModel.empIndustry.observe(this, { empIndustry ->
            binding.tvEmployerIndustry.text = empIndustry
            eIndustry = empIndustry
        })
        viewModel.empAddress.observe(this, { empAddress ->
            viewModel.empPostcode.observe(this, { empPostcode ->
                viewModel.empState.observe(this, { empState ->
                    val combinedAddress = "$empAddress $empPostcode $empState"
                    binding.tvEmployerAddress.text = combinedAddress
                    eAddress = empAddress
                    ePostcode = empPostcode
                    eState = empState
                })
            })
        })
        viewModel.personInChargeName.observe(this, { personInChargeName ->
            binding.tvPersonInchargeName.text = personInChargeName
            ePersonInChargeName = personInChargeName
        })
        viewModel.personInChargeContact.observe(this, { personInChargeContact ->
            binding.tvPersonInchargeContact.text = personInChargeContact
            ePersonInChargeContact = personInChargeContact
        })
        viewModel.personInChargeDesignation.observe(this, { personInChargeDesignation ->
            binding.tvPersonInchargeDesignation.text = personInChargeDesignation
            ePersonInChargeDesignation = personInChargeDesignation
        })
        viewModel.personInChargeGender.observe(this, { personInChargeGender ->
            binding.tvPersonInchargeGender.text = personInChargeGender
            ePersonInChargeGender = personInChargeGender
        })
        viewModel.personInChargeEmail.observe(this, { personInChargeEmail ->
            binding.tvPersonInchargeEmail.text = personInChargeEmail
        })

        binding.editProfileBtn.setOnClickListener {
            openEditProfileFragment(
                empEmail,
                eName,
                eIndustry,
                eAddress,
                ePostcode,
                eState,
                ePersonInChargeName,
                ePersonInChargeContact,
                ePersonInChargeDesignation,
                ePersonInChargeGender
            )
        }

        binding.logoutBtn.setOnClickListener {
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            //
            Toast.makeText(
                this,
                "Log Out Successfully",
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    private fun bottomNavigationBar() {
        binding.bottomNavigationView.setSelectedItemId(R.id.profile)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.jobs -> {
                    val intent = Intent(applicationContext, EmpJobsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.applicants -> {
                    val intent = Intent(applicationContext, EmpApplicantsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.interviews -> {
                    val intent = Intent(applicationContext, EmpInterviewActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.profile -> {
                    true
                }

                else -> false
            }
        }
    }

    private fun openEditProfileFragment(
        empEmail: String,
        eName: String,
        eIndustry: String,
        eAddress: String,
        ePostcode: String,
        eState: String,
        ePersonInChargeName: String,
        ePersonInChargeContact: String,
        ePersonInChargeDesignation: String,
        ePersonInChargeGender: String
    ) {
        val empEditProfileFragment = EmpEditProfileFragment()

        val bundle = Bundle()
        bundle.putString("personInChargeEmail", empEmail)
        bundle.putString("empName", eName)
        bundle.putString("empIndustry", eIndustry)
        bundle.putString("empAddress", eAddress)
        bundle.putString("empPostcode", ePostcode)
        bundle.putString("empState", eState)
        bundle.putString("personInChargeName", ePersonInChargeName)
        bundle.putString("personInChargeContact", ePersonInChargeContact)
        bundle.putString("personInChargeDesignation", ePersonInChargeDesignation)
        bundle.putString("personInChargeGender", ePersonInChargeGender)

        empEditProfileFragment.arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, empEditProfileFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}