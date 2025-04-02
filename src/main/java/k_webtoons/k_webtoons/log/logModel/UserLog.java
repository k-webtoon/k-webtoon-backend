package k_webtoons.k_webtoons.log.logModel;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLog {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private String log;
}
