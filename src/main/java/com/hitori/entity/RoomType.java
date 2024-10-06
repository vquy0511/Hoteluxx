package com.hitori.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.w3c.dom.html.HTMLDocument;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roomtype")
@Getter
@Setter
public class RoomType implements Serializable {
    @Id
    @Column(nullable = false)
    int id;
    String name;
    String describe;
    String policy;
    int size;
    private String bed;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    float price;

    @JsonIgnore
    @OneToMany(mappedBy = "roomType",fetch = FetchType.EAGER)
    private List<Room> rooms;
}
