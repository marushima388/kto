package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import models._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, cr: Crawler, db: DBAccess) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def news() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.news(cr.mynaviNews()))
  }

  def list() = Action { implicit request: Request[AnyContent] =>
	val items:Seq[NewsList] = db.select_news_list()
    Ok(views.html.news(items))
  }

  def disp(link:String, title:String) = Action { implicit request: Request[AnyContent] =>
	request.session.get("USER_ID").map { userId =>
		db.insert_news_history(userId,title)
		Redirect("https://news.mynavi.jp"+link)
	}.getOrElse {
		Unauthorized("Oops, you are not connected")
   	}

  }

  def history(id:String) = Action { implicit request: Request[AnyContent] =>
		val his = db.select_news_history(id)
		Ok(his.toString)
   	}
}