create table scheduling(
    id bigint not null auto_increment,
    name varchar(100),
    description varchar(300),
    time varchar(10) not null,
	date_scheduling date not null,
	primary key(id));
 
