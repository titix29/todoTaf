package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Project(id: Pk[Long], name: String, comment: String)

object Project {
	
	def all(): List[Project] = DB.withConnection { implicit connection =>
		SQL("SELECT * FROM project").as(projectParser *)
	}
	
	def create(project: Project) = DB.withConnection { implicit connection =>
		SQL("INSERT INTO project(name, comment) VALUES({name}, {comment})").on(
			'name -> project.name,
			'comment -> project.comment
		).executeUpdate()
	}
	
	// TODO : withTransaction and reset task project_id
	def delete(id: Long) = DB.withConnection { implicit connection =>
		SQL("DELETE FROM project WHERE id = {id}").on(
			'id -> id
		).executeUpdate()
	}
	
	val projectParser = {
		get[Pk[Long]]("id") ~
		get[String]("name") ~
		get[String]("comment") map {
			case id~name~comment => Project(id, name, comment)
		}
	}
}