package kpfu.itis.myservice.data.db.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import kpfu.itis.myservice.data.db.models.User

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE vk_id = :id")
    fun getUserLocalById(id: Long): Single<User>

    @Query("SELECT * FROM users WHERE vk_id = :id")
    fun getUserLocalByIdForSup(id: Long): User

    @Query("DELETE FROM users WHERE vk_id = :id")
    fun deleteUserLocal(id: Long)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUserLocal(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserLocal(user: User)

    @Query("UPDATE users SET description = :description WHERE vk_id = :id" )
    fun updateDescrription(id: Long, description: String?)

    @Query("DELETE FROM notification")
    fun clearNotifications()

    @Query("DELETE FROM favorite")
    fun clearFavorites()

    @Query("DELETE FROM service")
    fun clearServices()

    @Transaction
    fun clear(id: Long) {
        deleteUserLocal(id)
        clearServices()
        clearFavorites()
        clearNotifications()
    }

}
