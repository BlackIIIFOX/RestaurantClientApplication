package com.tamagotchi.restaurantclientapplication.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tamagotchi.tamagotchiserverprotocol.models.AccountInfoModel;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface AccountsDAO {
    @Insert(onConflict = REPLACE)
    void save(AccountInfoModel accountModel);

    @Query("SELECT * FROM accounts WHERE id = :userId")
    LiveData<AccountInfoModel> load(int userId);
}
