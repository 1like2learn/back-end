package com.lambdaschool.airbnb.airbnbapi.repository;

import com.lambdaschool.airbnb.airbnbapi.models.Useremail;
import org.springframework.data.repository.CrudRepository;

/**
 * The CRUD Repository connecting Useremail to the rest of the application
 */
public interface UseremailRepository
        extends CrudRepository<Useremail, Long>
{
}
