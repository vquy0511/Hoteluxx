package com.hitori.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "bookingdetail")
public class BookingDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    int status;
    String paxname1;
    String paxname2;
    String paxname3;
    String imagecard1;
    String imagecard2;
    String imagecard3;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "check_in") // Chỉ định tên cột đúng trong Entity
    private Date check_in;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "check_out")
    Date check_out;

    @ManyToOne
    @JoinColumn(name = "Bookingid")
    Booking booking;

    @ManyToOne
    @JoinColumn(name = "Roomid") // Khóa ngoại đến phòng
    Room room;

}
