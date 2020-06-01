package kpfu.itis.myservice.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single
import kpfu.itis.myservice.data.db.models.Favorite

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(service: Favorite) : Completable

    @Query("DELETE FROM favorite WHERE ser_id = :id")
    fun delete(id: Long) : Completable

    @Query("SELECT * FROM favorite")
    fun get(): Single<List<Favorite>>

}
