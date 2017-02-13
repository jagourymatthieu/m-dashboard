CREATE TABLE categorie (id integer not null primary key, libelle varchar(30) not null);
CREATE TABLE programme (id integer not null primary key, libelle varchar(30) not null, id_type integer not null);
CREATE TABLE muscle (id integer not null primary key, libelle varchar(30) not null);
CREATE TABLE comic (id integer not null primary key, titre varchar(255) not null, numero int not null, date_sortie date not null, prix integer not null, achete boolean not null default false, id_cat integer not null);
CREATE TABLE exercice (id integer not null primary key, libelle varchar(30) not null, id_muscle integer not null);
CREATE TABLE type (id integer not null primary key, libelle varchar(30) not null);
CREATE TABLE entrainement (id integer not null primary key, serie integer not null, repetition integer not null, id_exercice integer not null, id_programme integer not null);


ALTER TABLE programme ADD CONSTRAINT FK_id_type FOREIGN KEY (id_type) REFERENCES TYPE (id) MATCH FULL;
ALTER TABLE comic ADD CONSTRAINT FK_id_cat FOREIGN KEY (id_cat) REFERENCES CATEGORIE (id) MATCH FULL;
ALTER TABLE exercice ADD CONSTRAINT FK_id_muscle FOREIGN KEY (id_muscle) REFERENCES MUSCLE (id) MATCH FULL;
ALTER TABLE entrainement ADD CONSTRAINT FK_id_exercice FOREIGN KEY (id_exercice) REFERENCES EXERCICE (id) MATCH FULL;
ALTER TABLE entrainement ADD CONSTRAINT FK_id_programme FOREIGN KEY (id_programme) REFERENCES PROGRAMME (id) MATCH FULL;
