package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import java.time.LocalDate
import java.util.Random
import models._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

@Singleton
class LoginController @Inject()(mcc: MessagesControllerComponents, db: DBAccess) extends MessagesAbstractController(mcc) {

	val loginForm = Form(
		mapping(
			"userId" -> text,
			"userPass" -> text
		)
		(LoginUser.apply)(LoginUser.unapply)
	)

	def loginInit() = Action { implicit request: MessagesRequest[AnyContent] =>
		Ok(views.html.login(loginForm))
	}

	def loginSubmit() = Action { implicit request: MessagesRequest[AnyContent] =>
		loginForm.bindFromRequest.fold(
			errors => {
				BadRequest(views.html.login(errors))
			},
			success => {
				val user: LoginUser = loginForm.bindFromRequest.get
				val userDB = db.select_login(user.userId).head
				if (user.userPass == userDB.userPass) {
					
					Redirect(routes.LoginController.top()).withSession(request.session + ("USER_ID" -> user.userId))
				}
				else {
					BadRequest(views.html.login(loginForm))
				}
			}
		)
	}

	def top() = Action { implicit request: MessagesRequest[AnyContent] =>
		request.session.get("USER_ID").map { userId =>
			val items:Seq[NewsList] = db.select_news_list()
			Ok(views.html.top(userId)(items))
    	}.getOrElse {
      		Unauthorized("Oops, you are not connected")
    	}
	}

	// def test(id: String) = Action { implicit request: MessagesRequest[AnyContent] =>
	// def test() = Action { implicit request: MessagesRequest[AnyContent] =>
		// val aaa = db.select_login(id)
		// Ok(db.select_login(id).head.userId)
		// Ok("a")
	// }


}