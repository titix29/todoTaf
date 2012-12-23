# Tasks schema
 
# --- !Ups

CREATE SEQUENCE project_id_seq;
CREATE TABLE project (
    id integer NOT NULL DEFAULT nextval('project_id_seq'),
    name varchar(255),
	comment varchar(255),
	creation_date timestamp,
	primary key(id)
);

CREATE SEQUENCE task_id_seq;
CREATE TABLE task (
    id integer NOT NULL DEFAULT nextval('task_id_seq'),
    title varchar(255),
	comment varchar(255),
	due_date timestamp,
	status varchar(50),
	project_id integer,
	primary key(id),
	foreign key(project_id) references project(id)
);

 
# --- !Downs

DROP TABLE task;
DROP SEQUENCE task_id_seq;
DROP TABLE project;
DROP SEQUENCE project_id_seq;
