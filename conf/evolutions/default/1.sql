# Tasks schema
 
# --- !Ups

CREATE SEQUENCE task_id_seq;
CREATE TABLE task (
    id integer NOT NULL DEFAULT nextval('task_id_seq'),
    title varchar(255),
	comment varchar(255),
	due_date timestamp
);
 
# --- !Downs
 
DROP TABLE task;
DROP SEQUENCE task_id_seq;