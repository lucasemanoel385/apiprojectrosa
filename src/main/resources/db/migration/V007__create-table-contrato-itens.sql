create table contract_itens(

    contract_id bigint,
    itens_cod bigint,
    foreign key(contract_id) references contract(id) ON DELETE CASCADE ON UPDATE NO ACTION,
    foreign key(itens_cod) references itens_contract(id) ON DELETE CASCADE ON UPDATE NO ACTION);
