package br.com.geeknizado.Financial.Manager.internal.model;

import br.com.geeknizado.Financial.Manager.internal.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "categories")
@Data
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    @CreatedDate
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @OneToMany(mappedBy = "category")
    private List<Transaction> transactions;
}
