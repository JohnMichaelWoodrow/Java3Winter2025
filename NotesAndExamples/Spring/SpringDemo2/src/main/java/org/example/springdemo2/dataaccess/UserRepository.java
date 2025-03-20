package org.example.springdemo2.dataaccess;

import org.example.springdemo2.pojos.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    // Code beyond basics
}
