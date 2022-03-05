package com.example.room
import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.room.databinding.ActivityMainBinding
import com.example.room.databinding.UpdateDialogBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val employeeDao= (application as employeeApp).db.employeeDao()

        binding?.btn?.setOnClickListener {
            addRecord(employeeDao)
        }

        lifecycleScope.launch {
            employeeDao.fetchAllEmployees().collect {
                val list=ArrayList(it)
                setUpDataToRecyclerView(list,employeeDao)
            }
        }


    }//FUN

    fun addRecord(employeeDao: EmployeeDao){
        val name=binding?.edName?.text.toString()
        val email=binding?.edEmail?.text.toString()

        if (name.isNotEmpty() && email.isNotEmpty()){
            lifecycleScope.launch {
                employeeDao.insert(EmployeeEntity(name = name, email = email))
                Toast.makeText(applicationContext,"record was saved",Toast.LENGTH_SHORT).show()
                binding?.edName?.text?.clear()
                binding?.edEmail?.text?.clear()
            }
        }else{
            Toast.makeText(applicationContext,"name or email can't be empty",Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpDataToRecyclerView(employeesList:ArrayList<EmployeeEntity>,employeeDao:EmployeeDao){
        if(employeesList.isNotEmpty()){
            val itemAdapter = itemAdapter(employeesList,
                {updateID -> updateRecordDialog(updateID,employeeDao)},
                {deleteId -> deleteDialogRecord(deleteId,employeeDao)}
                )
            binding?.rv?.layoutManager=LinearLayoutManager(this)
            binding?.rv?.hasFixedSize()
            binding?.rv?.adapter=itemAdapter
            binding?.rv?.visibility=View.VISIBLE
            binding?.noRecord?.visibility=View.GONE
        }else{
            binding?.rv?.visibility=View.GONE
            binding?.noRecord?.visibility=View.VISIBLE
        }
    }

    private fun updateRecordDialog(id:Int , employeeDao:EmployeeDao){
        val updateDialog=Dialog(this,R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        val binding=UpdateDialogBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)
        lifecycleScope.launch {
            employeeDao.fetchEmployee(id).collect {
                if (it != null){
                    binding.edName.setText(it.name)
                    binding.edEmail.setText(it.email)
                }
            }
        }
        binding.btnUpdate.setOnClickListener {
            val name=binding.edName.text.toString()
            val email=binding.edEmail.text.toString()
            if (name.isNotEmpty() && email.isNotEmpty()){
                lifecycleScope.launch {
                    employeeDao.update(EmployeeEntity(id,name = name, email = email))
                    Toast.makeText(applicationContext,"record updated",Toast.LENGTH_SHORT).show()
                    updateDialog.dismiss()
                }
            }else{
                Toast.makeText(applicationContext,"name or email can't be empty",Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnCancel.setOnClickListener {
            updateDialog.dismiss()
        }
        updateDialog.show()
    }

    private fun deleteDialogRecord(id:Int , employeeDao: EmployeeDao){

        val builder=AlertDialog.Builder(this)
        builder.setTitle("Delete Record")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        lifecycleScope.launch {
            employeeDao.fetchEmployee(id)
                .collect {
                if (it != null){
                    builder.setMessage("are you sure you want to delete ${it.name}?")
                }
            }
        }

        builder.setPositiveButton("yes"){dialogInterface,_ ->
            lifecycleScope.launch {
                employeeDao.delete(EmployeeEntity(id,"",""))
                Toast.makeText(applicationContext,"record deleted successfully",Toast.LENGTH_SHORT).show()
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("no"){dialogInterface,_->
            dialogInterface.dismiss()
        }
        val alertDialog:AlertDialog=builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }












    override fun onDestroy() {
        super.onDestroy()
        binding =null
    }
}