create table expenses(
    id bigint not null auto_increment,
    description varchar(300),
    value decimal(10,2) not null,
    date date not null,
	primary key(id));
 
