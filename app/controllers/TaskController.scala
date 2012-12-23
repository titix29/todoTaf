package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import anorm._

import models._

object TaskController extends Controller {
  
	def index = Action {
		Ok(views.html.tasks(Task.all(), Project.all(), taskForm))
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
  
	def deleteTask(id: Long) = Action {
		Task.delete(id)
		Redirect(routes.TaskController.index)
	}
	
	val taskForm = Form(
		mapping(
			"id" -> ignored(NotAssigned: Pk[Long]),
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