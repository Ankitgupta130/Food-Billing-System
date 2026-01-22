package com.avion.billing_system.repository;

import com.avion.billing_system.entity.SnackVote;
import com.avion.billing_system.repository.projection.SnackVoteCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SnackVoteRepository extends JpaRepository<SnackVote, Long> {
    boolean existsByEmployeeId(Long employeeId);

    String DATAQUERY = """
            SELECT
                s.name AS snack_name,
                e.name AS employee_name,
                COUNT(v.vote_id) AS vote_count
            FROM
                snack_votes v
            JOIN
                snack s ON v.snack_id = s.snack_id
            JOIN
                employees e ON v.employee_id = e.id
            GROUP BY
                s.name, e.name
            """;

    @Query(value = DATAQUERY, nativeQuery = true)
    List<SnackVoteCount> countVotesPerSnack();
}
