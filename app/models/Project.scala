package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import java.util.{Date}

case class Project(id: Pk[Long], name: String, comment: String, creationDate: Date)

object Project {
	
	def all(): List[Project] = DB.withConnection { implicit connection =>
		SQL("SELECT * FROM project").as(projectParser *)
	}
	
	def create(project: Project): Option[Long] = DB.withConnection { implicit connection =>
		SQL("INSERT INTO project(name, comment, creation_date) VALUES({name}, {comment}, {creationDate})").on(
			'name -> project.name,
			'comment -> project.comment,
			'creationDate -> project.creationDate
		).executeInsert()
	}
	
	def delete(id: Long) = DB.withTransaction { implicit connection =>
		// set the tasks to no project
		SQL("UPDATE task SET project_id = NULL WHERE project_id = {id}").on(
			'id -> id
		).executeUpdate()
	
		SQL("DELETE FROM project WHERE id = {id}").on(
			'id -> id
		).executeUpdate()
	}
	
	val projectParser = {
		get[Pk[Long]]("id") ~
		get[String]("name") ~
		get[String]("comment") ~
		get[Date]("creation_date") map {
			case id~name~comment~creationDate => Project(id, name, comment, creationDate)
		}
	}
}