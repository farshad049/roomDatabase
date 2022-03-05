package com.example.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
 interface EmployeeDao {
    @Insert  suspend fun insert(employeeEntity:EmployeeEntity)

    @Update
     suspend fun update(employeeEntity: EmployeeEntity)

    @Delete
     suspend fun delete(employeeEntity: EmployeeEntity)

    @Query("SELECT * FROM `employee-table`")
      fun fetchAllEmployees(): Flow<List<EmployeeEntity>>

    @Query("SELECT * FROM `Employee-table` WHERE ID=:id")
      fun fetchEmployee(id :Int): Flow<EmployeeEntity>



}