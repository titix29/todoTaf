package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import play.api.i18n.Messages.Implicits._

import play.api.Play.current

import anorm._

import models._

import java.util.{Date}

object ProjectController extends Controller {
  
  	def index = Action {
		Ok(views.html.projects(Project.all(), projectForm))
	}

	def newProject = Action { implicit request =>
		projectForm.bindFromRequest.fold(
			errors => BadRequest(views.html.projects(Project.all(), errors)),
			project => {
				Project.create(project)
				Redirect(routes.ProjectController.index)
			}
		)
	}
  
	def deleteProject(id: Long) = Action {
		Project.delete(id)
		Redirect(routes.ProjectController.index)
	}
  
	val projectForm = Form(
		mapping(
			"id" -> ignored(Option(-1L)),
			"name" -> nonEmptyText,
			"comment" -> text,
			// TODO: check if ignored or optional
			"creationDate" -> ignored(new Date)
		)(Project.apply)(Project.unapply)
	)
  
}
