package kpfu.itis.myservice.data.db.dao

import androidx.room.*
import io.reactivex.Single
import kpfu.itis.myservice.data.db.models.Service

@Dao
interface ServiceDao {

    @Query("SELECT * FROM service")
    fun getServices() : Single<List<Service>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addService(service: Service)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addServices(services: List<Service>?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateService(service: Service)

    @Query("SELECT * FROM service WHERE ser_id = :id")
    fun getService(id: Long) : Single<Service>

    @Query("DELETE FROM service WHERE ser_id = :id")
    fun deleteService(id: Long)

}
