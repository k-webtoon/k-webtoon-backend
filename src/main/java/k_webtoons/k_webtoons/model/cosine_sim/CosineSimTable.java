package k_webtoons.k_webtoons.model.cosine_sim;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cosine_sim_top10_table")
public class CosineSimTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seen_webtoon", nullable = false)
    private Long webtoon1;

    @Column(name = "recom_webtoon", nullable = false)
    private Long webtoon2;

    @Column(name = "cosine_sim", nullable = false)
    private Double similarityScore;
}
