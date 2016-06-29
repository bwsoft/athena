create table markers (
  id INT not null auto_increment primary key,
  name varchar(60) not null,
  address varchar(80) not null,
  lat float(10,6) not null,
  lng float(10,6) not null
) engine = MYISAM;

