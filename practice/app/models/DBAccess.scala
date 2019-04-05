package models

import javax.inject._
import play.api.mvc._
import play.api.db._
import anorm._
import anorm.SqlParser._
import scala.language.postfixOps

import javax.inject._
import play.api._
import play.api.mvc._
import java.time.LocalDate
import java.util.Date
import java.util.Random
import models._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

import javax.inject.Inject

import anorm.SqlParser._
import anorm._
import play.api.db.DBApi

import scala.language.postfixOps

import play.api.libs.json.{JsString,Json}
import play.api.Play.current

@javax.inject.Singleton
class DBAccess @Inject() (dbapi: DBApi) {

	private val db = dbapi.database("default")

	// val parser = str("user_id") ~ str("user_pass")
	val simple = {
		get[String]("user_auth.user_id") ~
		get[String]("user_auth.user_pass") map {
			case userId~userPass => LoginUser(userId, userPass)
		}
	}

	val simpleNewsList = {
		get[String]("news_list.news_title") ~
		get[String]("news_list.news_link") map {
			case newsTitle~newsLink => NewsList(newsTitle, newsLink)
		}
	}

	val simpleNewsHistory = {
		get[String]("news_list.news_title") ~
		get[String]("news_list.news_link")~
		get[Date]("news_history.date_time") map {
			case hisTitle~hisLink~hisDateTime => NewsHistory(hisTitle, hisLink, hisDateTime.toString)
		}
	}

	def select_login(id: String): Seq[LoginUser] = {
	// def select_login(): List[String~String] = {
		db.withConnection { implicit c =>
			SQL(
				"""
				select * from user_auth where user_id = {id}
				"""
			).on(
				"id" -> id
			).as(simple *)
			
		}
	}

	def insert_news_list(title: String, link: String) {
		db.withConnection { implicit c =>
			SQL(
				"insert ignore into news_list values({title}, {link})"
			).on(
				"title" -> title,
				"link" -> link,
			).executeUpdate()
		}
	}

	def insert_news_history(id: String, title: String) {
		db.withConnection { implicit c =>
			SQL(
				"insert into news_history (user_id, news_title) values({id}, {title})"
			).on(
				"id" -> id,
				"title" -> title
			).executeUpdate()
		}
	}

	def select_news_list(): Seq[NewsList] = {
		db.withConnection { implicit c =>
			SQL(
				"""
				select * from news_list
				"""
			).as(simpleNewsList *)
		}
	}

	def select_news_history(id: String): Seq[NewsHistory] = {
		db.withConnection { implicit c =>
			SQL(
				"""
				select news_list.news_title, news_list.news_link, news_history.date_time
				from news_list
				inner join news_history
				on news_list.news_title = news_history.news_title
				and news_history.user_id = {id}
				"""
			).on(
				"id" -> id
			).as(simpleNewsHistory *)
			
		}
	}


	// def userlist = Action {
	// 	db.withConnection { implicit c =>
	// 		val record = SQL("select * from user_auth").as(simple*)
	// 		// // val seikei = record.apply().map (
	// 		// 	row => Map(
	// 		// 		"user_id" -> JsString(row[String]("user_id")),
	// 		// 		"user_pass" -> JsString(row[String]("user_pass"))
	// 		// 	)
	// 		// }.toList

	// 	Ok(Json.toJson(record))
	// }
// }
}