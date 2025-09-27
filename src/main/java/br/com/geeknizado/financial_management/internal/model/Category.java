package br.com.geeknizado.financial_management.internal.model;

import br.com.geeknizado.financial_management.internal.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "categories")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 100)
    private String name;

    @Column(length = 15)
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    @CreatedDate
    private OffsetDateTime createdAt = OffsetDateTime.now();

    /*@OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Transaction> transactions;*/
}
