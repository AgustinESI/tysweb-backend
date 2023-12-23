package edu.uclm.esi.tecsistweb.repository;

import edu.uclm.esi.tecsistweb.model.Match;
import edu.uclm.esi.tecsistweb.model.User;
import org.springframework.data.repository.CrudRepository;

public interface MatchDAO extends CrudRepository<Match, String> {


}
