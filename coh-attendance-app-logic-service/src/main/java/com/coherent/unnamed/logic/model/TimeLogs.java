package com.coherent.unnamed.logic.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="time_logs")

public class TimeLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "latitude")
    private String latitude;

/*
    @Column(name ="user_id_fk")
    private long userIdFk;
*/
   @ManyToOne(cascade= CascadeType.MERGE)
   @JoinColumn(name="user_id_fk", referencedColumnName = "id")
   private Users users;

    @Column(name = "is_logged")
    private int isLogged;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "deleted_flag")
    private boolean deletedFlag;

    @Column(name = "created_by")
    private String createdBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at")
    private Timestamp modifiedAt;

    @Column(name = "modified_by")
    private String modifiedBy;

/*   @Column(name = "status")
    private boolean status;*/

    /*@OneToMany
    @JoinColumn(name = "user_id_fk")
    private User user;*/


}
