package com.hitori.dao;

import com.hitori.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountDAO extends JpaRepository<Account, String> {

    @Query("SELECT DISTINCT ar.account  FROM Authority ar WHERE ar.role.id IN ('DIRE', 'STAF')")
    List<Account> getAdministrators();

//    @Query("SELECT acc.username, acc.phone, acc.idcard, acc.fullname, auth.role " +
//            "FROM Account acc JOIN Authority auth ON acc.username = auth.account.username " +
//            "WHERE auth.role = 'CUST'")
//    List<Object[]> findCustomersWithRoleCUST();

    @Query("SELECT DISTINCT ar.account  FROM Authority ar WHERE ar.role.id IN ('CUST')")
    List<Account> CustomersWithRoleCUST();

}
