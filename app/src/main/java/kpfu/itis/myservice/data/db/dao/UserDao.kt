package kpfu.itis.myservice.data.db.dao

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import kpfu.itis.myservice.data.db.models.UserLocal
import org.intellij.lang.annotations.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE vk_id = :id")
    fun getUserLocalById(id: Long): Flowable<UserLocal>

    @Query("DELETE FROM users WHERE vk_id = :id")
    fun deleteUserLocal(id: Long)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUserLocal(user: UserLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserLocal(user: UserLocal)

}
