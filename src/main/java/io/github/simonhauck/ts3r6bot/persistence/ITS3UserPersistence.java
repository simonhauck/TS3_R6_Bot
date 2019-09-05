package io.github.simonhauck.ts3r6bot.persistence;

import io.github.simonhauck.ts3r6bot.model.ts3.TS3User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ITS3UserPersistence extends CrudRepository<TS3User, Long> {

    TS3User getTS3UserBy_ts3UniqueIdentifier(String uid);

}
