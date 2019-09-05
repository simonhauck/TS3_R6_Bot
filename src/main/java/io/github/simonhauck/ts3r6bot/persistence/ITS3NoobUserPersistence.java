package io.github.simonhauck.ts3r6bot.persistence;

import io.github.simonhauck.ts3r6bot.model.ts3.TS3NoobUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITS3NoobUserPersistence extends CrudRepository<TS3NoobUser, Long> {

    TS3NoobUser getTS3NoobUserBy_ts3UniqueIdentifier(String uid);

    @Query(value = "SELECT * from noob_user n WHERE n.active = True Order By n.noob_time_stamp", nativeQuery = true)
    List<TS3NoobUser> getAllActiveTS3NoobUsers();
}
