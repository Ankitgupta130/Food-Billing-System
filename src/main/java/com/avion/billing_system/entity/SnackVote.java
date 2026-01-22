package com.avion.billing_system.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "snack_votes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"employee_id", "vote_date"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SnackVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;

    @ManyToOne
    @JoinColumn(name = "snack_id", nullable = false)
    private Snack snackId;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    @Column(name = "voted_date", nullable = false)
    private LocalDateTime voteDate;

}
