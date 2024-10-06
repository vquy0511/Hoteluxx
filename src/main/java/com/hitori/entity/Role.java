package com.hitori.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "roles")
public class Role implements Serializable {
    @Id
    private String id;
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "role")
    List<Authority> authorities;
}
