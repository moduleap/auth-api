package org.moduleap.api.repositories;

import org.moduleap.api.models.VerifyUser;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VerifyUserRepository extends PagingAndSortingRepository<VerifyUser,Long> {
    VerifyUser getByVerifyKey(String key);
}
