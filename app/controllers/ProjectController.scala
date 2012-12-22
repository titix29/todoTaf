package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import anorm._

import models._

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
			"id" -> ignored(NotAssigned: Pk[Long]),
			"name" -> nonEmptyText,
			"comment" -> text
		)(Project.apply)(Project.unapply)
	)
  
}