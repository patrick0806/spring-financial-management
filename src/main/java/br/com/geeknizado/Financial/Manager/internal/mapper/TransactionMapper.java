package br.com.geeknizado.Financial.Manager.internal.mapper;

import br.com.geeknizado.Financial.Manager.adapters.rest.dtos.TransactionDTO;
import br.com.geeknizado.Financial.Manager.internal.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TransactionMapper {
    public TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    TransactionDTO map(Transaction transaction);
    List<TransactionDTO> map(List<Transaction> transactions);
}
