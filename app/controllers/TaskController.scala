package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json._

import play.api.data._
import play.api.data.Forms._

import play.api.i18n.Messages.Implicits._

import play.api.Play.current

import anorm._

import models._

object TaskController extends Controller {
  
	def index = Action {
		Ok(views.html.tasks(Task.all(), Project.all(), taskForm))
	}
	
	def findById(id: Long) = Action {
		Task.findById(id).map { t =>  Ok(toJson(t)) }.getOrElse(NotFound)
	}
  
	def newTask = Action { implicit request =>
		taskForm.bindFromRequest.fold(
			errors => BadRequest(views.html.tasks(Task.all(), Project.all(), errors)),
			task => {
				Task.create(task)
				Redirect(routes.TaskController.index)
			}
		)
	}
	
	def updateTask(id: Long) = TODO
  
	def deleteTask(id: Long) = Action {
		Task.delete(id)
		Redirect(routes.TaskController.index)
	}
	
	val taskForm = Form(
		mapping(
			"id" -> ignored(Option(-1L)),
			"title" -> nonEmptyText,
			"comment" -> text,
			"dueDate" -> optional(date("dd/MM/yyyy")),
			"status" -> optional(text),
			"projectId" -> optional(longNumber)
		)
		((id, title, comment, dueDate, status, projectId) => Task(id, title, comment, dueDate, TaskStatus.withName(status.getOrElse("NOT_STARTED")), projectId))
		((task: Task) => Some(task.id, task.title, task.comment, task.dueDate, Option(task.status.toString), task.projectId))
	)
  
}
