package models

import scala.math.BigDecimal._

import play.api.db._
import play.api.Play.current
import play.api.libs.json._

import anorm._
import anorm.SqlParser._

import java.util.{Date}

case class Task(id: Option[Long], title: String, comment: String, dueDate: Option[Date], status: TaskStatus.Value = TaskStatus.NOT_STARTED, projectId: Option[Long])

object Task {

	def all(): List[Task] = DB.withConnection { implicit connection =>
		SQL("SELECT * FROM task").as(taskParser *)
	}
	
	def findById(id: Long): Option[Task] = DB.withConnection { implicit connection =>
		SQL("SELECT * FROM task WHERE id = {id}").on(
			'id -> id
		).as(taskParser.singleOpt)
	}
	
	def create(task: Task): Option[Long] = DB.withConnection { implicit connection =>
		SQL("INSERT INTO task(title, comment, due_date, status, project_id) VALUES({title}, {comment}, {dueDate}, {status}, {projectId})").on(
			'title -> task.title,
			'comment -> task.comment,
			'dueDate -> task.dueDate,
			'status -> task.status.toString,
			'projectId -> task.projectId
		).executeInsert()
	}
	
	def delete(id: Long) = DB.withConnection { implicit connection =>
		SQL("DELETE FROM task WHERE id = {id}").on(
			'id -> id
		).executeUpdate()
	}
	
	val taskParser = {
		get[Option[Long]]("id") ~
		get[String]("title") ~
		get[String]("comment") ~
		get[Option[Date]]("due_date") ~ 
		get[String]("status") ~
		get[Option[Long]]("project_id") map {
			case id~title~comment~dueDate~status~projectId => Task(id, title, comment, dueDate, TaskStatus.withName(status), projectId)
		}
	}
	
	// Cannot use simple/default (de)serializer (http://mandubian.com/2012/11/11/JSON-inception/) because of Pk[Long] type
	implicit val taskWrites = Json.writes[Task]
	implicit val taskStatusReads: Reads[TaskStatus.Value] = EnumUtils.enumReads(TaskStatus)
	implicit val taskReads = Json.reads[Task]
	// http://mandubian.com/2012/10/01/unveiling-play-2-dot-1-json-api-part2-writes-format-combinators/
	/*
	implicit object TaskFormat extends Format[Task] {
		
		def reads(json: JsValue) = JsSuccess(Task(
			(json \ "id").asOpt[Long],
			(json \ "title").as[String],
			(json \ "comment").as[String],
			(json \ "dueDate").asOpt[Date],
			TaskStatus.withName((json \ "status").as[String]),
			(json \ "projectId").asOpt[Long]
		))
		
		def writes(task: Task) = JsObject(Seq(
			"id" -> JsNumber(task.id.get),
			"title" -> JsString(task.title),
			"comment" -> JsString(task.comment),
			"dueDate" -> JsString(task.dueDate.map(date => date.toString).getOrElse("N/A")),
			"status" -> JsString(task.status.toString),
			"projectId" -> JsNumber(long2bigDecimal(task.projectId.getOrElse(Long.box(-1))))
		))
	}
	*/
}

object TaskStatus extends Enumeration {
	// cf. http://stackoverflow.com/questions/10227231/how-to-reference-scala-enumeration-from-other-package
	type TaskStatus = Value
	
	val NOT_STARTED = Value("NOT_STARTED")
	val PENDING = Value("PENDING")
	val FINISHED = Value("FINISHED")
}

// http://stackoverflow.com/questions/15488639/how-to-write-readst-and-writest-in-scala-enumeration-play-framework-2-1
object EnumUtils {
  def enumReads[E <: Enumeration](enum: E): Reads[E#Value] = new Reads[E#Value] {
    def reads(json: JsValue): JsResult[E#Value] = json match {
      case JsString(s) => {
        try {
          JsSuccess(enum.withName(s))
        } catch {
          case _: NoSuchElementException => JsError(s"Enumeration expected of type: '${enum.getClass}', but it does not appear to contain the value: '$s'")
        }
      }
      case _ => JsError("String value expected")
    }
  }

  def enumWrites[E <: Enumeration]: Writes[E#Value] = new Writes[E#Value] {
    def writes(v: E#Value): JsValue = JsString(v.toString)
  }
}
