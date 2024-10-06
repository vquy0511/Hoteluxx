package com.hitori.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Data
@Table(name = "Authorities", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"Username", "Roles"})
})
public class Authority implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne @JoinColumn(name = "Username")
    private Account account;

    @ManyToOne  @JoinColumn(name = "Roles")
    private Role role;
}
