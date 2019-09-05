package io.github.simonhauck.ts3r6bot.persistence;

import io.github.simonhauck.ts3r6bot.model.r6.R6Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRainbowUserPersistence extends CrudRepository<R6Player, Long> {

}
