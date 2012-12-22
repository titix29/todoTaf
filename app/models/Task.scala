package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import java.util.{Date}

case class Task(id: Pk[Long], title: String, comment: String, dueDate: Option[Date])

object Task {

	def all() : List[Task] = DB.withConnection { implicit connection =>
		SQL("SELECT * FROM Task").as(taskParser *)
	}
	
	def create(task: Task) = DB.withConnection { implicit connection =>
		SQL("INSERT INTO Task(title, comment, due_date) VALUES({title}, {comment}, {dueDate})").on(
			'title -> task.title,
			'comment -> task.comment,
			'dueDate -> task.dueDate
		).executeUpdate()
	}
	
	def delete(id: Long) = DB.withConnection { implicit connection =>
		SQL("DELETE FROM Task WHERE id = {id}").on(
			'id -> id
		).executeUpdate()
	}
	
	val taskParser = {
		get[Pk[Long]]("id") ~
		get[String]("title") ~
		get[String]("comment") ~
		get[Option[Date]]("due_date") map {
			case id~title~comment~dueDate => Task(id, title, comment, dueDate)
		}
	}
}