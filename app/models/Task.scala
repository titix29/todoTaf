package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import java.util.{Date}

case class Task(id: Pk[Long], title: String, comment: String, dueDate: Option[Date], projectId: Option[Long])

object Task {

	def all(): List[Task] = DB.withConnection { implicit connection =>
		SQL("SELECT * FROM task").as(taskParser *)
	}
	
	def create(task: Task) = DB.withConnection { implicit connection =>
		SQL("INSERT INTO task(title, comment, due_date, project_id) VALUES({title}, {comment}, {dueDate}, {projectId})").on(
			'title -> task.title,
			'comment -> task.comment,
			'dueDate -> task.dueDate,
			'projectId -> task.projectId
		).executeUpdate()
	}
	
	def delete(id: Long) = DB.withConnection { implicit connection =>
		SQL("DELETE FROM task WHERE id = {id}").on(
			'id -> id
		).executeUpdate()
	}
	
	val taskParser = {
		get[Pk[Long]]("id") ~
		get[String]("title") ~
		get[String]("comment") ~
		get[Option[Date]]("due_date") ~ 
		get[Option[Long]]("project_id") map {
			case id~title~comment~dueDate~projectId => Task(id, title, comment, dueDate, projectId)
		}
	}
}