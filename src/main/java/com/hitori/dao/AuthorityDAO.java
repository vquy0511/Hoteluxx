package com.hitori.dao;

import com.hitori.entity.Account;
import com.hitori.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorityDAO extends JpaRepository<Authority, Integer> {
    @Query("SELECT DISTINCT a FROM Authority a WHERE a.account IN ?1")
    List<Authority> authoritiesOf(List<Account> accounts);
    @Query("SELECT COUNT(a) FROM Authority a WHERE a.role = 'CUST'")
    Long countAuthoritiesByRoleCus();
}
