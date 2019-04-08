package org.moduleap.api.repositories;

import org.moduleap.api.models.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User getByUsername(String username);
    User getByEmail(String email);
}
