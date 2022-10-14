package edu.gatech.cs6310.Repo;


import edu.gatech.cs6310.Entity.Line;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("lineRepo")
public interface LineRepo extends JpaRepository<Line, Long> {

    Line findLineById(long lineId);
    @Transactional
//    @Modifying
//    @Query("DELETE FROM Line line WHERE line.id= :lineId")
    Integer deleteLineById(long lineId);
}
