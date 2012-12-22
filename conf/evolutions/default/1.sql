# Tasks schema
 
# --- !Ups

CREATE SEQUENCE task_id_seq;
CREATE TABLE task (
    id integer NOT NULL DEFAULT nextval('task_id_seq'),
    title varchar(255),
	comment varchar(255),
	due_date timestamp
);

CREATE SEQUENCE project_id_seq;
CREATE TABLE project (
    id integer NOT NULL DEFAULT nextval('project_id_seq'),
    name varchar(255),
	comment varchar(255)
);
 
# --- !Downs

DROP TABLE project;
DROP SEQUENCE project_id_seq;
DROP TABLE task;
DROP SEQUENCE task_id_seq;