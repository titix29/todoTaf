package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import java.util.{Date}

case class Task(id: Pk[Long], title: String, comment: String, dueDate: Option[Date], status: TaskStatus.Value = TaskStatus.NOT_STARTED, projectId: Option[Long])

object Task {

	def all(): List[Task] = DB.withConnection { implicit connection =>
		SQL("SELECT * FROM task").as(taskParser *)
	}
	
	def create(task: Task) = DB.withConnection { implicit connection =>
		SQL("INSERT INTO task(title, comment, due_date, status, project_id) VALUES({title}, {comment}, {dueDate}, {status}, {projectId})").on(
			'title -> task.title,
			'comment -> task.comment,
			'dueDate -> task.dueDate,
			'status -> task.status.toString,
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
		get[String]("status") ~
		get[Option[Long]]("project_id") map {
			case id~title~comment~dueDate~status~projectId => Task(id, title, comment, dueDate, TaskStatus.withName(status), projectId)
		}
	}
}

object TaskStatus extends Enumeration {
	// cf. http://stackoverflow.com/questions/10227231/how-to-reference-scala-enumeration-from-other-package
	type TaskStatus = Value
	
	val NOT_STARTED = Value("NOT_STARTED")
	val PENDING = Value("PENDING")
	val FINISHED = Value("FINISHED")
}